package com.open.crossdb.jni.api;

public interface IDB {

    long xdbExec(String sqlStr);

    long fetchRow(long resPtr);

    Integer columnInt(long resPtr,long rowPtr, int num);

    String columnStr(long resPtr,long rowPtr, int num);

    Float columnfloat(long resPtr,long rowPtr, int num);

    <T> T select(String sqlStr);

    int begin();

    int commit();

    int rollback();

    int close();

}
