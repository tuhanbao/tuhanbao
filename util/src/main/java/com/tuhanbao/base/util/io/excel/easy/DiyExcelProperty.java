package com.td.ca.base.util.io.excel.easy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiyExcelProperty {
    /**
     * 语言资源key
     */
    String value() default "";

    Class<?> enumClass() default NoEnum.class;

    boolean required() default false;

    String timeFormat() default "";

    boolean unique() default false;
}
