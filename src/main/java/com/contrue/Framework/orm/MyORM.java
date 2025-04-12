package com.contrue.Framework.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * @author confff
 */
public interface MyORM {
    /**
     * 通过任意被标记为表的类对象插入数据
     * @param conn  数据库连接
     * @param po    实体类
     * @return      是否插入成功
     * @param <T>   泛型
     */
    <T> boolean insert(Connection conn, T po) throws SQLException;

    /**
     * 通过类对象修改数据（只能有一个非空字段）
     * @param conn  数据库连接
     * @param po    实体类
     * @return      是否插入成功
     * @param <T>   泛型
     */
    <T> boolean update(Connection conn, T po) throws SQLException;

    /**
     * 通过类对象的id删除数据（id必须存在）
     * @param conn  数据库连接
     * @param po    实体类
     * @return      是否插入成功
     * @param <T>   泛型
     */
    <T> boolean delete(Connection conn, T po) throws SQLException;

    /**
     * 通过类对象查询（只能有一个非空字段）
     * @param conn  数据库连接
     * @param po    实体类
     * @return      是否插入成功
     * @param <T>   泛型
     */
    <T> List<T> select(Connection conn, T po, SelectMethod method) throws SQLException;

    <T> List<T> mapResultToEntity(ResultSet re,T po) throws SQLException;
}
