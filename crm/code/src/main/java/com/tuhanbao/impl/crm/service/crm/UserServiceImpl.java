package com.tuhanbao.impl.crm.service.crm;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuhanbao.api.crm.constants.ErrorCode;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.api.crm.model.crm.User;
import com.tuhanbao.api.crm.service.crm.IUserService;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.base.util.encipher.MD5Util;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.web.controller.authority.TokenService;
import com.tuhanbao.web.filter.MyBatisSelector;

@Service("userService")
@Transactional("crmTransactionManager")
public class UserServiceImpl extends ServiceImpl<User> implements IUserService {
    private static final MyBatisSelector USER_ROLE_SELECTOR = new MyBatisSelector(TableConstants.T_USER.TABLE);
    
    static {
        USER_ROLE_SELECTOR.joinTable(TableConstants.T_ROLE.TABLE).joinTable(TableConstants.T_ROLE_PERMISSION.TABLE)
            .joinTable(TableConstants.T_PERMISSION.TABLE);
    }

    @Override
    public User login(String name, String password) {
        Filter filter = new Filter();
        filter.andFilter(TableConstants.T_USER.LOGIN_NAME, name);
        List<User> userLi = select(USER_ROLE_SELECTOR, filter);
        if (userLi.isEmpty()) {
            throw new MyException(ErrorCode.USER_ISNOT_EXIST);
        }
        User user = userLi.get(0);
        if (!MD5Util.getMD5String(password).equals(user.getPassword())) {
            throw new MyException(ErrorCode.PASSWORD_WRONG);
        }
        
        TokenService.getInstance().add(user);
        return user;
    }

    @Override
    public List<User> getUserList(Filter filter) {
        return select(USER_ROLE_SELECTOR, filter);
    }

    @Override
    public User getUserDetail(long userId) {
        Filter filter = new Filter();
        filter.andFilter(TableConstants.T_USER.ID, userId);
        List<User> userLi = select(USER_ROLE_SELECTOR, filter);
        if (userLi.isEmpty()) {
            throw new MyException(ErrorCode.USER_ISNOT_EXIST);
        }
        return userLi.get(0);
    }
}