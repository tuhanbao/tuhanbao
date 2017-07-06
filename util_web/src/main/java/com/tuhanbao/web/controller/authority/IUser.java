package com.tuhanbao.web.controller.authority;

public interface IUser {
    long getUserId();
    
    int getAuthority();

    void setToken(String token);
    
    String getToken();
}
