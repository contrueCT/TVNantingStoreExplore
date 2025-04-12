package com.contrue.orm.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * sql语句映射的实体类
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappedStatement {
    private String id;
    private String sql;
    private String parameters;
    private String resultType;
    private String openToken;
    private String closeToken;
    private String actionType;

    public String getParameters() {
        return parameters;
    }
}
