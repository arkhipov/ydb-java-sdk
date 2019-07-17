package com.yandex.ydb.table.impl;

import java.util.concurrent.CompletableFuture;

import com.yandex.ydb.core.Result;
import com.yandex.ydb.core.Status;
import com.yandex.ydb.scheme.SchemeOperationProtos;
import com.yandex.ydb.scheme.SchemeOperationProtos.DescribePathRequest;
import com.yandex.ydb.scheme.SchemeOperationProtos.ListDirectoryRequest;
import com.yandex.ydb.scheme.SchemeOperationProtos.MakeDirectoryRequest;
import com.yandex.ydb.scheme.SchemeOperationProtos.RemoveDirectoryRequest;
import com.yandex.ydb.table.SchemeClient;
import com.yandex.ydb.table.description.DescribePathResult;
import com.yandex.ydb.table.description.ListDirectoryResult;
import com.yandex.ydb.table.rpc.SchemeRpc;


/**
 * @author Sergey Polovko
 */
final class SchemeClientImpl implements SchemeClient {

    private final SchemeRpc schemeRpc;

    SchemeClientImpl(SchemeClientBuilderImpl builder) {
        this.schemeRpc = builder.schemeRpc;
    }

    @Override
    public CompletableFuture<Status> makeDirectory(String path) {
        MakeDirectoryRequest request = MakeDirectoryRequest.newBuilder()
            .setPath(path)
            .build();
        return schemeRpc.makeDirectory(request)
            .thenCompose(response -> {
                if (!response.isSuccess()) {
                    return CompletableFuture.completedFuture(response.toStatus());
                }
                return schemeRpc.getOperationTray()
                    .waitStatus(response.expect("makeDirectory()").getOperation());
            });
    }

    @Override
    public CompletableFuture<Status> removeDirectory(String path) {
        RemoveDirectoryRequest request = RemoveDirectoryRequest.newBuilder()
            .setPath(path)
            .build();
        return schemeRpc.removeDirectory(request)
            .thenCompose(response -> {
                if (!response.isSuccess()) {
                    return CompletableFuture.completedFuture(response.toStatus());
                }
                return schemeRpc.getOperationTray()
                    .waitStatus(response.expect("removeDirectory()").getOperation());
            });
    }

    @Override
    public CompletableFuture<Result<DescribePathResult>> describePath(String path) {
        DescribePathRequest request = DescribePathRequest.newBuilder()
            .setPath(path)
            .build();
        return schemeRpc.describePath(request)
            .thenCompose(response -> {
                if (!response.isSuccess()) {
                    return CompletableFuture.completedFuture(response.cast());
                }
                return schemeRpc.getOperationTray().waitResult(
                    response.expect("describePath()").getOperation(),
                    SchemeOperationProtos.DescribePathResult.class,
                    result -> new DescribePathResult(result.getSelf()));
            });
    }

    @Override
    public CompletableFuture<Result<ListDirectoryResult>> listDirectory(String path) {
        ListDirectoryRequest request = ListDirectoryRequest.newBuilder()
            .setPath(path)
            .build();
        return schemeRpc.describeDirectory(request)
            .thenCompose(response -> {
                if (!response.isSuccess()) {
                    return CompletableFuture.completedFuture(response.cast());
                }
                return schemeRpc.getOperationTray().waitResult(
                    response.expect("describeDirectory()").getOperation(),
                    SchemeOperationProtos.ListDirectoryResult.class,
                    result -> new ListDirectoryResult(result.getSelf(), result.getChildrenList()));
            });
    }

    @Override
    public void close() {
        schemeRpc.close();
    }
}