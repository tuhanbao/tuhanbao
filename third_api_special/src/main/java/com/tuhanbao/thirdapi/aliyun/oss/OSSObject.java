package com.tuhanbao.thirdapi.aliyun.oss;

import java.net.URL;

public class OSSObject
{
    private String key;
    
    private URL url;
    
    private long size;

    public OSSObject(String key, URL url, long size)
    {
        this.key = key;
        this.url = url;
        this.size = size;
    }

    public String getKey()
    {
        return key;
    }

    public URL getUrl()
    {
        return url;
    }
    
    public long getSize()
    {
        return size;
    }
}
