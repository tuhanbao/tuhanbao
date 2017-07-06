package com.tuhanbao.web.controller.authority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.fastjson.JSON;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.json.JsonUtil;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.thirdapi.cache.CacheManager;
import com.tuhanbao.thirdapi.cache.ICacheKey;
import com.tuhanbao.thirdapi.cache.NoExpireCacheKey;

public class PermissionManager {
    
    private IPermissionManagerService rolePermissionService;
    
    private String regex;
    
    public static final String LOGIN = "[\\w\\W]*/login[\\w\\W]*";
    
    //不校验权限的url
    private String[] filterUrl;

    private AtomicBoolean hasInit = new AtomicBoolean(false);

    private static final ICacheKey PERMISSION_ROLE = new NoExpireCacheKey("permission_role");
    
    private static final ICacheKey PERMISSION_URL = new NoExpireCacheKey("permission_url");
    
    public PermissionManager() {
        
    }
    
    private void init() {
        if (rolePermissionService == null) return;
        
        List<IRole> roles = rolePermissionService.getAllRoles();
        List<IPermission> allPermissions = rolePermissionService.getAllPermissions();

        Map<Long, IPermission> allPermissionsMap = new HashMap<Long, IPermission>();
        for (IPermission item : allPermissions) {
            if (item != null) {
                allPermissionsMap.put(item.getId(), item);
            }
        }
        
        Map<String, List<Long>> urlRoles = new HashMap<String, List<Long>>();
        Map<Long, List<IPermission>> rolePermissions = new HashMap<Long, List<IPermission>>();
        
        for (IRole role : roles) {
            addRole2Url(urlRoles, role);
            addPermission2Role(rolePermissions, role, allPermissionsMap);
        }
        
        for (Entry<String, List<Long>> entry : urlRoles.entrySet()) {
            CacheManager.set(PERMISSION_URL, entry.getKey(), entry.getValue());
        }

        for (Entry<Long, List<IPermission>> entry : rolePermissions.entrySet()) {
            CacheManager.set(PERMISSION_ROLE, entry.getKey() + "", entry.getValue());
        }
    }
    
    private void addRole2Url(Map<String, List<Long>> urlRoles, IRole role) {
        List<IPermission> permissions = role.getPermissions();
        long roleId = role.getId();
        for (IPermission permission : permissions) {
            if (permission != null) {
                String url = permission.getUrl();
                if (!StringUtil.isEmpty(url)) {
                    addRole2Url(urlRoles, url, roleId);
                }
            }
        }
    }
    
    private void addRole2Url(Map<String, List<Long>> urlRoles, String url, long roleId) {
        if (!urlRoles.containsKey(url)) {
            urlRoles.put(url, new ArrayList<Long>());
        }
        
        urlRoles.get(url).add(roleId);
    }
    
    private void addPermission2Role(Map<Long, List<IPermission>> rolePermissions, IRole role, Map<Long, IPermission> allPermission) {
        List<IPermission> rps = role.getPermissions();
        long roleId = role.getId();
        for (IPermission permission : rps) {
            if (permission != null) {
                addPermission2Role(rolePermissions, roleId, permission);
            }
        }
    }
    
    private void addPermission2Role(Map<Long, List<IPermission>> rolePermissions, long roleId, IPermission permission) {
        if (!rolePermissions.containsKey(roleId)) {
            rolePermissions.put(roleId, new ArrayList<IPermission>());
        }
        
        List<IPermission> list = rolePermissions.get(roleId);
        Permission item = Permission.getPermission(permission);
        if (!list.contains(item)) {
            list.add(item);
        }
    }
    
    public List<Integer> getHasPermissionRole(String url) {
        Object o = CacheManager.get(PERMISSION_URL, url);
        if (o == null) return null;
        return JSON.parseArray(JsonUtil.toJSON(o).toString(), Integer.class);
    }

    public List<IPermission> getAllPermission(long roleId) {
        Object o = CacheManager.get(PERMISSION_ROLE, roleId + "");
        if (o == null) return null;
        List<Permission> result = JSON.parseArray(JsonUtil.toJSON(o).toString(), Permission.class);
        List<IPermission> list = new ArrayList<IPermission>();
        list.addAll(result);
        return list;
    }

    public List<IPermission> getAllPermission() {
        return this.rolePermissionService.getAllPermissions();
    }

    public IPermissionManagerService getRolePermissionService() {
        return rolePermissionService;
    }

