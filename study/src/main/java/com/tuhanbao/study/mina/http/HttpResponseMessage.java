package com.tuhanbao.study.mina.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;

public class HttpResponseMessage
{
    public static final int HTTP_STATUS_SUCCESS = 200;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    private final Map<String, String> headers = new HashMap<String, String>();

    private final ByteArrayOutputStream body = new ByteArrayOutputStream(2048);

    private int responseCode = 200;

    public HttpResponseMessage()
    {
        this.headers.put("Server", "HttpServer (Mina 2.0)");
        this.headers.put("Cache-Control", "private");
        this.headers.put("Content-Type", "text/html; charset=utf-8");
        this.headers.put("Connection", "keep-alive");
        this.headers.put("Keep-Alive", "200");
        this.headers.put("Date", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date()));
        this.headers.put("Last-Modified", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date()));
    }

    public Map<String, String> getHeaders()
    {
        return this.headers;
    }

    public void setContentType(String contentType)
    {
        this.headers.put("Content-Type", contentType);
    }

    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }

    public int getResponseCode()
    {
        return this.responseCode;
    }

    public void appendBody(byte[] b)
    {
        try
        {
            this.body.write(b);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void appendBody(String s)
    {
        try
        {
            this.body.write(s.getBytes());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public IoBuffer getBody()
    {
        return IoBuffer.wrap(this.body.toByteArray());
    }

    public int getBodyLength()
    {
        return this.body.size();
    }
}
