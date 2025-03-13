package com.contrue.NantingStoreExplore.util.orm;

import com.contrue.NantingStoreExplore.annotation.Column;
import com.contrue.NantingStoreExplore.annotation.Table;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author confff
 */
public class MyORMImpl implements MyORM {
    @Override
    public <T> boolean insert(Connection conn, T po) throws SQLException {
        if(!po.getClass().isAnnotationPresent(Table.class)){
            throw new SQLException("此类没有被标记为表");
        }
        String tableName = po.getClass().getAnnotation(Table.class).name();
        List<String> columns = new ArrayList<String>();
        List<Object> values = new ArrayList<>();

        //找列和值
        for(Field field : po.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(Column.class)){
                field.setAccessible(true);
                try{
                    if(field.get(po)!=null&&field.get(po)!="id"){
                        columns.add(field.getAnnotation(Column.class).name());
                        values.add(field.get(po));
                    }
                }catch(IllegalAccessException e){
                    throw new SQLException("获取字段值错误");
                }
            }
        }

        if(columns.isEmpty()){
            throw new SQLException("没有被标记的字段或所以字段为空");
        }

        //开始拼接sql
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(tableName);
        sql.append("(");
        //拼接参数？
        for(int i = 0; i < columns.size(); i++){
            sql.append(i==0? columns.get(i) :","+columns.get(i));
        }
        sql.append(")");
        sql.append(" values(");
        for(int i = 0; i < values.size(); i++){
            sql.append(i==0?"?":",?");
        }
        sql.append(")：");

        //执行
        int result = 0;
        try(PreparedStatement ps = conn.prepareStatement(sql.toString())){
            for(int i = 0; i < columns.size(); i++){
                ps.setObject(i+1, values.get(i));
                result = ps.executeUpdate();
            }
        }

        return result == 1;
    }

    @Override
    public <T> boolean update(Connection conn, T po) throws SQLException {
        if(!po.getClass().isAnnotationPresent(Table.class)){
            throw new SQLException("此类没有被标记为表");
        }
        String tableName = po.getClass().getAnnotation(Table.class).name();
        List<String> columns = new ArrayList<String>();
        List<Object> values = new ArrayList<>();
        Integer id = null;

        //找列和值
        for(Field field : po.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(Column.class)){
                field.setAccessible(true);
                try{
                    if("id".equals(field.get(po))){
                        id = (Integer) field.get(po);
                    }else if(field.get(po)!=null&&field.get(po)!="id"){
                        columns.add(field.getAnnotation(Column.class).name());
                        values.add(field.get(po));
                    }
                }catch(IllegalAccessException e){
                    throw new SQLException("获取字段值错误");
                }
            }
        }

        if(columns.isEmpty()){
            throw new SQLException("没有被标记的字段或所有字段为空");
        }
        if(id==null){
            throw new SQLException("没有找到id");
        }

        //开始拼接sql
        StringBuilder sql = new StringBuilder();
        sql.append("update ");
        sql.append(tableName);
        sql.append(" set");
        //拼接参数？
        for(int i = 0; i < columns.size(); i++){
            sql.append(i==0?" ":" ,").append(" "+columns.get(i)+" ="+values.get(i));
        }

        sql.append(" where id=?");

        //执行
        int result = 0;
        try(PreparedStatement ps = conn.prepareStatement(sql.toString())){
            ps.setInt(1,id);
            result = ps.executeUpdate();
        }
        return result == 1;
    }

    @Override
    public <T> boolean delete(Connection conn, T po) throws SQLException {
        if(!po.getClass().isAnnotationPresent(Table.class)){
            throw new SQLException("此类没有被标记为表");
        }
        String tableName = po.getClass().getAnnotation(Table.class).name();
        Integer id = null;

        for(Field field : po.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(Column.class)){
                field.setAccessible(true);
                try{
                    if("id".equals(field.get(po))){
                        id = field.getInt(po);
                    }
                }catch(IllegalAccessException e){
                    throw new SQLException("获取字段值错误");
                }
            }
        }
        if(id==null){
            throw new SQLException("id为空！");
        }
        String sql = "delete from " + tableName + " where id = ?";

        int result = 0;
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,id);
            result = ps.executeUpdate();
        }

        return result == 1;
    }

    @Override
    public <T> List<T> select(Connection conn, T po) throws SQLException {
        return Collections.emptyList();
    }


}
