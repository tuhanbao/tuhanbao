package com.tuhanbao.autotool;

import com.tuhanbao.base.chain.Context;
import com.tuhanbao.base.util.config.Config;

/**
 * AutoGenratorCode
 * 自动生成代码的上下文
 * @author Administrator
 *
 */
public class AGCContext extends Context {
    
    private Config solidConfig;
    
    public AGCContext(Config solidConfig) {
        this.solidConfig = solidConfig;
    }
    
    public Config getSolidConfig() {
        return solidConfig;
    }
}
