package com.tuhanbao.thirdapi;

import com.tuhanbao.base.util.objutil.StringUtil;

public enum ProjectType {
    YYT("yyt_fz");
    
    private String path;
    
    private ProjectType() {
        this(null);
    }
    
    private ProjectType(String path) {
        this.path = path;
    }
    
    public String getPath() {
        if (StringUtil.isEmpty(path)) {
            return this.name().toLowerCase();
        }
        return path;
    }
}
