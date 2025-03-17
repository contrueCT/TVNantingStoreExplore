package com.contrue.util;

import java.sql.Connection;


/**
 * @author confff
 */
public class MyDBConnection {
    private MyDBConnection() {
    }

    private static volatile MyConnectionPool DataSource;

    public static Connection getConnection() {
        try{
            if (DataSource == null) {
                synchronized (MyDBConnection.class) {
                    if (DataSource == null) {
                        DataSource = MyConnectionPool.getDataSource();
                    }
                }
            }
            return DataSource.getConnection();
        }catch (Exception e) {
            SystemLogger.logError("连接获取失败",e);
            throw new RuntimeException(e);
        }

    }
}
