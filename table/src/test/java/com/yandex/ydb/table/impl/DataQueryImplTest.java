package com.yandex.ydb.table.impl;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.truth.extensions.proto.ProtoTruth;
import com.yandex.ydb.ValueProtos;
import com.yandex.ydb.ValueProtos.TypedValue;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.values.PrimitiveType;
import com.yandex.ydb.table.values.PrimitiveValue;
import com.yandex.ydb.table.values.Type;
import com.yandex.ydb.table.values.proto.ProtoType;
import com.yandex.ydb.table.values.proto.ProtoValue;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * @author Sergey Polovko
 */
public class DataQueryImplTest {

    @Test
    public void params() {
        ImmutableMap<String, Type> types = ImmutableMap.of(
            "name", PrimitiveType.utf8(),
            "age", PrimitiveType.uint8());

        ImmutableMap<String, ValueProtos.Type> typesPb = ImmutableMap.of(
            "name", PrimitiveType.utf8().toPb(),
            "age", PrimitiveType.uint8().toPb());

        Params params = new DataQueryImpl.DataQueryParams(types, typesPb)
            .put("name", PrimitiveValue.utf8("Jamel"))
            .put("age", PrimitiveValue.uint8((byte) 99));

        assertThat(params.isEmpty())
            .isFalse();

        Map<String, TypedValue> pb = params.toPb();
        assertThat(pb).isNotEmpty();

        ProtoTruth.assertThat(pb.get("name"))
            .isEqualTo(TypedValue.newBuilder()
                .setType(ProtoType.utf8())
                .setValue(ProtoValue.utf8("Jamel"))
                .build());

        ProtoTruth.assertThat(pb.get("age"))
            .isEqualTo(TypedValue.newBuilder()
                .setType(ProtoType.uint8())
                .setValue(ProtoValue.uint8((byte) 99))
                .build());

        // duplicate parameter
        try {
            params.put("name", PrimitiveValue.utf8("Another Name"));
            fail("expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("duplicate parameter: name", e.getMessage());
        }

        // wrong type
        try {
            params.put("name", PrimitiveValue.uint32(1));
            fail("expected exception was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("types mismatch: expected Utf8, got Uint32", e.getMessage());
        }
    }
}
