package com.yandex.ydb.core.grpc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.net.ssl.SSLException;

import com.google.common.net.HostAndPort;
import com.google.common.util.concurrent.MoreExecutors;
import com.yandex.ydb.core.Issue;
import com.yandex.ydb.core.Result;
import com.yandex.ydb.core.StatusCode;
import com.yandex.ydb.core.auth.AuthProvider;
import com.yandex.ydb.core.auth.NopAuthProvider;
import com.yandex.ydb.core.rpc.OperationTray;
import com.yandex.ydb.core.rpc.RpcTransport;
import com.yandex.ydb.core.rpc.RpcTransportBuilder;
import com.yandex.ydb.core.rpc.StreamObserver;
import com.yandex.yql.proto.IssueSeverity.TSeverityIds.ESeverityId;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.LoadBalancer;
import io.grpc.LoadBalancerProvider;
import io.grpc.LoadBalancerRegistry;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.util.internal.StringUtil;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;


/**
 * @author Sergey Polovko
 * @author Evgeniy Pshenitsin
 */
public class GrpcTransport implements RpcTransport {

    public static final int DEFAULT_PORT = 2135;

    private static final Logger logger = Logger.getLogger(GrpcTransport.class.getName());

    private final ManagedChannel realChannel;
    private final Channel channel;
    private final CallOptions callOptions;
    private final GrpcOperationTray operationTray;
    private final long defautlReadTimeoutMillis;

    private GrpcTransport(Builder builder) {
        this.realChannel = createChannel(builder);
        this.channel = interceptChannel(realChannel, builder);
        this.callOptions = createCallOptions(builder);
        this.defautlReadTimeoutMillis = builder.getReadTimeoutMillis();
        this.operationTray = new GrpcOperationTray(this);
    }

    public static Builder forHost(String host, int port) {
        return new Builder(null, null, singletonList(HostAndPort.fromParts(host, port)));
    }

    public static Builder forHosts(HostAndPort... hosts) {
        checkNotNull(hosts, "hosts is null");
        checkArgument(hosts.length > 0, "empty hosts array");
        return new Builder(null, null, Arrays.asList(hosts));
    }

    public static Builder forHosts(List<HostAndPort> hosts) {
        checkNotNull(hosts, "hosts is null");
        checkArgument(!hosts.isEmpty(), "empty hosts list");
        return new Builder(null, null, hosts);
    }

    public static Builder forEndpoint(String endpoint, String database) {
        checkNotNull(endpoint, "endpoint is null");
        checkNotNull(database, "database is null");
        return new Builder(endpoint, database, null);
    }

    public <ReqT, RespT> CompletableFuture<Result<RespT>> unaryCall(
        MethodDescriptor<ReqT, RespT> method,
        ReqT request,
        long deadlineAfter)
    {
        CallOptions callOptions = this.callOptions;
        if (deadlineAfter > 0) {
            final long now = System.nanoTime();
            if (now >= deadlineAfter) {
                return completedFuture(deadlineExpiredResult(method));
            }
            callOptions = this.callOptions.withDeadlineAfter(deadlineAfter - now, TimeUnit.NANOSECONDS);
        } else if (defautlReadTimeoutMillis > 0) {
            callOptions = this.callOptions.withDeadlineAfter(defautlReadTimeoutMillis, TimeUnit.MILLISECONDS);
        }

        CompletableFuture<Result<RespT>> promise = new CompletableFuture<>();
        ClientCall<ReqT, RespT> call = channel.newCall(method, callOptions);
        sendOneRequest(call, request, new UnaryStreamToFuture<>(promise));
        return promise;
    }

    public <ReqT, RespT> void unaryCall(
        MethodDescriptor<ReqT, RespT> method,
        ReqT request,
        Consumer<Result<RespT>> consumer,
        long deadlineAfter)
    {
        CallOptions callOptions = this.callOptions;
        if (deadlineAfter > 0) {
            final long now = System.nanoTime();
            if (now >= deadlineAfter) {
                consumer.accept(deadlineExpiredResult(method));
                return;
            }
            callOptions = this.callOptions.withDeadlineAfter(deadlineAfter - now, TimeUnit.NANOSECONDS);
        } else if (defautlReadTimeoutMillis > 0) {
            callOptions = this.callOptions.withDeadlineAfter(defautlReadTimeoutMillis, TimeUnit.MILLISECONDS);
        }

        ClientCall<ReqT, RespT> call = channel.newCall(method, callOptions);
        sendOneRequest(call, request, new UnaryStreamToConsumer<>(consumer));
    }

