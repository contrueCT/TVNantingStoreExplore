import com.contrue.annotation.Column;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class Test {

    private <T> List<T> executeQuery(PreparedStatement preparedStatement, Class<T> resultType) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return mapResultSetToObject(resultSet,resultType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public <T> List<T> mapResultSetToObject(ResultSet resultSet, Class<T> clazz) throws Exception {
        Map<Integer, T> objectMap = new HashMap<>(); // 存储主对象
        Map<Integer, List<?>> nestedObjectMap = new HashMap<>(); // 存储嵌套对象，使用通配符泛型

        while (resultSet.next()) {
            // 获取主对象的唯一标识（如 user_id）
            int userId = resultSet.getInt("user_id");

            // 如果主对象尚未创建，则创建并存储
            T obj = objectMap.computeIfAbsent(userId, key -> {
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            // 解析主对象字段
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                // 如果字段是嵌套集合类型（如 List<Permission>），跳过直接处理
                if (field.getType() == List.class) {
                    continue;
                }

                // 获取列名并设置字段值
                String columnName = getColumnName(field);
                Object value = resultSet.getObject(columnName);
                field.set(obj, value);
            }

            // 处理嵌套对象（如 Permission）
            handleNestedObjects(resultSet, userId, nestedObjectMap, clazz);
        }

        // 将嵌套对象填充到主对象中
        for (Map.Entry<Integer, T> entry : objectMap.entrySet()) {
            T obj = entry.getValue();
            List<?> nestedObjects = nestedObjectMap.get(entry.getKey()); // 获取 List<?>
            setNestedObjects(obj, nestedObjects); // 假设 setNestedObjects 可以处理 List<?>
        }

        return new ArrayList<>(objectMap.values());
    }

    private <T> void handleNestedObjects(ResultSet resultSet, int userId,
                                         Map<Integer, List<?>> nestedObjectMap, Class<?> parentClass) throws Exception { // 修改 nestedObjectMap 的类型
        // 遍历主对象的所有字段
        for (Field field : parentClass.getDeclaredFields()) {
            field.setAccessible(true); // 允许访问私有字段

            // 如果字段类型是 List，则认为它是嵌套对象集合
            if (field.getType() == List.class) {
                // 获取泛型的实际类型（如 List<Permission> -> Permission）
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> nestedClass = (Class<?>) listType.getActualTypeArguments()[0];

                // 动态创建嵌套对象的实例
                Object nestedObj = createInstance(nestedClass);

                // 遍历嵌套对象的字段，从 ResultSet 中获取值并设置到嵌套对象中
                Field[] nestedFields = nestedClass.getDeclaredFields();
                for (Field nestedField : nestedFields) {
                    nestedField.setAccessible(true); // 允许访问私有字段

                    // 获取嵌套字段对应的列名（支持注解）
                    String columnName = getColumnName(nestedField);

                    // 从 ResultSet 中获取值并设置到嵌套对象中
                    Object value = resultSet.getObject(columnName);
                    nestedField.set(nestedObj, value);
                }

                // 将嵌套对象添加到 nestedObjectMap 中
                List<Object> objectList = (List<Object>) nestedObjectMap.computeIfAbsent(userId, k -> new ArrayList<>());
                objectList.add(nestedObj);

            }
        }
    }

    private <T> T createInstance(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.getDeclaredConstructor().newInstance();
    }

    private String getColumnName(Field field) {
        Column columnAnnotation = field.getAnnotation(Column.class);
        return columnAnnotation != null ? columnAnnotation.name() : field.getName();
    }

    private <T> void setNestedObjects(T obj, List<?> nestedObjects) throws Exception {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() == List.class) {
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> nestedClass = (Class<?>) listType.getActualTypeArguments()[0];

                if (nestedObjects != null && !nestedObjects.isEmpty()) {
                    // 检查 nestedObjects 中的元素类型是否与 nestedClass 兼容
                    List<?> filteredList = nestedObjects.stream()
                            .filter(nestedClass::isInstance)
                            .collect(Collectors.toList());

                    // 将过滤后的列表设置为字段的值
                    field.set(obj, filteredList);
                } else {
                    field.set(obj, Collections.emptyList());
                }
            }
        }
    }
}
