// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: kikimr/public/api/protos/ydb_rate_limiter.proto

package com.yandex.ydb.rate_limiter;

public interface DescribeResourceResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Ydb.RateLimiter.DescribeResourceResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Holds DescribeResourceResult in case of successful call.
   * </pre>
   *
   * <code>.Ydb.Operations.Operation operation = 1;</code>
   */
  boolean hasOperation();
  /**
   * <pre>
   * Holds DescribeResourceResult in case of successful call.
   * </pre>
   *
   * <code>.Ydb.Operations.Operation operation = 1;</code>
   */
  com.yandex.ydb.OperationProtos.Operation getOperation();
  /**
   * <pre>
   * Holds DescribeResourceResult in case of successful call.
   * </pre>
   *
   * <code>.Ydb.Operations.Operation operation = 1;</code>
   */
  com.yandex.ydb.OperationProtos.OperationOrBuilder getOperationOrBuilder();
}
