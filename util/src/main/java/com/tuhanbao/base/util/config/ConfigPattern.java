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

    private String path;
    
    public static final ConfigPattern PRODUCE = new ConfigPattern("");
    public static final ConfigPattern DEBUG = new ConfigPattern("debug");
    public static final ConfigPattern PRE = new ConfigPattern("pre");
    public static final ConfigPattern GRAY = new ConfigPattern("gray");

    public ConfigPattern(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getSuffix() {
        return "_" + path;
    }
    
    public int hashCode() {
        return path.hashCode();
    }
    
    public boolean equals(Object o) {
        return this.path.equals(((ConfigPattern)o).path);
    }

    public static ConfigPattern getPattern(String name) {
        if (StringUtil.isEmpty(name)) return PRODUCE;
        
        if (DEBUG.path.equals(name)) return DEBUG;
        if (PRE.path.equals(name)) return PRE;
        if (GRAY.path.equals(name)) return GRAY;
        return new ConfigPattern(name);
    }
    
    public String toString() {
        if (this == PRODUCE) return "produce";
        return this.path;
    }
}
