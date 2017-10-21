package com.tuhanbao.web.controller.authority;

public interface IUser {
    long getUserId();
    
    /**
     * 这个方法主要用于粗权限控制，类似于用角色id控制权限
     * 
     * 如果使用细权限控制，此方法可任意返回，只要controller不适用AccessRequired即可
     * @return
     */
    int getAuthority();

    void setToken(String token);
    
    String getToken();
}
