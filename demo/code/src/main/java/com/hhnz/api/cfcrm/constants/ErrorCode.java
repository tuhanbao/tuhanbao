package com.hhnz.api.cfcrm.constants;

import com.tuhanbao.base.util.exception.BaseErrorCode;

public final class ErrorCode extends BaseErrorCode {
    /**
     * 待发送短信用户超出阈值
     */
    public static final int VIP_MSG_OVERFLOW = 10001;

    /**
     * 短信发送异常
     */
    public static final int MSG_SEND_EXCEPTION = 10002;

    /**
     * 网信用户ID已经被占用
     */
    public static final int ID_IS_USED = 10003;

    /**
     * YEAR字段必传
     */
    public static final int YEAR_IS_NULL = 10004;

    /**
     * 不能在自动祝福启用30分钟内进行修改
     */
    public static final int CANNOT_UPDATE_BIRTH_MSG = 10005;

    /**
     * 当天的定时短信太多了！
     */
    public static final int AUTO_SEND_MSG_TOO_MUCH = 10006;

    /**
     * 预约时间不能小于当前时间
     */
    public static final int CANNOT_LESS_CURRENT_TIME = 10007;

    /**
     * 密码不能为空
     */
    public static final int LOGIN_NAME_PASSWORD_NOT_NULL = 10008;

    /**
     * 没有该角色
     */
    public static final int NO_ROLE = 10009;

    /**
     * 角色已经存在
     */
    public static final int ROLE_EXIST = 10010;

    /**
     * 缺少必要的字段
     */
    public static final int NECESSARY_FIELD_LOST = 10011;

    /**
     * 所传类型为空
     */
    public static final int AWARD_TYPE_IS_NULL = 10012;

    /**
     * 权限项不存在
     */
    public static final int PERMISSION_NOT_EXIST = 10013;

    /**
     * 存在子权限，无法删除
     */
    public static final int EXIST_CHILD_PERMISSION = 10014;

    /**
     * 导出错误
     */
    public static final int EXPORT_ERROR = 10015;

    /**
     * 前端所传省市id有错误
     */
    public static final int PRO_CITY_ID_ERROR = 10016;

    /**
     * 网络连接异常
     */
    public static final int NETWORK_LINK_ERROR = 10017;

}