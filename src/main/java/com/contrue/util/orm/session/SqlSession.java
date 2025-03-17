package com.contrue.util.orm.session;

import java.sql.SQLException;
import java.util.List;

public interface SqlSession {
    /**
     * 查询所有对象
     * @param statementId sql语句的id
     * @param params 筛选参数
     * @return
     * @param <T>
     */
    <T> List<T> select(String statementId, Object ... params) throws SQLException;

    <T> T selectOne(String statementId, Object ... params) throws SQLException;

    int insert(String statementId, Object ... params) throws SQLException;

    int update(String statementId, Object ... params) throws SQLException;

    <T> T getMapper(Class<T> clazz)throws SQLException;

    int delete(String statementId, Object ... params) throws SQLException;

}
