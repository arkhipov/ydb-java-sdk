package com.yandex.ydb.table.result.impl;

import com.yandex.ydb.ValueProtos;
import com.yandex.ydb.table.values.proto.ProtoType;


/**
 * @author Sergey Polovko
 */
final class ProtoVoidValueReader extends AbstractValueReader {
    private ProtoVoidValueReader() {}

    static final ProtoVoidValueReader INSTANCE = new ProtoVoidValueReader();

    @Override
    protected ValueProtos.Type getType() {
        return ProtoType.voidType();
    }

    @Override
    protected void setValue(ValueProtos.Value value) {
        // skip
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append("Void");
    }
}
