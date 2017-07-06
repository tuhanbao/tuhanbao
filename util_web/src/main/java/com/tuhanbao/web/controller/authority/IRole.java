package com.tuhanbao.web.controller.authority;

import java.util.List;

public interface IRole {

    long getId();

    List<IPermission> getPermissions();
    
}
