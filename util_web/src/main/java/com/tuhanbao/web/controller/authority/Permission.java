package com.tuhanbao.web.controller.authority;

import com.alibaba.fastjson.annotation.JSONField;
import com.tuhanbao.base.util.db.table.data.BooleanValue;

public class Permission implements IPermission{

    private Long id;
    
    private String url;

    private String html;
    
    private Long parentId;
    
    private int isMenu;
    
    private String name;
    
    private Integer sort;
    
    private String otherInfo;
    
    public Permission() {
        
    }
    
    private Permission(IPermission iPermission) {
        this.id = iPermission.getId();
        this.url = iPermission.getUrl();
        this.html = iPermission.getHtml();
        this.parentId = iPermission.getParentId();
        this.isMenu = BooleanValue.getIntValue(iPermission.isMenu());
        this.name = iPermission.getName();
        this.sort = iPermission.getSort();
        this.otherInfo = iPermission.getOtherInfo();
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public static Permission getPermission(IPermission iPermission) {
        if (iPermission instanceof Permission) return (Permission)iPermission;
        return new Permission(iPermission);
    }
    
    public boolean equals(Object o) {
        if (o instanceof IPermission) {
            return ((IPermission)o).getId() == this.getId();
        }
        return false;
    }
    
    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public long getParentId() {
        return parentId;
    }

    @Override
    @JSONField(serialize=false)
    public boolean isMenu() {
        return isMenu == 1;
    }
    
    public int getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(int isMenu) {
        this.isMenu = isMenu;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSort() {
        return sort;
    }

    @Override
    public String getHtml() {
        return this.html;
    }
}
