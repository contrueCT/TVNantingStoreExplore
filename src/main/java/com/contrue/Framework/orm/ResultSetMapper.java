package com.contrue.Framework.orm;

import com.contrue.Framework.annotation.Column;
import com.contrue.Framework.annotation.ForeignKey;
import com.contrue.util.SystemLogger;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @author confff
 */
public class ResultSetMapper {
    /**
     * 主方法
     * @param rs sql返回值
     * @param mainClazz 主对象类型
     * @return 对象列表
     */
    public <T> List<T> mapResultSet(ResultSet rs, Class<T> mainClazz) throws SQLException {
        //主对象map
        Map<Integer,T> mainObjectMap = new HashMap<Integer,T>();
        //嵌套对象map
        Map<Integer,Map<String,List<Object>>> nestedObjectMap = new HashMap<>();

        //获取主键字段
        String primaryKeyColumn = "id";

        //有效返回字段
        Set<String> availableColumns = getAvailableColumns(rs);

        //确保主键列存在
        if (!availableColumns.contains(primaryKeyColumn)) {
            throw new IllegalArgumentException("主键列'"+primaryKeyColumn+"'在结果集中不存在");
        }

        while(rs.next()){
            //主键值（id）
            int primaryKeyValue = rs.getInt(primaryKeyColumn);
            T mainObject = mainObjectMap.computeIfAbsent(primaryKeyValue,key -> createInstance(mainClazz));
            //处理简单字段
            processSimpleField(rs,mainObject,availableColumns);
            //处理嵌套字段
            processNestedObjects(rs,primaryKeyValue,nestedObjectMap,mainClazz,availableColumns);

        }
        //将嵌套对象集合封装到主对象中
        assignNestedObjectsToMainObjects(mainObjectMap,nestedObjectMap);

        return new ArrayList<>(mainObjectMap.values());
    }

