package com.tuhanbao.base.util.config;

public interface ConfigRefreshListener
{
    void refresh();
    
    String getKey();
}
