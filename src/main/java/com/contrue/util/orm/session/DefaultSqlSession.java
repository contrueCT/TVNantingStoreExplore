package com.contrue.util.orm.session;

import com.contrue.util.orm.configuration.Configuration;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {}

    @Override
    public <T> List<T> select(String statementId, Object... params) throws SQLException {
        return Collections.emptyList();
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws SQLException {
        return null;
    }

    @Override
    public int insert(String statementId, Object... params) throws SQLException {
        return 0;
    }

    @Override
    public int update(String statementId, Object... params) throws SQLException {
        return 0;
    }

    @Override
    public <T> T getMapper(Class<T> mapperClazz) throws SQLException {
        Proxy.newProxyInstance(mapperClazz.getClassLoader(),new Class[]{mapperClazz},new InvocationHandler(){
            @Override
            public Object invoke(Object proxy, Method method, Object[] args)throws Throwable{
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." + methodName;
                Type genericReturnType = method.getGenericReturnType();

                if(methodName.contains(MethodType.INSERT.toString())){
                    return insert(statementId, args);
                }else if(methodName.contains(MethodType.UPDATE.toString())){
                    return update(statementId, args);
                }else if(methodName.contains(MethodType.DELETE.toString())){
                    return delete(statementId, args);
                }

                if (genericReturnType instanceof ParameterizedType) {
                    return select(statementId, args);
                }else{
                    return selectOne(statementId, args);
                }

            }
        });
        return null;
    }

    @Override
    public int delete(String statementId, Object... params) throws SQLException {
        return 0;
    }
}
