package com.tuhanbao.study.mina.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Vector;

import com.tuhanbao.io.base.Constants;

public class HttpSendUtil
{
    public static final String GET = "GET";

    public static final String POST = "POST";
    
    public static HttpRespons send(String urlString, String method, Map<String, String> parameters,
            Map<String, String> properties) throws IOException
    {
        HttpURLConnection urlConnection = null;
        if ((method.equalsIgnoreCase(GET)) && (parameters != null))
        {
            StringBuilder param = new StringBuilder();
            param.append(Constants.QUESTION_MARK);
            for (Map.Entry<String, String> entry : parameters.entrySet())
            {
                param.append(entry.getKey()).append(Constants.EQUAL).append(entry.getValue());
                param.append(Constants.AND_MARK);
            }
            param.deleteCharAt(param.length() - 1);
            urlString = urlString + encode(param.toString());
        }
        
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(Constants.TIMEOUT * 1000);
        urlConnection.setReadTimeout(Constants.TIMEOUT * 1000);
        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);

        if (properties != null)
        {
            for (Map.Entry<String, String> entry : properties.entrySet())
            {
                urlConnection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        
        if ((method.equalsIgnoreCase(POST)) && (parameters != null))
        {
            StringBuilder param = new StringBuilder();
            for (Map.Entry<String, String> entry : parameters.entrySet())
            {
                param.append(entry.getKey()).append(Constants.EQUAL).append(entry.getValue());
                param.append("&");
            }
            param.deleteCharAt(param.length() - 1);
            urlConnection.getOutputStream().write(param.toString().getBytes());
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }

        return makeContent(urlString, urlConnection);
    }

    private static HttpRespons makeContent(String urlString, HttpURLConnection urlConnection) throws IOException
    {
        HttpRespons httpResponser = new HttpRespons();
        try
        {
            InputStream in = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            httpResponser.contentCollection = new Vector<String>();
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null)
            {
                httpResponser.contentCollection.add(line);
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            String ecod = urlConnection.getContentEncoding();
            if (ecod == null)
            {
                ecod = Constants.UTF_8;
            }
            httpResponser.urlString = urlString;

            httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
            httpResponser.file = urlConnection.getURL().getFile();
            httpResponser.host = urlConnection.getURL().getHost();
            httpResponser.path = urlConnection.getURL().getPath();
            httpResponser.port = urlConnection.getURL().getPort();
            httpResponser.protocol = urlConnection.getURL().getProtocol();
            httpResponser.query = urlConnection.getURL().getQuery();
            httpResponser.ref = urlConnection.getURL().getRef();
            httpResponser.userInfo = urlConnection.getURL().getUserInfo();

            httpResponser.content = new String(temp.toString().getBytes(), ecod);
            httpResponser.contentEncoding = ecod;
            httpResponser.code = urlConnection.getResponseCode();
            httpResponser.message = urlConnection.getResponseMessage();
            httpResponser.contentType = urlConnection.getContentType();
            httpResponser.method = urlConnection.getRequestMethod();
            httpResponser.connectTimeout = urlConnection.getConnectTimeout();
            httpResponser.readTimeout = urlConnection.getReadTimeout();

            return httpResponser;
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (urlConnection != null) urlConnection.disconnect();
        }
    }
    
    @SuppressWarnings("deprecation")
    public static String encode(String s)
    {
        try
        {
            return URLEncoder.encode(s, Constants.UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            return URLEncoder.encode(s);
        }
    }
    
    @SuppressWarnings("deprecation")
    public static String decode(String s)
    {
        try
        {
            return URLDecoder.decode(s, Constants.UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            return URLDecoder.decode(s);
        }
    }
}
