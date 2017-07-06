package com.tuhanbao.base.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Vector;

import com.tuhanbao.base.Constants;

/**
 * 
 * HttpUtil跟HttpSendUtil很类似，都可用
 * @see HttpUtil
 * @author Administrator
 *
 */
public class HttpSendUtil
{
    public static final String GET = "GET";

    public static final String POST = "POST";
    
    /**
     * 
     * @param urlString
     * @param method
     * @param parameters get:直接拼接在url上的参数  post:放在字节流中的字符串
     * @param properties 放在header中的参数
     * @return
     * @throws IOException
     */
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
                param.append(entry.getKey()).append(Constants.EQUAL).append(HttpUtil.encode(entry.getValue()));
                param.append(Constants.AND_MARK);
            }
            param.deleteCharAt(param.length() - 1);
            urlString = urlString + param.toString();
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
}
