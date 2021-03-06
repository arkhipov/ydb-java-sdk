package com.yandex.ydb.core.grpc;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.yandex.ydb.OperationProtos.Operation;
import com.yandex.ydb.StatusCodesProtos.StatusIds.StatusCode;
import com.yandex.ydb.core.Operations;
import com.yandex.ydb.core.auth.AuthProvider;
import com.yandex.ydb.discovery.DiscoveryProtos.EndpointInfo;
import com.yandex.ydb.discovery.DiscoveryProtos.ListEndpointsRequest;
import com.yandex.ydb.discovery.DiscoveryProtos.ListEndpointsResponse;
import com.yandex.ydb.discovery.DiscoveryProtos.ListEndpointsResult;
import com.yandex.ydb.discovery.v1.DiscoveryServiceGrpc;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.Status;
import io.grpc.internal.GrpcUtil;


/**
 * @author Sergey Polovko
 * @author Evgeniy Pshenitsin
 */
final class YdbNameResolver extends NameResolver {

    private static final Logger logger = Logger.getLogger(YdbNameResolver.class.getName());

    private static final String SCHEME = "ydb";
    public static final Attributes.Key<String> LOCATION_ATTR = Attributes.Key.create("loc");

    private final String database;
    private final String authority;
    private final GrpcTransport transport;

    private Listener listener;
    private volatile boolean shutdown = false;

    private YdbNameResolver(String hostname, int port, String database, AuthProvider authProvider, @Nullable byte[] cert) {
        this.database = database;
        this.authority = GrpcUtil.authorityFromHostAndPort(hostname, port);
        GrpcTransport.Builder transportBuilder = GrpcTransport.forHost(hostname, port)
            .withAuthProvider(authProvider);
        if (cert != null) {
            transportBuilder.withSecureConnection(cert);
        }
        this.transport = transportBuilder.build();
    }

    static String makeTarget(String endpoint, String database) {
        StringBuilder sb = new StringBuilder();
        sb.append(SCHEME).append("://").append(endpoint);
        if (!database.startsWith("/")) {
            sb.append('/');
        }
        sb.append(database);
        return sb.toString();
    }

    @Override
    public String getServiceAuthority() {
        return authority;
    }

    @Override
    public void start(Listener listener) {
        this.listener = listener;
        resolve();
    }

    @Override
    public void refresh() {
        resolve();
    }

    @Override
    public void shutdown() {
        transport.close();
        shutdown = true;
    }

