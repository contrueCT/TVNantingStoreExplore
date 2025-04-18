package com.contrue.Framework.orm.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

    //String是xml的namespace+sql的id
    Map<String,MappedStatement> mappedStatementMap = new ConcurrentHashMap<String, MappedStatement>();

    public MappedStatement getMappedStatement(String statementId) {
        return mappedStatementMap.get(statementId);
    }
}