    public synchronized void setRolePermissionService(IPermissionManagerService rolePermissionService) {
        if (this.rolePermissionService != null) return;
        
        this.rolePermissionService = rolePermissionService;
        check();
    }
    
    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex.replace("*", "[\\w\\W]*");
    }

    public void setFilterUrl(String wightList) {
        this.filterUrl = StringUtil.string2Array(wightList);
        for (int i = 0; i < filterUrl.length; i++) {
            filterUrl[i] = filterUrl[i].replace("*", "[\\w\\W]*");
        }
    }
    
    public boolean needCheckPermission(String url) {
        if (filterUrl != null) {
            for (String item : filterUrl) {
                if (url.matches(item)) {
                    return false;
                }
            }
        }
        else {
            //默认情况下，过滤登陆
            if (IS_LOGIN(url)) {
                return false;
            }
        }
        //默认需要过滤所有
        if (StringUtil.isEmpty(regex)) return true;
        return url.matches(regex);
    }

    public boolean IS_LOGIN(String url) {
        return url.matches(LOGIN);
    }

    private void check() {
        if (this.rolePermissionService == null) {
            throw new MyException("role permission service has not init");
        }
        
        if (!hasInit.get()) {
            init();
            hasInit.set(true);
        }
    }

    public void updatePermission(long roleId, List<IPermission> permissions) {
        List<IPermission> cachePermissions = getAllPermission(roleId);
        
        if (cachePermissions != null) {
            for (Iterator<IPermission> it = cachePermissions.iterator(); it.hasNext();) {
                IPermission cachePermission = it.next();
                boolean hasRemove = true;
                for (IPermission newPermission : permissions) {
                    if (cachePermission.getId() == newPermission.getId()) {
                        hasRemove = false;
                        permissions.remove(newPermission);
                        break;
                    }
                }
                if (hasRemove) {
                    it.remove();
                    removePermission(roleId, cachePermission);
                }
            }
        }
        
        cachePermissions = addPermission(roleId, cachePermissions, permissions);
        CacheManager.set(PERMISSION_ROLE, roleId + "", cachePermissions);
    }
    
    public void deleteRole(long roleId) {
        List<IPermission> cachePermissions = getAllPermission(roleId);
        
        if (cachePermissions != null) {
            for (Iterator<IPermission> it = cachePermissions.iterator(); it.hasNext();) {
                removePermission(roleId, it.next());
            }
        }
        
        CacheManager.delete(PERMISSION_ROLE, roleId + "");
    }
    
    public void deletePermission(IPermission permission) {
        if (permission == null) return;
        
        rolePermissionService.deletePermission(permission);
        String url = permission.getUrl();
        if (!StringUtil.isEmpty(url)) {
            CacheManager.delete(PERMISSION_URL, url);
        }
        
        for (IRole role : rolePermissionService.getAllRoles()) {
            long roleId = role.getId();
            rolePermissionService.removePermission(roleId, permission);
            List<IPermission> cachePermissions = getAllPermission(roleId);
            if (cachePermissions != null) {
                if (cachePermissions.remove(permission)) {
                    CacheManager.set(PERMISSION_ROLE, roleId + "", cachePermissions);
                }
            }
            
        }
    }
    
    /**
     * 批量新增权限
     * @param roleId
     * @param newPermissions
     */
    private List<IPermission> addPermission(long roleId, List<IPermission> cachePermissions, List<IPermission> newPermissions) {
        if (newPermissions == null) {
            throw new MyException(BaseErrorCode.ILLEGAL_INCOMING_ARGUMENT);
        }
        
        if (cachePermissions == null) {
            cachePermissions = new ArrayList<IPermission>();
        }
        
        for (IPermission newPermission : newPermissions) {
            cachePermissions.add(Permission.getPermission(newPermission));
            rolePermissionService.addPermission(roleId, newPermission);
            
            addRole2Url(roleId, newPermission);
        }

        
        return cachePermissions;
    }

    private void addRole2Url(long roleId, IPermission newPermission) {
        String url = newPermission.getUrl();
        if (!StringUtil.isEmpty(url)) {
            Object urlRoles = CacheManager.get(PERMISSION_URL, url);
            List<Long> roles = null;
            if (urlRoles != null) {
                roles = JSON.parseArray(urlRoles.toString(), Long.class);
            }
            
            boolean hasRole = false;
            if (roles == null) {
                roles = new ArrayList<Long>();
            }
            else {
                for (Long item : roles) {
                    if (item.equals(roleId)) {
                        hasRole = true;
                        break;
                    }
                }
            }
            
            if (!hasRole) {
                roles.add(roleId);
                CacheManager.set(PERMISSION_URL, url, roles);
                //不再增加rolePermission
            }
        }
    }
    
    private void removePermission(long roleId, IPermission permission) {
        rolePermissionService.removePermission(roleId, permission);
        
        removeRole2Url(roleId, permission);
    }

    private void removeRole2Url(long roleId, IPermission newPermission) {
        String url = newPermission.getUrl();
        if (!StringUtil.isEmpty(url)) {
            Object urlRoles = CacheManager.get(PERMISSION_URL, url);
            List<Long> roles = null;
            if (urlRoles != null) {
                roles = JSON.parseArray(urlRoles.toString(), Long.class);
            }
            
            if (roles != null) {
                if (roles.remove(roleId)) {
                    CacheManager.set(PERMISSION_URL, url, roles);
                }
            }
        }
    }
}