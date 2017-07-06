package com.tuhanbao.base.util.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * HttpUtil跟HttpSendUtil很类似，都可用
 * 
 * @see HttpSendUtil
 * @author Administrator
 *
 */
public class HttpUtil {
    // public static void main(String args[]) throws IOException, JSONException
    // {
    //// post("http://localhost:8080/test/test/select?name=1", "name=haha");
    //// get("http://localhost:8080/test/test/update?name=1");
    //// Map<String, String> params = new HashMap<String, String>();
    //// params.put("name", "1");
    //// Map<String, String> properties = new HashMap<String, String>();
    //// properties.put("name", "2");
    //// HttpSendUtil.send("http://localhost:8080/test/test/update",
    // HttpSendUtil.GET, params, properties);
    //// HttpSendUtil.send("http://localhost:8080/test/test/select",
    // HttpSendUtil.POST, params, properties);
    ////
    // JSONObject json = new JSONObject();
    // json.put("name", "tubie");
    // json.put("password", "132465");
    // encode(json.toString() + "==");
    // }

    /**
     * content post传输的字节流，暂时只支持String类型
     * 
     * @param url
     * @param content
     * @return
     */
    public static String post(String url, String content) {
        PostMethod method = new PostMethod(url);
        try {
            if (!StringUtil.isEmpty(content)) {
                method.setRequestEntity(new StringRequestEntity(content, "text/xml", Constants.UTF_8));
            }
            int status = getHttpClient(Constants.UTF_8).executeMethod(method);
            if (HttpStatus.SC_OK == status) {
                String result = method.getResponseBodyAsString();
                return result;
            }
            else {
                throw new MyException(-1, "http send error : " + status);
            }
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
    }

    public static String get(String url) {
        return get(url, null, Constants.UTF_8);
    }

    public static String get(String url, Map<String, String> params) {
        return get(url, params, Constants.UTF_8);
    }

    public static String get(String url, Map<String, String> params, String charset) {
        return get(url, params, null, charset);
    }

    public static String get(String url, Map<String, String> params, Map<String, String> headVars, String charset) {
        GetMethod method = new GetMethod(url + map2Url(params, charset));
        if (headVars != null) {
            for (Entry<String, String> var : headVars.entrySet()) {
                method.setRequestHeader(new Header(var.getKey(), var.getValue()));
            }
        }

        try {
            int status = getHttpClient(charset).executeMethod(method);
            if (HttpStatus.SC_OK == status) {
                String result = method.getResponseBodyAsString();
                return result;
            }
            else {
                throw new MyException(-1, "http send error : " + status);
            }
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
    }

    public static String map2Url(Map<String, String> parameters) {
        return map2Url(parameters, Constants.UTF_8);
    }

    public static String map2Url(Map<String, String> parameters, String charset) {
        if (parameters == null || parameters.isEmpty()) return Constants.EMPTY;

        StringBuilder param = new StringBuilder();
        param.append(Constants.QUESTION_MARK);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            param.append(entry.getKey()).append(Constants.EQUAL).append(encode(entry.getValue(), charset));
            param.append(Constants.AND_MARK);
        }
        param.deleteCharAt(param.length() - 1);
        return param.toString();
    }

    private static HttpClient getHttpClient(String charset) {
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset(charset);
        client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
        client.getHttpConnectionManager().getParams().setSoTimeout(15000);
        return client;
    }

    public static String encode(String s) {
        return encode(s, Constants.UTF_8);
    }

    public static String encode(String s, String charset) {
        try {
            return URLEncoder.encode(s, charset);
        }
        catch (UnsupportedEncodingException e) {
            throw MyException.getMyException(e, "encode error : " + s);
        }
    }

    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, Constants.UTF_8);
        }
        catch (UnsupportedEncodingException e) {
            throw MyException.getMyException(e, "encode error : " + s);
        }
    }
}
