// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: kikimr/public/api/protos/ydb_rate_limiter.proto

package com.yandex.ydb.rate_limiter;

/**
 * Protobuf type {@code Ydb.RateLimiter.ListResourcesResult}
 */
public  final class ListResourcesResult extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:Ydb.RateLimiter.ListResourcesResult)
    ListResourcesResultOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ListResourcesResult.newBuilder() to construct.
  private ListResourcesResult(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ListResourcesResult() {
    resourcePaths_ = com.google.protobuf.LazyStringArrayList.EMPTY;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ListResourcesResult(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();
            if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
              resourcePaths_ = new com.google.protobuf.LazyStringArrayList();
              mutable_bitField0_ |= 0x00000001;
            }
            resourcePaths_.add(s);
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
        resourcePaths_ = resourcePaths_.getUnmodifiableView();
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.yandex.ydb.rate_limiter.RateLimiterProtos.internal_static_Ydb_RateLimiter_ListResourcesResult_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.yandex.ydb.rate_limiter.RateLimiterProtos.internal_static_Ydb_RateLimiter_ListResourcesResult_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.yandex.ydb.rate_limiter.ListResourcesResult.class, com.yandex.ydb.rate_limiter.ListResourcesResult.Builder.class);
  }

  public static final int RESOURCE_PATHS_FIELD_NUMBER = 1;
  private com.google.protobuf.LazyStringList resourcePaths_;
  /**
   * <code>repeated string resource_paths = 1;</code>
   */
  public com.google.protobuf.ProtocolStringList
      getResourcePathsList() {
    return resourcePaths_;
  }
  /**
   * <code>repeated string resource_paths = 1;</code>
   */
  public int getResourcePathsCount() {
    return resourcePaths_.size();
  }
  /**
   * <code>repeated string resource_paths = 1;</code>
   */
  public java.lang.String getResourcePaths(int index) {
    return resourcePaths_.get(index);
  }
  /**
   * <code>repeated string resource_paths = 1;</code>
   */
  public com.google.protobuf.ByteString
      getResourcePathsBytes(int index) {
    return resourcePaths_.getByteString(index);
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < resourcePaths_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, resourcePaths_.getRaw(i));
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    {
      int dataSize = 0;
      for (int i = 0; i < resourcePaths_.size(); i++) {
        dataSize += computeStringSizeNoTag(resourcePaths_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getResourcePathsList().size();
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.yandex.ydb.rate_limiter.ListResourcesResult)) {
      return super.equals(obj);
    }
    com.yandex.ydb.rate_limiter.ListResourcesResult other = (com.yandex.ydb.rate_limiter.ListResourcesResult) obj;

    boolean result = true;
    result = result && getResourcePathsList()
        .equals(other.getResourcePathsList());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getResourcePathsCount() > 0) {
      hash = (37 * hash) + RESOURCE_PATHS_FIELD_NUMBER;
      hash = (53 * hash) + getResourcePathsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.yandex.ydb.rate_limiter.ListResourcesResult parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.yandex.ydb.rate_limiter.ListResourcesResult prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code Ydb.RateLimiter.ListResourcesResult}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:Ydb.RateLimiter.ListResourcesResult)
      com.yandex.ydb.rate_limiter.ListResourcesResultOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.yandex.ydb.rate_limiter.RateLimiterProtos.internal_static_Ydb_RateLimiter_ListResourcesResult_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.yandex.ydb.rate_limiter.RateLimiterProtos.internal_static_Ydb_RateLimiter_ListResourcesResult_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.yandex.ydb.rate_limiter.ListResourcesResult.class, com.yandex.ydb.rate_limiter.ListResourcesResult.Builder.class);
    }

    // Construct using com.yandex.ydb.rate_limiter.ListResourcesResult.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      resourcePaths_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.yandex.ydb.rate_limiter.RateLimiterProtos.internal_static_Ydb_RateLimiter_ListResourcesResult_descriptor;
    }

    public com.yandex.ydb.rate_limiter.ListResourcesResult getDefaultInstanceForType() {
      return com.yandex.ydb.rate_limiter.ListResourcesResult.getDefaultInstance();
    }

    public com.yandex.ydb.rate_limiter.ListResourcesResult build() {
      com.yandex.ydb.rate_limiter.ListResourcesResult result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.yandex.ydb.rate_limiter.ListResourcesResult buildPartial() {
      com.yandex.ydb.rate_limiter.ListResourcesResult result = new com.yandex.ydb.rate_limiter.ListResourcesResult(this);
      int from_bitField0_ = bitField0_;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        resourcePaths_ = resourcePaths_.getUnmodifiableView();
        bitField0_ = (bitField0_ & ~0x00000001);
      }
      result.resourcePaths_ = resourcePaths_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.yandex.ydb.rate_limiter.ListResourcesResult) {
        return mergeFrom((com.yandex.ydb.rate_limiter.ListResourcesResult)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.yandex.ydb.rate_limiter.ListResourcesResult other) {
      if (other == com.yandex.ydb.rate_limiter.ListResourcesResult.getDefaultInstance()) return this;
      if (!other.resourcePaths_.isEmpty()) {
        if (resourcePaths_.isEmpty()) {
          resourcePaths_ = other.resourcePaths_;
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          ensureResourcePathsIsMutable();
          resourcePaths_.addAll(other.resourcePaths_);
        }
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.yandex.ydb.rate_limiter.ListResourcesResult parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.yandex.ydb.rate_limiter.ListResourcesResult) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private com.google.protobuf.LazyStringList resourcePaths_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    private void ensureResourcePathsIsMutable() {
      if (!((bitField0_ & 0x00000001) == 0x00000001)) {
        resourcePaths_ = new com.google.protobuf.LazyStringArrayList(resourcePaths_);
        bitField0_ |= 0x00000001;
       }
    }
    /**
     * <code>repeated string resource_paths = 1;</code>
     */
    public com.google.protobuf.ProtocolStringList
        getResourcePathsList() {
      return resourcePaths_.getUnmodifiableView();
    }
    /**
     * <code>repeated string resource_paths = 1;</code>
     */
    public int getResourcePathsCount() {
      return resourcePaths_.size();
    }
    /**
     * <code>repeated string resource_paths = 1;</code>
     */
    public java.lang.String getResourcePaths(int index) {
      return resourcePaths_.get(index);
    }
    /**
     * <code>repeated string resource_paths = 1;</code>
     */
    public com.google.protobuf.ByteString
        getResourcePathsBytes(int index) {
      return resourcePaths_.getByteString(index);
    }
    /**
     * <code>repeated string resource_paths = 1;</code>
     */
    public Builder setResourcePaths(
        int index, java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureResourcePathsIsMutable();
      resourcePaths_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string resource_paths = 1;</code>
     */
    public Builder addResourcePaths(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureResourcePathsIsMutable();
      resourcePaths_.add(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string resource_paths = 1;</code>
     */
    public Builder addAllResourcePaths(
        java.lang.Iterable<java.lang.String> values) {
      ensureResourcePathsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, resourcePaths_);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string resource_paths = 1;</code>
     */
    public Builder clearResourcePaths() {
      resourcePaths_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string resource_paths = 1;</code>
     */
    public Builder addResourcePathsBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      ensureResourcePathsIsMutable();
      resourcePaths_.add(value);
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:Ydb.RateLimiter.ListResourcesResult)
  }

  // @@protoc_insertion_point(class_scope:Ydb.RateLimiter.ListResourcesResult)
  private static final com.yandex.ydb.rate_limiter.ListResourcesResult DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.yandex.ydb.rate_limiter.ListResourcesResult();
  }

  public static com.yandex.ydb.rate_limiter.ListResourcesResult getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ListResourcesResult>
      PARSER = new com.google.protobuf.AbstractParser<ListResourcesResult>() {
    public ListResourcesResult parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new ListResourcesResult(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ListResourcesResult> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ListResourcesResult> getParserForType() {
    return PARSER;
  }

  public com.yandex.ydb.rate_limiter.ListResourcesResult getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