    private void resolve() {
        listEndpoints((response, status) -> {
            if (!status.isOk()) {
                listener.onError(status.augmentDescription("unable to resolve database " + database + ", network issue"));
                return;
            }

            logger.log(Level.INFO, String.format("response METHOD_LIST_ENDPOINTS - %s", response.toString()));

            try {
                Operation operation = response.getOperation();
                if (!operation.getReady()) {
                    // TODO: wait deferred operations
                    String msg = "unable to resolve database " + database +
                        ", got not ready operation, id: " + operation.getId() +
                        ", status: " + operation.getStatus();
                    listener.onError(Status.INTERNAL.withDescription(msg));
                    return;
                }

                if (operation.getStatus() != StatusCode.SUCCESS) {
                    String msg = "unable to resolve database " + database +
                        ", got non SUCCESS response, id: " + operation.getId() +
                        ", status: " + operation.getStatus();
                    listener.onError(Status.INTERNAL.withDescription(msg));
                    return;
                }

                ListEndpointsResult result = Operations.unpackResult(operation, ListEndpointsResult.class);

                /*
                     ---EXAMPLE---
                * METHOD_LIST_ENDPOINTS
                *
                * result = {DiscoveryProtos$ListEndpointsResult@9576} "endpoints {
                 bitField0_ = 0
                 endpoints_ = {Collections$UnmodifiableRandomAccessList@9597}  size = 6
                 selfLocation_ = "SAS"
                 memoizedIsInitialized = 1
                 unknownFields = {UnknownFieldSet@9420} ""
                 memoizedSize = -1
                 memoizedHashCode = 0
                 */

                int endpointsCount = result.getEndpointsCount();
                if (endpointsCount == 0) {
                    String msg = "unable to resolve database " + database + ", got empty list of endpoints";
                    listener.onError(Status.UNAVAILABLE.withDescription(msg));
                    return;
                }

                logger.info(String.format("ListEndpointsResult - %s)",
                        result.getEndpointsList().stream()
                        .map(e -> String.format("{addr - %s, loc - %s}", e.getAddress(), e.getLocation()))
                                .collect(Collectors.joining(","))));

                List<EquivalentAddressGroup> groups = new ArrayList<>(endpointsCount);
                for (int i = 0; i < endpointsCount; i++) {
                    EndpointInfo e = result.getEndpoints(i);

                    /*
                     ---EXAMPLE---
                    * e = {DiscoveryProtos$EndpointInfo@9601} "address: "ydb-eu-man-1022.search.yandex.net"\nport: 31011\nlocation: "MAN"\n"
                     bitField0_ = 0
                     address_ = "ydb-eu-man-1022.search.yandex.net"
                     port_ = 31011
                     loadFactor_ = 0.0
                     ssl_ = false
                     service_ = {LazyStringArrayList@9762}  size = 0
                     location_ = "MAN"
                     memoizedIsInitialized = -1
                     unknownFields = {UnknownFieldSet@9420} ""
                     memoizedSize = -1
                     memoizedHashCode = 0
                     */

                    try {
                        groups.add(createAddressGroup(e));
                    } catch (UnknownHostException x) {
                        String msg = "unable to resolve database " + database +
                            ", got unknown hostname: " + e.getAddress();
                        listener.onError(Status.UNAVAILABLE.withDescription(msg).withCause(x));
                        return;
                    }
                }

                listener.onAddresses(groups, Attributes.EMPTY);
            } catch (Throwable t) {
                String msg = "unable to resolve database " + database + ", unhandled exception";
                listener.onError(Status.UNAVAILABLE.withDescription(msg).withCause(t));
            }
        });
    }

    // TODO: resolve name asynchronously
    private static EquivalentAddressGroup createAddressGroup(EndpointInfo endpoint) throws UnknownHostException {
        InetAddress[] addresses = InetAddress.getAllByName(endpoint.getAddress());
        if (addresses.length == 1) {
            return new EquivalentAddressGroup(new InetSocketAddress(addresses[0], endpoint.getPort()),
                    Attributes.newBuilder().set(LOCATION_ATTR, endpoint.getLocation()).build());
        }

        List<SocketAddress> socketAddresses = new ArrayList<>(addresses.length);
        for (InetAddress address : addresses) {
            socketAddresses.add(new InetSocketAddress(address, endpoint.getPort()));
        }
        return new EquivalentAddressGroup(socketAddresses,
                Attributes.newBuilder().set(LOCATION_ATTR, endpoint.getLocation()).build());
    }

    private void listEndpoints(BiConsumer<ListEndpointsResponse, Status> consumer) {
        if (shutdown) {
            return;
        }

        ListEndpointsRequest request = ListEndpointsRequest.newBuilder()
            .setDatabase(database)
            .build();
        transport.unaryCall(DiscoveryServiceGrpc.getListEndpointsMethod(), request, consumer, 0);
    }

    static Factory newFactory(AuthProvider authProvider, @Nullable byte[] cert) {
        return new Factory() {
            @Nullable
            @Override
            public NameResolver newNameResolver(URI targetUri, Helper helper) {
                if (!SCHEME.equals(targetUri.getScheme())) {
                    return null;
                }
                int port = targetUri.getPort();
                if (port == -1) {
                    port = GrpcTransport.DEFAULT_PORT;
                }
                return new YdbNameResolver(targetUri.getHost(), port, targetUri.getPath(), authProvider, cert);
            }

            @Override
            public String getDefaultScheme() {
                return SCHEME;
            }
        };
    }
}
