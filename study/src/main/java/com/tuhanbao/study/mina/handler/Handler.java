package com.tuhanbao.study.mina.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler
{
    public int value();
    
    public boolean isHttp() default false;
}
