package com.tuhanbao.web.controller.authority;

public interface IPermission {

    long getId();
    
    /**
     * controller的访问路径
     * @return
     */
    String getUrl();
    
    /**
     * 前端html的路径，用于动态生成菜单树
     * @return
     */
    String getHtml();
    
    /**
     * parent id
     * @return
     */
    long getParentId();
    
    /**
     * 是否是菜单
     * @return
     */
    boolean isMenu();
    
    /**
     * 名称，返回的是语言资源，需要通过语言资源获取
     * @return
     */
    String getName();
    
    /**
     * 排序，决定菜单展示的先后顺序
     * @return
     */
    int getSort();
    
    /**
     * 一些其他信息，自由扩展
     * @return
     */
    String getOtherInfo();
}
