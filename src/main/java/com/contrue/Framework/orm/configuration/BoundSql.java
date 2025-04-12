package com.contrue.Framework.orm.configuration;
import lombok.Data;

import java.util.*;
/**
 * @author confff
 */
@Data
public class BoundSql {
    private String sqlText;
    //每次解析sql的起始位置
    private int findPosition = 0;
    //
    String openToken;
    String closeToken;
    //占位符始末位置
    Map<Integer,Integer> map = new TreeMap<Integer,Integer>();
    //sql语句中解析出的原类参数名列表
    private List<String> parameterMapperList = new ArrayList<>();

    private void parseSqlText(String sql) {
        int openIndex = sql.indexOf(openToken, findPosition);
        if(!(openIndex == -1)) {
            int closeIndex = sql.indexOf(closeToken, openIndex);
            if(!(closeIndex == -1)) {
                map.put(openIndex, closeIndex);
                findPosition = closeIndex + 1;
                parseSqlText(sql);
            }
        }
    }

    public BoundSql(MappedStatement mappedStatement) {
        this.sqlText = mappedStatement.getSql();
        this.openToken = mappedStatement.getOpenToken();
        this.closeToken = mappedStatement.getCloseToken();
        this.parseSqlText(this.sqlText);
        Set<Map.Entry<Integer,Integer>> entrySet = map.entrySet();
        for (Map.Entry<Integer,Integer> entry : entrySet) {
            Integer key = entry.getKey()+2;
            Integer value = entry.getValue();
            //将解析出的参数名称（原类中的字段名）加入集合
            System.out.println(sqlText);
            System.out.println(value);
            System.out.println(key);
            parameterMapperList.add(sqlText.substring(key,value));
        }
        for(String parameterMapper : parameterMapperList) {
            sqlText = sqlText.replace(openToken+parameterMapper+closeToken,"?");
        }
    }
}
