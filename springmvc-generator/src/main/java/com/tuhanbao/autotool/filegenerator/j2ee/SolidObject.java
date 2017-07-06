package com.tuhanbao.autotool.filegenerator.j2ee;

import com.tuhanbao.base.util.objutil.OverwriteStrategy;

public class SolidObject {
    private String key;
    
    //默认路径
    private String url = "/bak";
    
    private String targetUrl;
    
    private OverwriteStrategy overwriteStrategy = OverwriteStrategy.COVER;

    public SolidObject(String key, String url, String targetUrl, int overwriteStrategy) {
        super();
        this.key = key;

        if (url == null) url = "";
        this.url = url;
        if (targetUrl == null) targetUrl = "";
        this.targetUrl = targetUrl;
        this.overwriteStrategy = OverwriteStrategy.getOverwriteStrategy(overwriteStrategy);
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public OverwriteStrategy getOverwriteStrategy() {
        return overwriteStrategy;
    }
    
}
