package com.contrue.util.orm.session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface SqlSession {
    /**
     * 查询所有对象
     * @param statementId sql语句的id
     * @param param 筛选参数
     * @return
     * @param <T>
     */
    <T> List<T> select(String statementId, Object param) throws SQLException;

    <T> T selectOne(String statementId, Object param) throws SQLException;

    int insert(String statementId, Object param) throws SQLException;

    int update(String statementId, Object param) throws SQLException;

    <T> T getMapper(Class<T> clazz, Connection conn)throws SQLException;

    int delete(String statementId, Object param) throws SQLException;

}
