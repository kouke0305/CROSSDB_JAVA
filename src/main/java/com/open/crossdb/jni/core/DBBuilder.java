package com.open.crossdb.jni.core;

import java.util.Properties;

public class DBBuilder {
    private String dbName;
    private String dbPath;
    private String dbType;
    private Properties properties = new Properties();

    public DBBuilder dbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public DBBuilder dbPath(String dbPath) {
        this.dbPath = dbPath;
        return this;
    }

    public DBBuilder dbType(String dbType) {
        this.dbType = dbType;
        return this;
    }

    public DBBuilder properties(Properties properties) {
        this.properties = properties;
        return this;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbPath() {
        return dbPath;
    }

    public Properties getProperties() {
        return properties;
    }

    public CrossDB build() {
        return new CrossDB(this);
    }
}
