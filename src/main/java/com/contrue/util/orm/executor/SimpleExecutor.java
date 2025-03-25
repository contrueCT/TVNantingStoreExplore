package com.contrue.util.orm.executor;

import com.contrue.util.orm.ResultSetMapper;
import com.contrue.util.orm.configuration.BoundSql;
import com.contrue.util.orm.configuration.Configuration;
import com.contrue.util.orm.configuration.MappedStatement;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.contrue.util.orm.session.MethodType.SELECT;

/**
 * @author confff
 */
public class SimpleExecutor implements Executor {
    @Override
    public <T> List<T> execute(Configuration config, String statementId, Object[] parameters, Connection conn) throws NoSuchFieldException, SQLException {
        MappedStatement mappedStatement = config.getMappedStatement(statementId);
        BoundSql boundSql = new BoundSql(mappedStatement);
        String sql = boundSql.getSqlText();
        //sql语句中解析出的原类参数名列表
        List<String> parameterMapperList = boundSql.getParameterMapperList();
        Class<?> parameterTypeClass = this.getClassType(mappedStatement.getParameters());
        //获取返回值类型
        Class<?> resultTypeClass = this.getClassType(mappedStatement.getResultType());
        try(PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            setParameters(preparedStatement,parameterMapperList,parameterTypeClass,parameters);
            if (SELECT.equals(mappedStatement.getActionType())) {
                return (List<T>) executeQuery (preparedStatement, resultTypeClass);
            } else {
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
                return Collections.emptyList();
            }
        }
    }

    @Override
    public int executeUpdate(Configuration config, String statementId, Object[] parameters, Connection connection) throws SQLException, NoSuchFieldException {
        MappedStatement mappedStatement = config.getMappedStatement(statementId);
        BoundSql boundSql = new BoundSql(mappedStatement);
        String sql = boundSql.getSqlText();
        //sql语句中解析出的原类参数名列表
        List<String> parameterMapperList = boundSql.getParameterMapperList();
        Class<?> parameterTypeClass = this.getClassType(mappedStatement.getParameters());
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            setParameters(preparedStatement,parameterMapperList,parameterTypeClass,parameters);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected;
        }

    }


    private <T> List<T> executeQuery(PreparedStatement preparedStatement, Class<T> resultType) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMapper resultSetMapper = new ResultSetMapper();
        return resultSetMapper.mapResultSet(resultSet, resultType);
    }

    //设置prepareStatement的参数
    private void setParameters(PreparedStatement preparedStatement, List<String> parameterMapperList, Class<?> parameterTypeClass, Object[] parameters) throws NoSuchFieldException, SQLException {
        if (parameterMapperList == null || parameterMapperList.isEmpty()) {
            return;
        }

        for (int i = 0; i < parameterMapperList.size(); i++) {
            String paramName = parameterMapperList.get(i);
            Object value = getParameterValue(parameters[0], paramName, parameterTypeClass);
            preparedStatement.setObject(i + 1, value);
        }
    }
    //获取传入参数的值
    private Object getParameterValue(Object parameter, String paramName, Class<?> parameterTypeClass) throws NoSuchFieldException {
        if (parameterTypeClass == Integer.class||parameterTypeClass == int.class||parameterTypeClass == String.class||parameterTypeClass == Float.class||parameterTypeClass == Double.class||parameterTypeClass == long.class) {
            //如果是单个值直接使用
            return parameter;
        } else if (parameterTypeClass != null) {
            //如果是实体类对象，则通过反射获取字段值
            try {
                Field field = parameterTypeClass.getDeclaredField(paramName);
                field.setAccessible(true);
                return field.get(parameter);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported parameter type");
        }
    }

    //获取配置的sql语句中的参数类型
    private Class<?> getClassType(String parameterType) {
        if (parameterType == null) {
            return null;
        }
        try {
            return Class.forName(parameterType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
