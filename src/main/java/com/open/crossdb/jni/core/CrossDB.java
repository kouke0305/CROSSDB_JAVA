package com.open.crossdb.jni.core;

import com.open.crossdb.jni.api.IDB;


public class CrossDB implements IDB {

    private String dbName;

    private long dbPointer;

    public CrossDB(DBBuilder builder) {
        dbName = builder.getDbName();
        this.dbPointer = CrossDBJNI.jniOpenDB(builder.getDbPath());
    }

    @Override
    public long xdbExec(String sqlStr) {
        return CrossDBJNI.jniXdbExec(dbPointer, sqlStr);
    }

    @Override
    public long fetchRow(long resPtr) {
        return CrossDBJNI.jniFetchRow(resPtr);
    }

    @Override
    public Integer columnInt(long resPtr, long rowPtr, int num) {
        return CrossDBJNI.jniColumnInt(resPtr, rowPtr, num);
    }

    @Override
    public String columnStr(long resPtr, long rowPtr, int num) {
        return CrossDBJNI.jniColumnStr(resPtr, rowPtr, num);
    }

    @Override
    public Float columnfloat(long resPtr, long rowPtr, int num) {
        return CrossDBJNI.jniColumnFloat(resPtr, rowPtr, num);
    }

    @Override
    public <T> T select(String sqlStr) {
        long resPtr = CrossDBJNI.jniXdbExec(dbPointer, sqlStr);
        //TODO
        while (CrossDBJNI.jniFetchRow(resPtr) != 0L){

        }

        return null;
    }

    @Override
    public int begin() {
        return CrossDBJNI.jniBegin(dbPointer);
    }

    @Override
    public int commit() {
        return CrossDBJNI.jniCommit(dbPointer);
    }

    @Override
    public int rollback() {
        return CrossDBJNI.jniRollback(dbPointer);
    }

    @Override
    public int close() {
        return CrossDBJNI.jniClose(dbPointer);
    }

    long getDbPointer() {
        return dbPointer;
    }

}
