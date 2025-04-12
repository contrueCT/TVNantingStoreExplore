package com.contrue.Framework.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author confff
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {
    String name();
}
