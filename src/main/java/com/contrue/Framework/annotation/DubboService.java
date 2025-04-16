package com.contrue.Framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author confff
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface DubboService {
    String value() default "";
    String version() default "";
    String group() default "";
    int timeout() default 0;
    int retries() default 2;
    String interfaceClass() default "";
}