    public <ReqT, RespT> void unaryCall(
        MethodDescriptor<ReqT, RespT> method,
        ReqT request,
        BiConsumer<RespT, Status> consumer,
        long deadlineAfter)
    {
        CallOptions callOptions = this.callOptions;
        if (deadlineAfter > 0) {
            final long now = System.nanoTime();
            if (now >= deadlineAfter) {
                consumer.accept(null, deadlineExpiredStatus(method));
                return;
            }
            callOptions = this.callOptions.withDeadlineAfter(deadlineAfter - now, TimeUnit.NANOSECONDS);
        } else if (defautlReadTimeoutMillis > 0) {
            callOptions = this.callOptions.withDeadlineAfter(defautlReadTimeoutMillis, TimeUnit.MILLISECONDS);
        }

        ClientCall<ReqT, RespT> call = channel.newCall(method, callOptions);
        sendOneRequest(call, request, new UnaryStreamToBiConsumer<>(consumer));
    }

    public <ReqT, RespT> void serverStreamCall(
        MethodDescriptor<ReqT, RespT> method,
        ReqT request,
        StreamObserver<RespT> observer,
        long deadlineAfter)
    {
        CallOptions callOptions = this.callOptions;
        if (deadlineAfter > 0) {
            final long now = System.nanoTime();
            if (now >= deadlineAfter) {
                observer.onError(GrpcStatuses.toStatus(deadlineExpiredStatus(method)));
                return;
            }
            callOptions = this.callOptions.withDeadlineAfter(deadlineAfter - now, TimeUnit.NANOSECONDS);
        } else if (defautlReadTimeoutMillis > 0) {
            callOptions = this.callOptions.withDeadlineAfter(defautlReadTimeoutMillis, TimeUnit.MILLISECONDS);
        }

        ClientCall<ReqT, RespT> call = channel.newCall(method, callOptions);
        sendOneRequest(call, request, new ServerStreamToObserver<>(observer, call));
    }

    private static <ReqT, RespT> void sendOneRequest(
        ClientCall<ReqT, RespT> call,
        ReqT request,
        ClientCall.Listener<RespT> listener)
    {
        try {
            call.start(listener, new Metadata());
            call.request(1);
            call.sendMessage(request);
            call.halfClose();
        } catch (Throwable t) {
            try {
                call.cancel(null, t);
            } catch (Throwable ex) {
                logger.log(Level.SEVERE, "Exception encountered while closing the call", ex);
            }
            listener.onClose(Status.INTERNAL.withCause(t), null);
        }
    }

    @Override
    public OperationTray getOperationTray() {
        return operationTray;
    }

    @Override
    public void close() {
        operationTray.close();
        realChannel.shutdown();
    }

    private static ManagedChannel createChannel(Builder builder) {
        String endpoint = builder.getEndpoint();
        String database = builder.getDatabase();
        List<HostAndPort> hosts = builder.getHosts();
        assert (endpoint != null) == (database != null);
        assert (endpoint == null) != (hosts == null);

        final String localDc = builder.getLocalDc();

        String defaultPolicy = "round_robin";

        if (!StringUtil.isNullOrEmpty(localDc)) {
            defaultPolicy = "prefer_nearest";
            final LoadBalancerRegistry lbr = LoadBalancerRegistry.getDefaultRegistry();

            lbr.register(new LoadBalancerProvider() {
                @Override
                public boolean isAvailable() {
                    return true;
                }

                @Override
                public int getPriority() {
                    return 10;
                };

                @Override
                public String getPolicyName() {
                    return "prefer_nearest";
                }

                @Override
                public LoadBalancer newLoadBalancer(LoadBalancer.Helper helper) {
                    return new PreferNearestLoadBalancer(helper, localDc);
                }
            });
        }

        final NettyChannelBuilder channelBuilder;
        if (endpoint != null) {
            channelBuilder = NettyChannelBuilder.forTarget(YdbNameResolver.makeTarget(endpoint, database))
                .nameResolverFactory(YdbNameResolver.newFactory(builder.getAuthProvider(), builder.cert))
                .defaultLoadBalancingPolicy(defaultPolicy);
        } else if (hosts.size() > 1) {
            channelBuilder = NettyChannelBuilder.forTarget(HostsNameResolver.makeTarget(hosts))
                .nameResolverFactory(HostsNameResolver.newFactory(hosts))
                .defaultLoadBalancingPolicy(defaultPolicy);
        } else {
            channelBuilder = NettyChannelBuilder.forAddress(new InetSocketAddress(
                hosts.get(0).getHost(),
                hosts.get(0).getPortOrDefault(DEFAULT_PORT)));
        }

        if (builder.cert != null) {
            channelBuilder
                .negotiationType(NegotiationType.TLS)
                .sslContext(createSslContext(builder.cert));
        } else {
            channelBuilder.negotiationType(NegotiationType.PLAINTEXT);
        }

        channelBuilder
            .maxInboundMessageSize(64 << 20) // 64 MiB
            .withOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT);

