package com.open.crossdb.jni.core;


public class CrossDBJNI {

    static native long jniOpenDB(String dbPath);

    static native int jniBegin(long dbPtr);

    static native int jniCommit(long dbPtr);

    static native int jniRollback(long dbPtr);

    static native long jniXdbExec(long dbPtr, String sqlStr);

    static native long jniFetchRow(long resPtr);

    static native int jniColumnInt(long resPtr, long rowPtr, int colNum);
    static native String jniColumnStr(long resPtr, long rowPtr, int colNum);
    static native float jniColumnFloat(long resPtr, long rowPtr, int colNum);

    static native int jniClose(long dbPtr);
}
