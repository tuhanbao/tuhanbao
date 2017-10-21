package com.tuhanbao.api.crm.model.crm;

import com.tuhanbao.web.controller.authority.IUser;

public class User extends UserMO implements IUser {
	private String token;
    
    public User() {

    }

    @Override
    public long getUserId() {
        return this.getId();
    }

    @Override
    public int getAuthority() {
        return (int)this.getRoleId();
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return this.token;
    }

}