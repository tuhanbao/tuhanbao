package com.tuhanbao.impl.crm.service.crm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuhanbao.api.crm.constants.ErrorCode;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.api.crm.model.crm.Role;
import com.tuhanbao.api.crm.service.crm.IPermissionService;
import com.tuhanbao.api.crm.service.crm.IRolePermissionService;
import com.tuhanbao.api.crm.service.crm.IRoleService;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.web.controller.authority.IPermission;
import com.tuhanbao.web.controller.authority.IPermissionManagerService;
import com.tuhanbao.web.controller.authority.IRole;
import com.tuhanbao.web.controller.authority.PermissionManager;
import com.tuhanbao.web.filter.MyBatisSelector;

@Service("roleService")
@Transactional("crmTransactionManager")
public class RoleServiceImpl extends ServiceImpl<Role> implements IRoleService, IPermissionManagerService {
    
    @Autowired
    private IRolePermissionService rpService;

    @Autowired
    private IPermissionService permissionService;
    
    @Autowired
    private PermissionManager permissionManager;
    
    private static final MyBatisSelector SELECTOR = new MyBatisSelector(TableConstants.T_ROLE.TABLE);
    
    static {
        SELECTOR.joinTable(TableConstants.T_ROLE_PERMISSION.TABLE).joinTable(TableConstants.T_PERMISSION.TABLE);
    }
    
    @SuppressWarnings("unchecked")
    public List<IRole> getAllRoles() {
        List<?> roles = this.select(SELECTOR, null);
        return (List<IRole>)roles;
    }

    @SuppressWarnings("unchecked")
    public List<IPermission> getAllPermissions() {
        List<?> permissions = permissionService.select(null);
        return (List<IPermission>)permissions;
    }

    @Override
    public void addPermission(long roleId, IPermission permission) {
        rpService.addRolePermission(roleId, permission.getId());
    }

    @Override
    public void removePermission(long roleId, IPermission permission) {
        rpService.removeRolePermission(roleId, permission.getId());
    }
    
    public void add(Role role) {
    	throw new MyException(ErrorCode.HAVE_NO_RIGHT);
    }

    public int update(Role role) {
    	throw new MyException(ErrorCode.HAVE_NO_RIGHT);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void updatePermission(long roleId, List<Long> permissionIds) {        
        //为null表示不更新权限
        if (permissionIds == null) {
            return;
        }
        else {
            List<?> list = null;
            if (permissionIds.isEmpty()) {
                list = new ArrayList<IPermission>();
            }
            else {
                Filter filter = new Filter().andFilter(TableConstants.T_PERMISSION.ID, permissionIds);
                list = permissionService.select(filter);
            }
            permissionManager.updatePermission(roleId, (List<IPermission>)list);
        }
    }

    @Override
    public void deletePermission(IPermission permission) {
        permissionService.deletePermission(permission.getId());
    }

	@Override
	public void deleteRole(long roleId) {
		throw new MyException(ErrorCode.HAVE_NO_RIGHT);
	}
}