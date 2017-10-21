/**
 * 
 */
package {projectHead}.controller.{projectName}.controller;

import javax.servlet.http.HttpServletRequest;

import com.tuhanbao.api.crm.model.crm.Company;
import com.tuhanbao.api.crm.model.crm.User;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.web.controller.authority.IUser;
import com.tuhanbao.web.controller.authority.TokenService;
import com.tuhanbao.web.controller.helper.JsonHttpMessageConverter;

/**
 * 2016年10月24日
 * @author liuhanhui
 */
public class BaseController {
    public static final String GET = "/get";
    
    public static final String UPDATE = "/update";
    
    public static final String ADD = "/add";
    
    public static final String DELETE = "/delete";
    
    protected static final Object NULL = JsonHttpMessageConverter.NULL;
    
    protected static final int ADMIN = 1;
    
    public IUser getCurrentUser(HttpServletRequest request){
        TokenService instance = TokenService.getInstance();
        IUser user = null;
        if (instance != null) {
            user = instance.getCurrentUser(request);
        }
        if (user == null) throw new MyException(BaseErrorCode.PLEASE_LOGIN_FIRST);
        return user;
    }
    
    public Company getCompany(HttpServletRequest request){
        User user = (User) getCurrentUser(request);
        return user.getCompany();
    }
    
}
