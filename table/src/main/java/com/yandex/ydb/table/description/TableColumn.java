package com.yandex.ydb.table.description;

import com.yandex.ydb.table.values.Type;


/**
 * @author Sergey Polovko
 */
public class TableColumn {

    private final String name;
    private final Type type;

    public TableColumn(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + ' ' + type;
    }
}
