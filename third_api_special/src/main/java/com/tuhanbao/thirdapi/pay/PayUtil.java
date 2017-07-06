/**
 * 
 */
package com.tuhanbao.thirdapi.pay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tuhanbao.base.util.config.ConfigManager;

/**
 * 2016年11月22日
 * @author liuhanhui
 */
public abstract class PayUtil {
    private static final String DEBUG_PAY_NO = "99";
    
    public static String generateFlowNum(PayType type, long orderNum, long timestamp) {
        String payId = type.getPayId() + "";
        if (ConfigManager.isDebug()) {
            payId = payId.substring(0, payId.length() - 2) + DEBUG_PAY_NO;
        }
        return payId + "" + orderNum + "" + timestamp;
    }

    public static String generateBackFlowNum(String flowNum) {
        return flowNum + "back";
    }
    
    /**
     * 支付
     * 
     * @param payType 内部支付类型
     * @param orderNum 订单号
     * @param orderDesc 订单描述
     * @param time 支付时间
     * @param total 支付额度，单位分
     * @param args 额外参数
     * @return 返回url
     */
    public abstract PayBean pay(PayType payType, long orderNum, String orderDesc, long time, long totalFee, Map<String, Object> args);

    /**
     * 查询支付结果
     * 
     * @param flowNum
     *            流水号
     * @param args
     *            自定义参数
     * @return
     */
    public abstract PayResultBean notifyCallBack(HttpServletRequest request, Map<String, Object> args);

    /**
     * 查询支付结果
     * @param flowNum 流水号
     * @param args 自定义参数
     * @return
     */
    public abstract PayResultBean getPayResult(String flowNum, Map<String, Object> args);

    /**
     * 退款
     * @param flowNum
     * @param refund
     * @param time
     * @param args 自定义参数
     * @return
     */
    public abstract RefundBean refund(String flowNum, long refundFee, long time, Map<String, Object> args);
}
