package com.tuhanbao.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigPattern;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * URL解析工具
 * 
 * @author zhihongp
 * 
 */
public class URLUtil {

    // private final static String REAL_IP_BEFORE_NGINX_PROXY = "X-real-ip";

    // private final static String FORWARD_IP_BEFORE_NGINX_PROXY =
    // "X-Forwarded-For";
    
    /**
     * 获取没有后缀的uri
     * 
     * @param uri
     * @return
     */
    public static final String getURIWithoutSuffix(String uri) {
        if (StringUtil.isEmpty(uri)) {
            return uri;
        }

        int pointIndex = uri.indexOf(".");
        if (pointIndex == -1) {
            return uri;
        }

        return uri.substring(0, pointIndex);
    }

    /**
     * 获取uri的后缀
     * 
     * @param uri
     * @return
     */
    public static final String getURISuffix(String uri) {
        if (StringUtil.isEmpty(uri)) {
            return Constants.EMPTY;
        }

        int pointIndex = uri.indexOf(".");
        if (pointIndex == -1) {
            return Constants.EMPTY;
        }

        return uri.substring(pointIndex);
    }

    /**
     * 获取没有后缀的uri
     * 
     * @param url
     * @param contextPath
     * @return
     */
    public static final String getURIWithoutSuffix(String url, String contextPath) {
        if (StringUtil.isEmpty(url)) {
            return url;
        }

        int contextPathPonit = url.indexOf(contextPath);

        if (contextPathPonit != -1) {
            url = url.substring(contextPathPonit);
        }

        int pointIndex = url.indexOf(".");

        if (pointIndex == -1) {
            return url;
        } else {
            return url.substring(0, pointIndex);
        }
    }

    /**
     * 是否是json请求
     * 
     * @return
     */
    public static final boolean isAjaxUrl(HttpServletRequest request) {
        String accept = request.getHeader("Accept");

        if (!StringUtil.isEmpty(accept) && (accept.contains("application/json") || accept.contains("application/jsonp"))) {
            return true;
        }

        String uri = request.getRequestURI();
        int suffixIndex = uri.lastIndexOf(".");

        if (suffixIndex != -1) {
            String suffix = uri.substring(suffixIndex + 1);
            if ("json".equals(suffix) || "jsonp".equals(suffix)) {
                return true;
            }
        }

        String format = request.getParameter("format");

        if ("json".equals(format) || "jsonp".equals(format)) {
            return true;
        }

        String ajaxHeader = request.getHeader("X-Requested-With");

        if (!StringUtil.isEmpty(ajaxHeader) && "XMLHttpRequest".equalsIgnoreCase(ajaxHeader)) {
            return true;
        }

        return false;
    }

    /**
     * 是否是jsonp请求
     * 
     * @return
     */
    public static final boolean isJsonp(HttpServletRequest request) {
        String accept = request.getHeader("Accept");

        if (!StringUtil.isEmpty(accept) && (accept.contains("application/jsonp"))) {
            return true;
        }

        String format = request.getParameter("format");

        if ("jsonp".equals(format)) {
            return true;
        }

        String uri = request.getRequestURI();
        int suffixIndex = uri.lastIndexOf(".");

        if (suffixIndex != -1) {
            String suffix = uri.substring(suffixIndex + 1);
            if ("jsonp".equals(suffix)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取用户访问地址
     * 
     * @param request
     * @return
     */
    public static String getClientAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        
        String str = request.getHeader("X-Forwarded-For");

        if (str == null || str.length() == 0 || ("unknown".equalsIgnoreCase(str))) {
            str = request.getHeader("X-Real-IP");
        }
        if (str == null || str.length() == 0 || "unknown".equalsIgnoreCase(str)) {
            str = request.getHeader("Proxy-Client-IP");
        }
        if (str == null || str.length() == 0 || "unknown".equalsIgnoreCase(str)) {
            str = request.getHeader("WL-Proxy-Client-IP");
        }
        if (str == null || str.length() == 0 || "unknown".equalsIgnoreCase(str)) {
            str = request.getHeader("HTTP_CLIENT_IP");
        }
        if (str == null || str.length() == 0 || "unknown".equalsIgnoreCase(str)) {
            str = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (str == null || str.length() == 0 || "unknown".equalsIgnoreCase(str)) {
            str = request.getRemoteAddr();
        }

        if (str != null && str.indexOf(",") != -1) {
            str = str.substring(str.lastIndexOf(",") + 1, str.length()).trim();
        }

        return str == null ? null : (str.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : str);
    }

    /**
     * 根据当前环境处理url(当前环境如果是线上预发环境则会在url前增加预发标志pre，如果是在灰度环境则会在url钱增加灰度标志gray)
     * 
     * @param url
     * @return
     */
    public static String environmentUrl(String url) {
        if (url == null) {
            return null;
        }

        ConfigPattern currentConfigPattern = ConfigManager.getCurrentConfigPattern();
        if (currentConfigPattern == ConfigPattern.PRE || currentConfigPattern == ConfigPattern.GRAY) {
            String domain = getDomain(url);
            String http = getHttp(url);
            String www = getWWW(url);
            String uri = getURI(domain, url);
            
            if (!uri.equals(url)) {
                url = http + www + currentConfigPattern.getPath() + domain + uri;
            }
        }
        
        return url;
    }
    
    private static String getDomain(String url) {
        String domain = null;
        Pattern p = Pattern.compile("(?<=http(s)?://)[^.].*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);

        if (matcher.find()) {
            domain = matcher.group();
        }

        if (domain == null) {
            domain = "";
        } else {
            String www = "www.";
            int index = domain.indexOf("www.");

            if (index != -1) {
                domain = domain.substring(index + www.length());
            }
        }

        return domain;
    }

    private static String getHttp(String url) {
        String http = "";

        if (url.indexOf("http://") != -1) {
            http = "http://";
        } else if (url.indexOf("https://") != -1) {
            http = "https://";
        }

        return http;
    }

    private static String getWWW(String url) {
        String www = "";

        if (url.indexOf("www.") != -1) {
            www = "www.";
        }

        return www;
    }

    private static String getURI(String domain, String url) {
        String uri = "";

        if (!StringUtil.isEmpty(domain)) {
            int index = url.indexOf(domain);

            if (index != -1) {
                uri = url.substring(index + domain.length());
            }
        } else {
            uri = url;
        }

        return uri;
    }

//    public static void main(String[] args) {
//        String url = "http://payment.berbon.com/trade/callback/sztfBalancePay";
//        String grayUrl = URLUtil.environmentUrl(url);
//
//        System.out.println(grayUrl);
//    }

}