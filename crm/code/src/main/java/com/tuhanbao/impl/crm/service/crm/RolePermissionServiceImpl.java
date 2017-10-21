package com.tuhanbao.impl.crm.service.crm;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.api.crm.model.crm.RolePermission;
import com.tuhanbao.api.crm.service.crm.IRolePermissionService;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.web.filter.MyBatisSelector;

@Service("rolePermissionService")
@Transactional("crmTransactionManager")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermission> implements IRolePermissionService {
	private static final MyBatisSelector AUTH_SELECTOR = new MyBatisSelector(TableConstants.T_ROLE_PERMISSION.TABLE);
    
    static {
        AUTH_SELECTOR.joinTable(TableConstants.T_PERMISSION.TABLE);
    }

    @Override
    public List<RolePermission> getAllAuthByRoleId(long roleId) {
        List<RolePermission> permisson = select(AUTH_SELECTOR, new Filter().andFilter(TableConstants.T_ROLE_PERMISSION.ROLE_ID, roleId));
        return permisson;
    }

    @Override
    public void addRolePermission(long roleId, long permissionId) {
        RolePermission rp = new RolePermission();
        rp.setRoleId(roleId);
        rp.setPermissionId(permissionId);
        this.add(rp);
    }

    @Override
    public void removeRolePermission(long roleId, long permissionId) {
        Filter filter = new Filter();
        filter.andFilter(TableConstants.T_ROLE_PERMISSION.ROLE_ID, roleId);
        filter.andFilter(TableConstants.T_ROLE_PERMISSION.PERMISSION_ID, permissionId);
        this.delete(filter);
    }
}