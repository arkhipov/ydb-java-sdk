// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: kikimr/public/api/protos/ydb_coordination.proto

package com.yandex.ydb.coordination;

public interface DropNodeRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Ydb.Coordination.DropNodeRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string path = 1;</code>
   */
  java.lang.String getPath();
  /**
   * <code>string path = 1;</code>
   */
  com.google.protobuf.ByteString
      getPathBytes();

  /**
   * <code>.Ydb.Operations.OperationParams operation_params = 2;</code>
   */
  boolean hasOperationParams();
  /**
   * <code>.Ydb.Operations.OperationParams operation_params = 2;</code>
   */
  com.yandex.ydb.OperationProtos.OperationParams getOperationParams();
  /**
   * <code>.Ydb.Operations.OperationParams operation_params = 2;</code>
   */
  com.yandex.ydb.OperationProtos.OperationParamsOrBuilder getOperationParamsOrBuilder();
}
