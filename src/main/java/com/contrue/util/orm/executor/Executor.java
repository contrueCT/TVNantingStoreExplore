package com.contrue.util.orm.executor;

import com.contrue.util.orm.configuration.Configuration;
import com.contrue.util.orm.configuration.MappedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Executor {
    /**
     * sql执行器
     * @param config 配置参数
     * @param mappedStatement sql语句
     * @param parameters 参数
     * @return
     * @param <T>
     */
    <T> List<T> execute(Configuration config, String statementId, Object[] parameters, Connection connection) throws SQLException, NoSuchFieldException;
}
