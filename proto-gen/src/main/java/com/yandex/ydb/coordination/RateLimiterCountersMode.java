// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: kikimr/public/api/protos/ydb_coordination.proto

package com.yandex.ydb.coordination;

/**
 * <pre>
 **
 * Counters mode
 * </pre>
 *
 * Protobuf enum {@code Ydb.Coordination.RateLimiterCountersMode}
 */
public enum RateLimiterCountersMode
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <pre>
   * The default or current value
   * </pre>
   *
   * <code>RATE_LIMITER_COUNTERS_MODE_UNSET = 0;</code>
   */
  RATE_LIMITER_COUNTERS_MODE_UNSET(0),
  /**
   * <pre>
   * Aggregated counters for resource tree
   * </pre>
   *
   * <code>RATE_LIMITER_COUNTERS_MODE_AGGREGATED = 1;</code>
   */
  RATE_LIMITER_COUNTERS_MODE_AGGREGATED(1),
  /**
   * <pre>
   * Counters on every resource
   * </pre>
   *
   * <code>RATE_LIMITER_COUNTERS_MODE_DETAILED = 2;</code>
   */
  RATE_LIMITER_COUNTERS_MODE_DETAILED(2),
  UNRECOGNIZED(-1),
  ;

  /**
   * <pre>
   * The default or current value
   * </pre>
   *
   * <code>RATE_LIMITER_COUNTERS_MODE_UNSET = 0;</code>
   */
  public static final int RATE_LIMITER_COUNTERS_MODE_UNSET_VALUE = 0;
  /**
   * <pre>
   * Aggregated counters for resource tree
   * </pre>
   *
   * <code>RATE_LIMITER_COUNTERS_MODE_AGGREGATED = 1;</code>
   */
  public static final int RATE_LIMITER_COUNTERS_MODE_AGGREGATED_VALUE = 1;
  /**
   * <pre>
   * Counters on every resource
   * </pre>
   *
   * <code>RATE_LIMITER_COUNTERS_MODE_DETAILED = 2;</code>
   */
  public static final int RATE_LIMITER_COUNTERS_MODE_DETAILED_VALUE = 2;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static RateLimiterCountersMode valueOf(int value) {
    return forNumber(value);
  }

  public static RateLimiterCountersMode forNumber(int value) {
    switch (value) {
      case 0: return RATE_LIMITER_COUNTERS_MODE_UNSET;
      case 1: return RATE_LIMITER_COUNTERS_MODE_AGGREGATED;
      case 2: return RATE_LIMITER_COUNTERS_MODE_DETAILED;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<RateLimiterCountersMode>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      RateLimiterCountersMode> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<RateLimiterCountersMode>() {
          public RateLimiterCountersMode findValueByNumber(int number) {
            return RateLimiterCountersMode.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return com.yandex.ydb.coordination.CoordinationProtos.getDescriptor().getEnumTypes().get(1);
  }

  private static final RateLimiterCountersMode[] VALUES = values();

  public static RateLimiterCountersMode valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private RateLimiterCountersMode(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:Ydb.Coordination.RateLimiterCountersMode)
}

