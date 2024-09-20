package com.open.crossdb.jni.core;

public enum DbType {
    MEMORY("memory"),
    DISK("disk")
    ;


    DbType(String desc) {
        this.desc = desc;
    }

    private String desc;

    public String getDesc() {
        return desc;
    }
}
