package com.tuhanbao.base.util.config;

import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 配置文件支持的模式
 * 
 * 系统默认预置了4种模式，也可以自定义
 * 
 * @author Administrator
 *
 */
public class ConfigPattern {

    private String name;
    
    public static final ConfigPattern PRODUCE = new ConfigPattern("");
    public static final ConfigPattern DEBUG = new ConfigPattern("debug");
    public static final ConfigPattern PRE = new ConfigPattern("pre");
    public static final ConfigPattern GRAY = new ConfigPattern("gray");

    public ConfigPattern(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return "_" + name;
    }
    
    public int hashCode() {
        return name.hashCode();
    }
    
    public boolean equals(Object o) {
        return this.name.equals(((ConfigPattern)o).name);
    }

    public static ConfigPattern getPattern(String name) {
        if (StringUtil.isEmpty(name)) return PRODUCE;
        
        if (DEBUG.name.equals(name)) return DEBUG;
        if (PRE.name.equals(name)) return DEBUG;
        if (GRAY.name.equals(name)) return DEBUG;
        return new ConfigPattern(name);
    }
    
    public String toString() {
        return this.name;
    }
}
