package com.tuhanbao.api.crm.constants;

import com.tuhanbao.base.util.exception.BaseErrorCode;

public final class ErrorCode extends BaseErrorCode {
    /**
     * 短信发送异常
     */
    public static final int MSG_SEND_EXCEPTION = 10001;

    /**
     * 无效的企业简码
     */
    public static final int SHORT_NAME_INVALID = 10002;

    /**
     * 企业简码重复
     */
    public static final int COMPANY_SHORT_NAME_REPEAT = 10003;

    /**
     * 企业名称重复
     */
    public static final int COMPANY_NAME_REPEAT = 10004;

    /**
     * 存在下级权限，不能删除。
     */
    public static final int EXIST_CHILD_PERMISSION = 10005;

    /**
     * 登录名或密码不能为空
     */
    public static final int LOGIN_NAME_PASSWORD_NOT_NULL = 10006;

    /**
     * 角色已存在
     */
    public static final int ROLE_EXIST = 10007;

}