package com.tuhanbao.thirdapi.sms;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.http.HttpUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.RandomUtil;
import com.tuhanbao.base.util.objutil.TimeUtil;
import com.tuhanbao.base.util.rm.ResourceManager;
import com.tuhanbao.thirdapi.cache.CacheKey;
import com.tuhanbao.thirdapi.cache.CacheManager;
import com.tuhanbao.thirdapi.cache.ICacheKey;

/**
 * 短信工具依赖于缓存
 * 
 * @author Administrator
 *
 */
public class SmsUtil {

    private static String KEY_USERNAME = "username";
    private static String KEY_PASSWORD = "password";
    private static String KEY_FROM = "from";
    private static String KEY_TO = "to";
    private static String KEY_CONTENT = "content";
    private static String KEY_EXPANDPREFIX = "expandPrefix";

    /**
     * telePhone为手机号
     * 如果多个手机号，用逗号分隔','  最多只支持100个（高斯通短信平台规定）
     * 
     * 业务模块请自行检查手机号长度及合法性，
     * 特别注意有些用户输入的手机号本身自带逗号
     * 
     * @param telePhone
     * @param content
     * @return
     */
    public static boolean sendMsg(String telePhone, String content) {
        Map<String, String> params = new HashMap<>();
        params.put(KEY_USERNAME, SmsConfig.USERNAME);
        params.put(KEY_PASSWORD, SmsConfig.PASSWORD);
        params.put(KEY_FROM, "");
        params.put(KEY_TO, telePhone);
        params.put(KEY_CONTENT, content);
        params.put(KEY_EXPANDPREFIX, "");
        String res = HttpUtil.get(SmsConfig.URL, params, Constants.GBK);

        StringBuilder msg = new StringBuilder();
        msg.append("send sms msg to ").append(telePhone).append(" : ").append(content).append(Constants.ENTER);
        msg.append("result : ").append(res);
        LogManager.info(msg);

        // 验证规则由第三方短信接口决定，没要提成公用变量
        boolean isSuccess = res != null && res.contains(":") && res.split(":").length > 1 && res.split(":")[0].equals("OK");
        return isSuccess;
    }

    /**
     * 发送验证码专用
     * 
     * @param telePhone
     * @return
     */
    public static String sendAuthCode(String telePhone, String msg) {
        AuthCodeBean acb = getCachedCodeBean(telePhone);
        if (acb != null) {
            // 超过一分钟就可以重发
            if (TimeUtil.isOverdue(acb.getCreateTime(), SmsConfig.AUTHCODE_GETAGAIN)) {
                return sendAuthCodeDirect(telePhone, msg);
            }
            else {
                throw new MyException(BaseErrorCode.AUTHCODE_SEND_TOO_MANY);
            }
        }
        else {
            return sendAuthCodeDirect(telePhone, msg);
        }
    }

    private static String sendAuthCodeDirect(String telePhone, String msg) {
        AuthCodeBean acb;
        String authCode = RandomUtil.randomString(6, false, true);
        String content = ResourceManager.replaceArgs(msg, authCode, (SmsConfig.AUTHCODE_DEADLINE / TimeUtil.MIN2SEC) + "");
        if (sendMsg(telePhone, content)) {
            acb = new AuthCodeBean();
            acb.setValue(authCode);
            acb.setCreateTime(new Date().getTime());
            CacheManager.set(CacheKey.AUTHCODE, telePhone, acb);
            return authCode;
        }
        else {
            throw new MyException(BaseErrorCode.AUTHCODE_GET_FAIL);
        }
    }

    public static void validateCode(String telePhone, String authCode) {
        AuthCodeBean acb = CacheManager.get(CacheKey.AUTHCODE, telePhone, AuthCodeBean.class);
        if (acb == null) {
            throw new MyException(BaseErrorCode.RESEND_AUTHCODE);
        }
        
        if (TimeUtil.isOverdue(acb.getCreateTime(), SmsConfig.AUTHCODE_DEADLINE)) {
            LogManager.debug("auth code is overdue : " + telePhone);
            removeCode(CacheKey.AUTHCODE, telePhone);
            throw new MyException(BaseErrorCode.AUTHCODE_IS_EXPIRE);
        }
        if (!acb.getValue().equals(authCode)) {
            LogManager.debug("validate auth code fail : " + telePhone + ", " + authCode);
            throw new MyException(BaseErrorCode.AUTHCODE_VAL_FAIL);
        }
        
        LogManager.debug("validate auth code success : " + telePhone);
        removeCode(CacheKey.AUTHCODE, telePhone);
    }

    private static void removeCode(ICacheKey ck, String telePhone) {
        CacheManager.delete(ck, telePhone);
    }

    private static AuthCodeBean getCachedCodeBean(String telePhone) {
        if (!CacheManager.isExist(CacheKey.AUTHCODE, telePhone)) {
            return null;
        }
        AuthCodeBean acb = CacheManager.get(CacheKey.AUTHCODE, telePhone, AuthCodeBean.class);
        if (TimeUtil.isOverdue(acb.getCreateTime(), SmsConfig.AUTHCODE_DEADLINE)) {
            removeCode(CacheKey.AUTHCODE, telePhone);
            return null;
        }
        else {
            return acb;
        }
    }
}
