package com.contrue.Framework.orm.session;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author confff
 */
public interface SqlSession {
    //动态生成代理对象
    <T> T getMapper(Class<T> clazz, Connection conn)throws SQLException;


}
