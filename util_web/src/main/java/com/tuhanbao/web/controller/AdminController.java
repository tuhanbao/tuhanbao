package com.tuhanbao.web.controller;

import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.web.controller.authority.UserAccessApiInterceptor;
import com.tuhanbao.web.controller.helper.JsonHttpMessageConverter;

@Controller
@RequestMapping(value = "/admin", produces = "text/html;charset=UTF-8")
public class AdminController {
    private static final String REFRESH_CONFIG = "refreshConfig";

    private static final String VIEW_CONFIG = "viewConfig";
    
    /**
     * =转码为  %3D
     * @param password
     * @param key
     * @param content
     * @return
     */
    @RequestMapping(value = REFRESH_CONFIG)
    @ResponseBody
    public Object refreshConfig(HttpServletRequest request, @RequestParam("key") String key, String content) {
        //不提示任何错误，以免别人捣乱
        if (UserAccessApiInterceptor.isSupperManager(request)) {
            ConfigManager.refreshConfig(key, content);
        }
        return JsonHttpMessageConverter.NULL;
    }
    
    /**
     * =转码为  %3D
     * @param password
     * @param key
     * @param content
     * @return
     */
    @RequestMapping(value = VIEW_CONFIG)
    @ResponseBody
    public Object viewConfig(HttpServletRequest request, @RequestParam("key") String key, String itemKey) {
        //不提示任何错误，以免别人捣乱
        if (UserAccessApiInterceptor.isSupperManager(request)) {
            Config config = ConfigManager.getConfig(key);
            
            if (config == null) return JsonHttpMessageConverter.NULL;
            
            if (StringUtil.isEmpty(itemKey)) {
                StringBuilder sb = new StringBuilder();
                for (Entry<String, String> entry : config.getProperties().entrySet()) {
                    sb.append(entry.getKey()).append(Constants.COLON).append(entry.getValue()).append(Constants.ENTER);
                }
                return sb.toString();
            }
            else {
                return config.getString(itemKey);
            }
        }
        return JsonHttpMessageConverter.NULL;
    }
}
