package com.contrue.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author confff
 */
public class MyConnectionPool {
    private static MyConnectionPool DataSource;
    private BlockingQueue<Connection> connectionPool;
    private String url;
    private String user;
    private String password;
    private int initialSize;

    private MyConnectionPool() {
    }

    public static synchronized MyConnectionPool getDataSource() {

        if (DataSource == null) {
            DataSource = new MyConnectionPool();

            try {
                DataSource.initFromConfig("db.properties");
            } catch (IOException e) {
                SystemLogger.logError("初始化连接池时，文件读取失败，初始化失败",e);
                throw new RuntimeException(e);
            } catch (SQLException e) {
                SystemLogger.logError("初始化连接池时，连接创建失败，初始化失败",e);
                throw new RuntimeException(e);
            }
        }
        return DataSource;
    }

    public void initFromConfig(String configFilePath) throws IOException, SQLException {
        Properties prop = new Properties();

        try(InputStream input = MyConnectionPool
                .class.getClassLoader()
                .getResourceAsStream(configFilePath)) {
            if (input == null) {
                throw new FileNotFoundException(configFilePath);
            }
            prop.load(input);
            this.url = prop.getProperty("url");
            this.user = prop.getProperty("username");
            this.password = prop.getProperty("password");
            this.initialSize = Integer.parseInt(prop.getProperty("initialSize","5"));

            connectionPool = new LinkedBlockingQueue<>();
            for(int i = 0;i<this.initialSize;i++){
                Connection conn = DataSource.createConnection();
                connectionPool.add(conn);
            }
        }
    }

    //初始化时生成连接
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

    //获取连接，通过instance
    public synchronized Connection getConnection() throws SQLException, InterruptedException {
        if(connectionPool.isEmpty()){
            throw new SQLException("已无空闲连接池，获取失败");
        }
        Connection conn = connectionPool.take();
        return this.createProxyConnection(conn,this);
    }

    //释放连接
    public void releaseConnection(Connection conn) {
        if (conn != null) {
            try{
                if(!conn.isClosed()){
                    connectionPool.add(conn);
                }
            } catch (SQLException e) {
                SystemLogger.logError("连接回收失败",e);
                throw new RuntimeException(e);
            }
        }
    }

    //关闭连接池
    public void closeAll() {
        for(Connection conn:connectionPool){
            try{
                if(!conn.isClosed()){
                    realCloseConnection(conn);
                }
            }catch(SQLException e){
                SystemLogger.logError("关闭连接失败",e);
                System.out.println("关闭连接失败");
            }
        }
    }

    //拦截close
    private Connection createProxyConnection(Connection conn, MyConnectionPool pool) {
        return (Connection) Proxy.newProxyInstance(
                conn.getClass().getClassLoader(),
                new Class<?>[] { Connection.class },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("close".equals(method.getName())) {
                            // 拦截 close 方法
                            pool.releaseConnection(conn);
                            return null;
                        }
                        // 其他方法
                        return method.invoke(conn, args);
                    }
                }
        );
    }

    private Connection getRealConnection(Connection conn) {
        if (Proxy.isProxyClass(conn.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(conn);
            try {
                Field delegateField = handler.getClass().getDeclaredField("delegate");
                delegateField.setAccessible(true);
                return (Connection) delegateField.get(handler);
            } catch (Exception e) {
                SystemLogger.logError(e.getMessage(),e);
            }
        }
        return conn;
    }

    //同步锁确保不会关两次
    private synchronized void realCloseConnection(Connection conn) throws SQLException {
        Connection realConn = getRealConnection(conn);
        if (realConn != null && !realConn.isClosed()) {
            realConn.close();
        }
    }
}
