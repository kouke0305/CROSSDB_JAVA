package com.open.crossdb.jni;


import com.open.crossdb.jni.api.IDB;
import com.open.crossdb.jni.core.DBBuilder;
import com.open.crossdb.jni.core.LoadLibraryUtils;

import java.util.Properties;

class JniTestTest {


    public static void main(String[] args) {
        LoadLibraryUtils.loadLibrary("libcrossdb");
        LoadLibraryUtils.loadLibrary("crossdb");


        //createdb
        IDB db = new DBBuilder()
                .dbName("db1")
                .dbPath(":memory:")
                .properties(new Properties())
                .build();

        //createtable
        db.xdbExec("CREATE TABLE IF NOT EXISTS student (id INT PRIMARY KEY, name CHAR(16), age INT, class CHAR(16), score FLOAT, info CHAR(255))");
        //begin
        db.begin();
        //insert
        db.xdbExec("INSERT INTO student (id,name,age,class,score) VALUES (1,'jack',10,'3-1',90),(2,'tom',11,'2-5',91),(3,'jack',11,'1-6',92),(4,'rose',10,'4-2',90),(5,'tim',10,'3-1',95)");
        //commit
        db.commit();
        //select
        long resptr = db.xdbExec("SELECT id,name,age,class,score from student WHERE id = 2");
        long row = db.fetchRow(resptr);
        Integer id = db.columnInt(resptr,row,0);
        String name = db.columnStr(resptr,row,1);
        Integer age = db.columnInt(resptr,row,2);
        System.out.println("result :" + id + ":" + age + ":" + name);
        db.close();
    }

}