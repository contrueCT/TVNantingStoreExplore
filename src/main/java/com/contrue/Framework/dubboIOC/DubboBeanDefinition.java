package com.contrue.Framework.dubboIOC;

import com.contrue.Framework.IOC.BeanDefinition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author confff
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DubboBeanDefinition extends BeanDefinition {
    private boolean isDubboService;
    private String version;
    private String group;
    private int timeout;
    private int retries;
    private Class<?> interfaceClass;

    public DubboBeanDefinition(String name, Class<?> type) {
        super(name, type);
    }

    public DubboBeanDefinition(String name, Class<?> type, boolean isDubboService, String version,
                               String group, int timeout, int retries, Class<?> interfaceClass){
        super(name, type);
        this.isDubboService = isDubboService;
        this.version = version;
        this.group = group;
        this.timeout = timeout;
        this.retries = retries;
        this.interfaceClass = interfaceClass;
    }

    public boolean isDubboService() {
        return isDubboService;
    }


}
