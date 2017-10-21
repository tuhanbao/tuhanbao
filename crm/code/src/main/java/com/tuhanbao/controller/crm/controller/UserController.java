package com.tuhanbao.controller.crm.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tuhanbao.api.crm.constants.ErrorCode;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.api.crm.constants.enums.RoleType;
import com.tuhanbao.api.crm.model.crm.Permission;
import com.tuhanbao.api.crm.model.crm.Role;
import com.tuhanbao.api.crm.model.crm.User;
import com.tuhanbao.api.crm.service.crm.IPermissionService;
import com.tuhanbao.api.crm.service.crm.IRoleService;
import com.tuhanbao.api.crm.service.crm.IUserService;
import com.tuhanbao.base.Constants;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.base.util.db.table.data.BooleanValue;
import com.tuhanbao.base.util.encipher.MD5Util;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.web.controller.authority.IPermission;
import com.tuhanbao.web.controller.authority.PermissionManager;
import com.tuhanbao.web.controller.authority.TokenService;


@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    /**
     * 登陆
     */
    private static final String LOGIN = "login";
    /**
     * 添加用户
     */
    private static final String ADD_USER = "addUser";
    /**
     * 获取用户列表
     */
    private static final String GET_USER_LIST = "getUserList";
    /**
     * 获取用户详情
     */
    private static final String GET_USER_DETAIL = "getUserDetail";
    /**
     * 编辑用户
     */
    private static final String EDIT_USER = "editUser";
    /**
     * 编辑用户
     */
    private static final String MODIFY_PASSWORD = "modifyPassword";
    /**
     * 删除用户
     */
    private static final String DELETE_USER = "deleteUser";
    /**
     * 获取角色列表
     */
    private static final String GET_ROLE_LIST = "getRoleList";
    /**
     * 获取角色详情
     */
    private static final String GET_ROLE_DETAIL = "getRoleDetail";
    /**
     * 添加角色
     */
    private static final String ADD_ROLE = "addRole";
    /**
     * 编辑角色
     */
    private static final String EDIT_ROLE = "editRole";
    /**
     * 删除角色
     */
    private static final String DELETE_ROLE = "deleteRole";
    /**
     * 添加权限
     */
    private static final String ADD_PERMISSION = "addPermission";
    /**
     * 编辑权限
     */
    private static final String EDIT_PERMISSION = "editPermission";
    /**
     * 删除权限
     */
    private static final String DELETE_PERMISSION = "deletePermission";
    /**
     * 获取权限列表
     */
    private static final String LIST_PERMISSION = "listPermission";
    /**
     * 获取子权限列表
     */
    private static final String GET_CHILD_PERMISSION = "getChildPermission";
    /**
     * 获取子权限列表
     */
    private static final String GET_PERMISSION_DETAIL = "getPermissionDetail";

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;
    
    @Autowired
    private PermissionManager permissionManager;


    /**
     * TODO 这里为了防止攻击，应该还要后续加上验证码
     * 
     * @param request
     * @param response
     * @param password
     * @param loginName
     * @return
     */
    @RequestMapping(value = LOGIN)
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response, @RequestParam("password") String password, 
            @RequestParam("loginName") String loginName) {
        if (StringUtil.isEmpty(loginName) || StringUtil.isEmpty(password)) {
            throw new MyException(ErrorCode.LOGIN_NAME_PASSWORD_NOT_NULL);
        }
        User user = userService.login(loginName, password);
        Cookie cookie = new Cookie(TokenService.REQ_KEY_TOKEN, user.getToken());
        cookie.setPath("/");
        response.addCookie(cookie);
        return user;
    }

    @RequestMapping(value = ADD_USER)
    @ResponseBody
    public Object addUser(HttpServletRequest request, @RequestParam("account") String account, @RequestParam("password") String password,
            String mark, @RequestParam("roleId") long roleId, Long companyId) {
    	User user = (User)super.getCurrentUser(request);
    	
    	//开户权限校验。只有权限高的人才可以开权限低的用户
    	if (user.getRoleId() >= roleId) {
    		throw new MyException(ErrorCode.HAVE_NO_RIGHT);
    	}
    	
    	if (roleId <= RoleType.SYSTEM_ADMIN.value) {
    		companyId = 0L;
    	}
    	else {
    		if (user.getRoleId() < RoleType.SYSTEM_ADMIN.value) {
    			if (companyId == null) {
    				throw new MyException(ErrorCode.ILLEGAL_INCOMING_ARGUMENT);
    			}
    		}
    		else {
    			companyId = user.getCompanyId();
    		}
    	}
    	
        Filter filter = new Filter();
        filter.andFilter(TableConstants.T_USER.LOGIN_NAME, account);
        List<User> list = userService.select(filter);
        if (!list.isEmpty()) {
            throw new MyException(ErrorCode.ACCOUNT_IS_EXIST);
        }
        User newUser = new User();
        newUser.setLoginName(account);
        newUser.setPassword(MD5Util.getMD5String(password));
        newUser.setRoleId(roleId);
        newUser.setMark(mark);
        newUser.setCompanyId(companyId);
        userService.add(user);
        return NULL;
    }

    @RequestMapping(value = GET_USER_LIST)
    @ResponseBody
    public Object getUserList() {
        List<User> userList = userService.getUserList(null);
        for (User user : userList) {
            user.setPassword(Constants.EMPTY);
        }
        return userList;
    }

    @RequestMapping(value = GET_USER_DETAIL)
    @ResponseBody
    public Object getUserDetail(@RequestParam("userId") long userId) {
        List<User> userList = userService.getUserList(new Filter().andFilter(TableConstants.T_USER.ID, userId));
        if (userList.size() != 1) {
            throw new MyException(ErrorCode.USER_ISNOT_EXIST);
        }
        
        User user = userList.get(0);
        user.setPassword(Constants.EMPTY);
        return user;
    }

    @RequestMapping(value = EDIT_USER)
    @ResponseBody
    public Object editUser(HttpServletRequest request, @RequestParam("userId") long userId, 
            String account, String password, String mark, Long roleId) {
        User user = new User();
        user.setId(userId);
        if (!StringUtil.isEmpty(account)) {
            user.setLoginName(account);
        }
        if (!StringUtil.isEmpty(password)) {
            user.setPassword(MD5Util.getMD5String(password));
        }
        if (!StringUtil.isEmpty(mark)) {
            user.setMark(mark);
        }
        if (roleId != null) {
            user.setRoleId(roleId);
        }
        userService.updateSelective(user);
        return NULL;
    }
    
    @RequestMapping(value = MODIFY_PASSWORD)
    @ResponseBody
    public Object modifyPassword(HttpServletRequest request, @RequestParam("userId") long userId, 
            String password, String newPassword) {
        User user = userService.selectById(userId);
        if (user == null) {
            throw new MyException(ErrorCode.USER_ISNOT_EXIST);
        }
        
        if (MD5Util.getMD5String(password).equals(user.getPassword())) {
            user.setPassword(MD5Util.getMD5String(newPassword));
            userService.updateSelective(user);
            return NULL;
        }
        throw new MyException(ErrorCode.PASSWORD_WRONG);
    }

    @RequestMapping(value = DELETE_USER)
    @ResponseBody
    public Object deleteUser(HttpServletRequest request, @RequestParam("userId") long userId) {
        User user = userService.selectById(userId);
        if (user == null) {
            throw new MyException(ErrorCode.USER_ISNOT_EXIST);
        }
        userService.deleteById(userId);
        return NULL;
    }

    @RequestMapping(value = GET_ROLE_LIST)
    @ResponseBody
    public Object getRoleList() {
        List<Role> list = roleService.select(null);
        return list;
    }
    
    @RequestMapping(value = GET_ROLE_DETAIL)
    @ResponseBody
    public Object getRoleDetail(@RequestParam("roleId") long roleId) {
        Role role = roleService.selectById(roleId);
        if (role == null) {
            throw new MyException(ErrorCode.NO_RECORD_DATA);
        }
        List<IPermission> permissionList = permissionManager.getAllPermission(role.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("role", role);
        map.put("permission", permissionList);
        return map;
    }
    
    @RequestMapping(value = EDIT_ROLE)
    @ResponseBody
    public Object editRole(HttpServletRequest request, long roleId, 
            @RequestParam(value = "permissionIds", required = false) List<Long> permissionIds) {
        roleService.updatePermission(roleId, permissionIds);
        return NULL;
    }
    
    
    @RequestMapping(value = ADD_PERMISSION)
    @ResponseBody
    public Object addPermission(long parentId, int isMenu, String name, int sort, String url, String mark) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setParentId(parentId);
        permission.setIsMenu(BooleanValue.valueOf(isMenu).getValue());
        permission.setSort(sort);
        permission.setUrl(url);
        permission.setMark(mark);
        permissionService.add(permission);
        return NULL;
    }
    
    @RequestMapping(value = EDIT_PERMISSION)
    @ResponseBody
    public Object editPermission(long id, String name, Integer sort, String url, String mark) {
        Permission permission = new Permission();
        permission.setId(id);
        if (!StringUtil.isEmpty(name)) {
            permission.setName(name);
        }
        if (sort != null) {
            permission.setSort(sort);
        }
        if (url != null) {
            permission.setUrl(url);
        }
        if (mark != null) {
            permission.setMark(mark);
        }
        permissionService.updateSelective(permission);
        return NULL;
    }

    @RequestMapping(value = DELETE_PERMISSION)
    @ResponseBody
    public Object deletePermission(long permissionId) {
        Permission permission = permissionService.selectById(permissionId);
        if (permission == null) {
            throw new MyException(ErrorCode.NO_RECORD_DATA);
        }
        permissionManager.deletePermission(permission);
        return NULL;
    }

    @RequestMapping(value = LIST_PERMISSION)
    @ResponseBody
    public Object listPermission(Integer isContainBtn) {
        Filter filter = new Filter();
        if (isContainBtn != null && isContainBtn == BooleanValue.FALSE_VALUE) {
            filter.andFilter(TableConstants.T_PERMISSION.IS_MENU, true);
        }
        filter.addOrderField(TableConstants.T_PERMISSION.PARENT_ID);
        filter.addOrderField(TableConstants.T_PERMISSION.SORT);
        return permissionService.select(filter);
    }
    
    @RequestMapping(value = GET_CHILD_PERMISSION)
    @ResponseBody
    public Object getChildPermission(long permissionId, Integer isMenu) {
        Filter filter = new Filter().andFilter(TableConstants.T_PERMISSION.PARENT_ID, permissionId);
        if (isMenu != null) {
            filter.andFilter(TableConstants.T_PERMISSION.IS_MENU, isMenu);
        }
        filter.addOrderField(TableConstants.T_PERMISSION.SORT);
        return permissionService.select(filter);
    }
    
    @RequestMapping(value = GET_PERMISSION_DETAIL)
    @ResponseBody
    public Object getPermissionDetail(long id) {
        return permissionService.selectById(id);
    }
}
