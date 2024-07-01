package com.td.ca.base.util.io.codegenarator.sql;

public enum IndexType {
    NORMAL(0), UNIQUE(1);

    public int value;

    private IndexType(int value) {
        this.value = value;
    }

    public static IndexType getIndexType(int value) {
        for (IndexType type : IndexType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return IndexType.NORMAL;
    }
}
