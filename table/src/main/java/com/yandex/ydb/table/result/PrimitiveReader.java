package com.yandex.ydb.table.result;

import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.yandex.ydb.table.values.DecimalValue;


/**
 * @author Sergey Polovko
 */
public interface PrimitiveReader {

    boolean getBool();

    byte getInt8();

    int getUint8();

    short getInt16();

    int getUint16();

    int getInt32();

    long getUint32();

    long getInt64();

    long getUint64();

    float getFloat32();

    double getFloat64();

    LocalDate getDate();

    LocalDateTime getDatetime();

    Instant getTimestamp();

    Duration getInterval();

    ZonedDateTime getTzDate();

    ZonedDateTime getTzDatetime();

    ZonedDateTime getTzTimestamp();

    byte[] getString();

    default String getString(Charset charset) {
        return new String(getString(), charset);
    }

    UUID getUuid();

    String getUtf8();

    byte[] getYson();

    String getJson();

    DecimalValue getDecimal();
}
