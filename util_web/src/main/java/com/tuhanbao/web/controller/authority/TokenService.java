package com.tuhanbao.web.controller.authority;

import javax.servlet.http.HttpServletRequest;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.encipher.EncipherUtil;
import com.tuhanbao.base.util.encipher.EncipherType;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.objutil.TimeUtil;
import com.tuhanbao.thirdapi.cache.CacheKey;
import com.tuhanbao.thirdapi.cache.CacheManager;

public class TokenService {
    
    private static TokenService instance = new TokenService();

    public static String REQ_KEY_TOKEN = "token";
    
    public static TokenService getInstance() {
        return instance;
    }

    public static void registerTokenService(TokenService tokenService) {
        TokenService.instance = tokenService;
    }

    public String newToken(IUser user) {
        String token = user.getAuthority() + Constants.UNDER_LINE + getUserId(user) + Constants.UNDER_LINE + TimeUtil.nowStr();
        return EncipherUtil.encrypt(EncipherType.SELF, token);
    }

    protected String getUserIdByToken(String token) {
        int startIndex = token.indexOf(Constants.UNDER_LINE);
        int endIndex = token.lastIndexOf(Constants.UNDER_LINE);
        return token.substring(startIndex + 1, endIndex);
    }

    public IUser getUserByToken(String token, Class<? extends IUser> clazz) {
        if (StringUtil.isEmpty(token)) return null;
        String userId = getUserIdByToken(EncipherUtil.decrypt(EncipherType.SELF, token));
        IUser user = CacheManager.get(CacheKey.TOKEN, userId, clazz);
        if (user == null || !token.equals(user.getToken())) {
            return null;
        }
        return user;
    }
    
    public String getUserId(IUser user) {
        return user.getUserId() + "";
    }

    public boolean add(IUser user) {
        String token = newToken(user);
        user.setToken(token);
        CacheManager.set(CacheKey.TOKEN, getUserId(user), user);
        return true;
    }

    public void update(IUser user) {
        CacheManager.set(CacheKey.TOKEN, getUserId(user), user);
        return;
    }

    public boolean delete(IUser user) {
        CacheManager.delete(CacheKey.TOKEN, getUserId(user));
        return true;
    }

    public IUser getCurrentUser(HttpServletRequest request) {
        return getUserByToken(getToken(request));
    }

    public IUser getUserByToken(String token) {
        throw new MyException("please reload this method in you impl class!");
    }
    
    protected String getToken(HttpServletRequest request) {
        return request.getHeader(REQ_KEY_TOKEN);
    }
}
