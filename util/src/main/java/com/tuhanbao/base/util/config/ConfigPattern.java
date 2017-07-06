package com.tuhanbao.base.util.config;

/**
 * 配置文件支持的模式
 * 
 * @author Administrator
 *
 */
public class ConfigPattern {

    private String name;
    
    private String suffix;

    public ConfigPattern(String name) {
        this(name, "_" + name);
    }

    public ConfigPattern(String name, String suffix) {
        this.name = name.toLowerCase();
        this.suffix = suffix.toLowerCase();
    }

    public String getName() {
        return name;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    public int hashCode() {
        return name.hashCode();
    }
    
    public boolean equals(Object o) {
        return this.name.equals(((ConfigPattern)o).name);
    }
    
    public String toString() {
        return this.name;
    }
}
