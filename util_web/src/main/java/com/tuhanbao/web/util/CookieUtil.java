package com.tuhanbao.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tuhanbao.base.util.encipher.Encipher;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.IOUtil;
import com.tuhanbao.base.util.log.LogManager;

import payment.tools.util.StringUtil;

public class CookieUtil {

    /**
     * 得到当前request请求的所有cookie
     * 
     * @param request cookie数组
     * @return
     */
    public static Cookie[] getCookies(HttpServletRequest request) {
        return request == null ? null : request.getCookies();
    }

    /**
     * 根据cookie名字取得cookie
     * 
     * @param request
     * @param name
     * @return cookie对象
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = getCookies(request);
        if (StringUtil.isEmpty(name)) return null;

        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                String cookName = cookie.getName();

                if (name.equals(cookName)) {
                    return cookie;
                }
            }
        }

        return null;
    }

    /**
     * 根据cookie名字取得cookie的值
     * 
     * @param request
     * @param name
     * @return cookie的值
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        return getCookieValue(request, name, false);
    }

    /**
     * 根据cookie名字取得cookie的值(解密后的值)
     * 
     * @param request
     * @param name
     * @param isEncrypt 是否加过密
     * @return cookie的值
     */
    public static String getCookieValue(HttpServletRequest request, String name, boolean isEncrypt) {
        Cookie cookie = getCookie(request, name);
        try {
            if (cookie != null) {
                String value = cookie.getValue();
                if (isEncrypt) {
                    value = Encipher.decryptHttp(value);
                }
                value = URLDecoder.decode(value, IOUtil.DEFAULT_CHARSET);
                return value;
            }
        } catch (UnsupportedEncodingException e) {
            throw MyException.getMyException(e);
        }

        return null;
    }

    /**
     * 创建加密cookie(默认使用AES加密)
     * 
     * @param name
     * @param value
     * @param domain
     * @param path
     * @param secure
     * @param httpOnly
     * @param maxAge
     * @return
     */
    public static Cookie createCookie(String name, String value, String domain, String path, boolean secure, boolean httpOnly, int maxAge) {
        return createCookie(name, value, domain, path, secure, httpOnly, maxAge, false);
    }

    /**
     * 创建加密cookie(默认使用AES加密)
     * 
     * @param name
     * @param value
     * @param domain
     * @param path
     * @param secure
     * @param httpOnly
     * @param maxAge
     * @param isEncrypt 是否需要加密
     * @return
     */
    public static Cookie createCookie(String name, String value, String domain, String path, boolean secure, boolean httpOnly, int maxAge, boolean isEncrypt) {
        try {
            value = URLEncoder.encode(value, IOUtil.DEFAULT_CHARSET);
            if (isEncrypt) {
                value = Encipher.decryptHttp(value);
            }
            Cookie cookie = new Cookie(name, value);
            cookie.setDomain(domain);
            cookie.setPath(path);
            cookie.setSecure(secure);
            cookie.setHttpOnly(httpOnly);
            cookie.setMaxAge(maxAge);
            return cookie;
        } catch (UnsupportedEncodingException e) {
            throw MyException.getMyException(e);
        }
    }

    /**
     * 将cookie写入客户端
     * 
     * @param response
     * @param cookie 要写入的cookie
     */
    public static void writeCookie(HttpServletResponse response, Cookie cookie) {
        if (response != null) {
            response.addCookie(cookie);
        } else {
            LogManager.warn("cookie is null");
        }
    }

    /**
     * 删除客户端cookie
     * 
     * @param request
     * @param response
     * @param name cookie名称
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name, String domain, String path) {
        Cookie cookie = getCookie(request, name);

        if (cookie != null) {
            removeCookie(response, cookie, domain, path);
        }
    }

    /**
     * 删除客户端cookie
     * 
     * @param response
     * @param cookie cookie对象
     */
    public static void removeCookie(HttpServletResponse response, Cookie cookie, String domain, String path) {
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setDomain(domain);
            cookie.setPath(path);
            response.addCookie(cookie);
        } else {
            LogManager.warn("cookie is null");
        }
    }

}