        builder.getChannelInitializer().accept(channelBuilder);
        return channelBuilder.build();
    }

    private static SslContext createSslContext(byte[] cert) {
        try {
            return GrpcSslContexts.forClient()
                .trustManager(new ByteArrayInputStream(cert))
                .build();
        } catch (SSLException e) {
            throw new RuntimeException("cannot create ssl context", e);
        }
    }

    private static Channel interceptChannel(ManagedChannel realChannel, Builder builder) {
        if (builder.getDatabase() == null) {
            return realChannel;
        }

        Metadata extraHeaders = new Metadata();
        extraHeaders.put(YdbHeaders.DATABASE, builder.getDatabase());
        extraHeaders.put(YdbHeaders.BUILD_INFO, builder.getVersionString());
        ClientInterceptor interceptor = MetadataUtils.newAttachHeadersInterceptor(extraHeaders);
        return ClientInterceptors.intercept(realChannel, interceptor);
    }

    private static CallOptions createCallOptions(Builder builder) {
        CallOptions callOptions = CallOptions.DEFAULT;
        AuthProvider authProvider = builder.getAuthProvider();
        if (authProvider != NopAuthProvider.INSTANCE) {
            callOptions = callOptions.withCallCredentials(new YdbCallCredentials(authProvider));
        }
        if (builder.getCallExecutor() != MoreExecutors.directExecutor()) {
            callOptions = callOptions.withExecutor(builder.getCallExecutor());
        }
        return callOptions;
    }

    private static <T> Result<T> deadlineExpiredResult(MethodDescriptor<?, T> method) {
        String message = "deadline expired before calling method " + method.getFullMethodName();
        return Result.fail(StatusCode.CLIENT_DEADLINE_EXCEEDED, Issue.of(message, ESeverityId.S_ERROR));
    }

    private static Status deadlineExpiredStatus(MethodDescriptor<?, ?> method) {
        String message = "deadline expired before calling method " + method.getFullMethodName();
        return Status.DEADLINE_EXCEEDED.withDescription(message);
    }

    /**
     * BUILDER
     */
    @ParametersAreNonnullByDefault
    public static final class Builder extends RpcTransportBuilder<GrpcTransport, Builder> {
        private final String endpoint;
        private final String database;
        private final List<HostAndPort> hosts;
        private byte[] cert = null;
        private Consumer<NettyChannelBuilder> channelInitializer = (cb) -> {};
        private String localDc;

        private Builder(@Nullable String endpoint, @Nullable String database, @Nullable List<HostAndPort> hosts) {
            this.endpoint = endpoint;
            this.database = database;
            this.hosts = hosts;
        }

        @Nullable
        public List<HostAndPort> getHosts() {
            return hosts;
        }

        @Nullable
        public String getEndpoint() {
            return endpoint;
        }

        @Nullable
        public String getDatabase() {
            return database;
        }

        public String getVersionString() {
            Properties prop = new Properties();

            try {
                InputStream in = getClass().getResourceAsStream("/version.properties");
                prop.load(in);

                String arcadiaSourceUrl = prop.getProperty("ArcadiaSourceUrl");
                String arcadiaLastChangeNum = prop.getProperty("ArcadiaLastChangeNum");
                String version = prop.getProperty("version");

                if (version != null) {
                    return version;
                } else {
                    return arcadiaSourceUrl + "@" + arcadiaLastChangeNum;
                }

            } catch (Exception ex) { }

            return "unknown-version";
        }

        public Consumer<NettyChannelBuilder> getChannelInitializer() {
            return channelInitializer;
        }

        public String getLocalDc() {
            return localDc;
        }

        public Builder withChannelInitializer(Consumer<NettyChannelBuilder> channelInitializer) {
            this.channelInitializer = checkNotNull(channelInitializer, "channelInitializer is null");
            return this;
        }

        public Builder withSecureConnection(byte[] cert) {
            this.cert = cert.clone();
            return this;
        }

        public Builder withLocalDataCenter(String dc) {
            this.localDc = dc;
            return this;
        }

        @Override
        public GrpcTransport build() {
            return new GrpcTransport(this);
        }
    }
}
