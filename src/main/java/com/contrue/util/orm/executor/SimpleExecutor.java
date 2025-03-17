package com.contrue.util.orm.executor;

import com.contrue.util.orm.configuration.BoundSql;
import com.contrue.util.orm.configuration.Configuration;
import com.contrue.util.orm.configuration.MappedStatement;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class SimpleExecutor implements Executor {
    @Override
    public <T> List<T> execute(Configuration config, MappedStatement mappedStatement, Object[] parameters, Connection conn) throws NoSuchFieldException, SQLException {
        BoundSql boundSql = new BoundSql(mappedStatement);
        String sql = boundSql.getSqlText();
        //sql语句解析出的参数列表
        List<String> parameterMapperList = boundSql.getParameterMapperList();
        Class<?> parameterTypeClass = this.getClassType(mappedStatement.getParameters());
        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        try {
            for(int i = 0;i<parameterMapperList.size();i++){
                String content = parameterMapperList.get(i);
                Field field = parameterTypeClass.getDeclaredField(content);
                field.setAccessible(true);
                Object data = field.get(parameters[0]);
                preparedStatement.setObject(i+1, data);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }



        return Collections.emptyList();
    }

    private Class<?> getClassType(String parameterType) {
        if (parameterType == null || parameterType.length() == 0) {
            return null;
        }

        try {
            return Class.forName(parameterType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