    /**
     * 生成实例对象
     * @param mainClazz 主对象类型
     * @return 生成的对象
     */
    private <T> T createInstance(Class<T> mainClazz) {
        try{
            return mainClazz.getDeclaredConstructor().newInstance();
        }catch(Exception e){
            SystemLogger.logError("生成对象失败",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理简单字段
     * @param mainObject 当前的主对象
     */
    private <T> void processSimpleField(ResultSet resultSet, T mainObject, Set<String> availableColumns) {
        Class<?> mainObjectClass = mainObject.getClass();

        for(Field field : mainObjectClass.getDeclaredFields()){
            field.setAccessible(true);
            //跳过List字段
            if(field.getType() == List.class){
                continue;
            }
            //根据注解获取字段值
            Column columnAnnotation = field.getAnnotation(Column.class);
            if(columnAnnotation != null){
                String columnName = columnAnnotation.name();
                Class<?> targetType = field.getType();
                //判断此字段是否有返回
                try {
                    if (availableColumns.contains(columnName)) {
                        Object value = resultSet.getObject(columnName);
                        if (value != null) {
                            field.set(mainObject, value);
                        }
                    }
                } catch (Exception e) {
                    SystemLogger.logError(e.getMessage(),e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 处理嵌套对象集合
     * @param primaryKeyValue 主键id
     * @param nestedObjectMap 嵌套集合map
     */
    private void processNestedObjects(ResultSet resultSet, int primaryKeyValue, Map<Integer, Map<String, List<Object>>> nestedObjectMap, Class<?> mainClass, Set<String> availableColumns) throws SQLException{
        //获取或创建与该主键（id）对应的各个字段的列表map对象
        Map<String,List<Object>> fieldNestedObjects = nestedObjectMap.computeIfAbsent(primaryKeyValue,key -> new HashMap<>());
        //处理每个List类型字段
        for(Field field : mainClass.getDeclaredFields()){
            field.setAccessible(true);

            if(field.getType() == List.class&&field.getGenericType() instanceof ParameterizedType){
                ForeignKey foreignKeyAnnotation = field.getAnnotation(ForeignKey.class);
                if(foreignKeyAnnotation != null){
                    //获取字段名作为映射的KEY
                    String fieldName = field.getName();

                    //获取嵌套的实际类
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    Class<?> nestedClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    //获取此字段的嵌套对象集合
                    List<Object> nestedList = fieldNestedObjects.computeIfAbsent(fieldName,key -> new ArrayList<>());

                    //检查是否有该嵌套类的字段，如果没有直接跳过
                    boolean hasRelevantColumns = false;
                    for (Field nestedField : nestedClass.getDeclaredFields()) {
                        Column column = nestedField.getAnnotation(Column.class);
                        if (column != null && availableColumns.contains(column.name())) {
                            hasRelevantColumns = true;
                            break;
                        }
                    }

                    // 如果没有相关列，跳过此嵌套对象的处理
                    if (!hasRelevantColumns) {
                        continue;
                    }

                    //获取嵌套对象
                    Object nestedObject = createInstance(nestedClass);

                    boolean hasData = processNestedObjectFields(resultSet,nestedObject,nestedClass,availableColumns);
                    if(hasData){
                        nestedList.add(nestedObject);
                    }
                }
            }
        }
    }

    /**
     * 处理嵌套集合中的对象的字段
     * @param nestedObject  嵌套的对象
     * @param nestedClass   嵌套对象的类型
     * @return  有没有数据被赋值
     */
    private boolean processNestedObjectFields(ResultSet resultSet,Object nestedObject, Class<?> nestedClass,Set<String> availableColumns) throws SQLException{
        boolean hasData = false;
        //遍历插入嵌套类中的字段
        for(Field field : nestedClass.getDeclaredFields()){
            field.setAccessible(true);
            if(field.getType() == List.class){
                continue;
            }

            //根据注解获取字段值
            Column columnAnnotation = field.getAnnotation(Column.class);
            if(columnAnnotation != null){
                String columnName = columnAnnotation.name();
                try {
                    //判断是否返回了该字段
                    if (availableColumns.contains(columnName)) {
                        Object value = resultSet.getObject(columnName);

                        if (value != null) {
                            field.set(nestedObject, value);
                            hasData = true;
                        }
                    }
                } catch (IllegalAccessException e) {
                    SystemLogger.logError(e.getMessage(),e);
                    throw new RuntimeException(e);
                }
            }
        }
        return hasData;
    }

    /**
     * 将嵌套对象集合放入主对象字段中
     * @param mainObjectMap 主对象map
     * @param nestedObjectMap   各嵌套对象的map
     */
    private <T> void assignNestedObjectsToMainObjects(Map<Integer,T> mainObjectMap,Map<Integer,Map<String,List<Object>>> nestedObjectMap){
        for(Map.Entry<Integer,T> entry : mainObjectMap.entrySet()){
            int primaryKeyValue = entry.getKey();
            T mainObject = entry.getValue();
            //映射嵌套字段集合
            Map<String,List<Object>> fieldNestedObjects = nestedObjectMap.get(primaryKeyValue);
            if(fieldNestedObjects == null||fieldNestedObjects.isEmpty() ){
                continue;
            }

            for(Field field : mainObject.getClass().getDeclaredFields()){
                field.setAccessible(true);

                if(field.getType() == List.class&&field.getAnnotation(ForeignKey.class) != null){
                    //根据字段名赋值
                    String fieldName = field.getName();
                    List<Object> nestedList = fieldNestedObjects.get(fieldName);

                    try {
                        if(nestedList != null&&!nestedList.isEmpty()){
                            field.set(mainObject,nestedList);
                        }else{
                            field.set(mainObject, Collections.emptyList());
                        }
                    } catch (IllegalAccessException e) {
                        SystemLogger.logError("嵌套对象装配时出错，赋值到对象中时失败",e);
                        throw new RuntimeException(e);
                    }
                }
            }

        }
    }

    private Set<String> getAvailableColumns(ResultSet resultSet) throws SQLException {
        Set<String> columns = new HashSet<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            columns.add(metaData.getColumnLabel(i));
        }

        return columns;
    }
}
