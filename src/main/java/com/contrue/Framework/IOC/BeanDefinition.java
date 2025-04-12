package com.contrue.Framework.IOC;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanDefinition {
    private String name;
    private Class<?> type;
    private boolean singleton = true;

    public BeanDefinition(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }
}
