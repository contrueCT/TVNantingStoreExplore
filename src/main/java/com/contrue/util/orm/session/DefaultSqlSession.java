package com.contrue.util.orm.session;

import com.contrue.util.SystemLogger;
import com.contrue.util.orm.configuration.Configuration;
import com.contrue.util.orm.configuration.MappedStatement;
import com.contrue.util.orm.executor.Executor;
import com.contrue.util.orm.executor.SimpleExecutor;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.contrue.util.orm.session.MethodType.SELECT;

/**
 * @author confff
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getMapper(Class<T> mapperClazz, Connection conn) throws SQLException {
        return (T) Proxy.newProxyInstance(mapperClazz.getClassLoader(),new Class[]{mapperClazz},new InvocationHandler(){
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws SQLException, NoSuchFieldException {
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." + methodName;

                MappedStatement mappedStatement = configuration.getMappedStatement(statementId);
                if(mappedStatement == null) {
                    SystemLogger.logError("没有找到这个类方法"+statementId,new RuntimeException());
                    throw new RuntimeException("没有找到这个类方法"+statementId);
                }
                Executor executor = new SimpleExecutor();


                if (SELECT.equals(mappedStatement.getActionType())) {

                    return (List<T>) executor.execute(configuration,statementId,args,conn);
                } else {
                    return executor.executeUpdate(configuration,statementId,args,conn);
                }
            }
        });
    }

}
