package com.tuhanbao.web.controller.helper;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.web.ServerManager;

public class MyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    public MyPropertyPlaceholderConfigurer() {
        super();
        start();
    }

    public void setProperties(String properties) {
        String[] keys = StringUtil.string2Array(properties);
        Resource[] resources = new Resource[keys.length];
        int i = 0;
        for (String key : keys) {
            String url = null;
            url = ConfigManager.getPropertiesPath(key);
            //如果当模式但是找不到debug配置文件，2正常模式
            if (StringUtil.isEmpty(url) && ConfigManager.getCurrentConfigPattern() != ConfigManager.DEFAULT_CONFIG_PATTERN) {
                url = ConfigManager.getPropertiesPath(key, ConfigManager.DEFAULT_CONFIG_PATTERN);
            }
            resources[i] = new ClassPathResource(getSimplePath(url));
        }
        super.setLocations(resources);
    }

    private String getSimplePath(String url) {
        if (StringUtil.isEmpty(url)) {
            return url;
        }
        return url.substring(url.indexOf("init" + File.separator + "config"));
    }

    /**
     * 服务器启动
     */
    public static void start() {
        // web模式
        File f = null;
        try {
            f = new ClassPathResource(Constants.CONFIG_URL, ServerManager.class.getClassLoader()).getFile();
        }
        catch (IOException e) {
            System.exit(0);
        }
        // 启动LogManager
        ConfigManager.init(f);
        Constants.CONFIG_ROOT = FileUtil.appendPath(f.getParentFile().getParent(), Constants.CONFIG_URL);
        LogManager.init(f.getParentFile().getParent() + Constants.FILE_SEP + "log4j.properties");
        
    }
}
