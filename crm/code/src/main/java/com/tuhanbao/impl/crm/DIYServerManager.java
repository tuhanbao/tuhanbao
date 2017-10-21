package com.tuhanbao.impl.crm;

import org.springframework.stereotype.Component;

import com.tuhanbao.web.IServerManager;

@Component
public class DIYServerManager implements IServerManager {
    /**
     * 有需要请自行覆盖，服务器启动时会自动执行
     */
    public void init() {
    }
}
