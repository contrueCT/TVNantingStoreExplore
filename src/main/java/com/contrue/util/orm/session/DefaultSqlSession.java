package com.contrue.util.orm.session;

import com.contrue.util.orm.configuration.Configuration;
import com.contrue.util.orm.executor.Executor;
import com.contrue.util.orm.executor.SimpleExecutor;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {}

    @Override
    public <T> List<T> select(String statementId, Object param) throws SQLException {
        return Collections.emptyList();
    }

    @Override
    public <T> T selectOne(String statementId, Object param) throws SQLException {
        return null;
    }

    @Override
    public int insert(String statementId, Object param) throws SQLException {
        return 0;
    }

    @Override
    public int update(String statementId, Object param) throws SQLException {
        return 0;
    }

    @Override
    public <T> T getMapper(Class<T> mapperClazz, Connection conn) throws SQLException {
        return (T) Proxy.newProxyInstance(mapperClazz.getClassLoader(),new Class[]{mapperClazz},new InvocationHandler(){
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws SQLException, NoSuchFieldException {
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." + methodName;

                Executor executor = new SimpleExecutor();
                executor.execute(configuration,statementId,args,conn);
                return "";
            }
        });
    }

    @Override
    public int delete(String statementId, Object param) throws SQLException {
        return 0;
    }
}
