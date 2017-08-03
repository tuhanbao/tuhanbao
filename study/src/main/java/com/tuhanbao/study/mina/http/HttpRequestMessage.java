package com.tuhanbao.study.mina.http;

import java.util.Map;

import com.tuhanbao.io.base.Constants;

public class HttpRequestMessage
{
    private Map<String, String> headers = null;
    
    private Map<String, String> paremeters = null;

    public String getContext()
    {
        return this.headers.get("Context");
    }

    public String getParameter(String name)
    {
        return paremeters.get(name);
    }

    public String getHeader(String name)
    {
        return this.headers.get(name);
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();

        for (Map.Entry<String, String> e : this.headers.entrySet())
        {
            str.append(e.getKey() + " : " + e.getValue() + Constants.ENTER);
        }
        return str.toString();
    }
    
    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }

    public void setParemeters(Map<String, String> paremeters)
    {
        this.paremeters = paremeters;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public Map<String, String> getParemeters()
    {
        return paremeters;
    }

}
