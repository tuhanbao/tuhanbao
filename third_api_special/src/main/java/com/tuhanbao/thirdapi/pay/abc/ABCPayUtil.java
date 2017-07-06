/*
 * Powered By [cfth]
 * 
 * 
 * Since 2010 - 2013
 */
package com.tuhanbao.thirdapi.pay.abc;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.TrxException;
import com.abc.trustpay.client.ebus.PaymentRequest;
import com.abc.trustpay.client.ebus.PaymentResult;
import com.abc.trustpay.client.ebus.QueryOrderRequest;
import com.abc.trustpay.client.ebus.RefundRequest;
import com.alibaba.fastjson.JSONObject;
import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.Base64Util;
import com.tuhanbao.base.util.objutil.NumberUtil;
import com.tuhanbao.base.util.objutil.TimeUtil;
import com.tuhanbao.thirdapi.pay.PayBean;
import com.tuhanbao.thirdapi.pay.PayResultBean;
import com.tuhanbao.thirdapi.pay.PayType;
import com.tuhanbao.thirdapi.pay.PayUtil;
import com.tuhanbao.thirdapi.pay.RefundBean;

import payment.tools.util.StringUtil;

/**
 * @author xiaokeheng email:xiaokeheng(a)gmail.com
 * @version 1.0
 * @since 1.0
 * @date - 2013-07-22
 */

public class ABCPayUtil extends PayUtil {

    private static final ABCPayUtil INSTANCE = new ABCPayUtil();
    
    //用于查询支付结果前的回调
    private static IABCCallBack abcCallBack = null;

    private ABCPayUtil() {

    }

    public static final ABCPayUtil getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public PayBean pay(PayType payType, long orderNum, String orderDesc, long time, long totalFee, Map<String, Object> args) {
        PaymentRequest tPaymentRequest = new PaymentRequest();
        Map properties = tPaymentRequest.dicOrder;
        setValue(properties, ABCConstants.PAY_TYPE_ID, args);

        String[] timeStr = TimeUtil.getTimeStr(TimeUtil.YYYY_MM_DD_HH_MM_SS, time).split(" ");
        // 设定订单日期 （必填 - // YYYY/MM/DD）
        properties.put(ABCConstants.ORDER_DATE, timeStr[0]);
        // 设定订单时间 （必填 - // HH:MM:SS）
        properties.put(ABCConstants.ORDER_TIME, timeStr[1]);

        setValue(properties, ABCConstants.CURRENCY_CODE, args);
        String flowNum = generateFlowNum(payType, orderNum, time);
        properties.put(ABCConstants.ORDER_NO, flowNum);
        // 设定交易金额
        properties.put(ABCConstants.ORDER_AMOUNT, NumberUtil.round(totalFee / 100d, 2));
        setValue(properties, ABCConstants.INSTALLMENT_MARK, args);
        setValue(properties, ABCConstants.COMMODITY_TYPE, args);

        // 设定订单说明 非必填
        properties.put(ABCConstants.ORDER_DESC, orderDesc);
        // 设定订单有效期
        properties.put(ABCConstants.ORDER_TIMEOUT_DATE, TimeUtil.getTimeStr("yyyyMMddHHmmss", time + TimeUtil.HOUR2MILLS * ABCConfig.ORDER_TIME_OUT_HOURS));
        
        properties = tPaymentRequest.dicRequest;
        /*
         * 支付方式 1：农行卡支付 2：国际卡支付 3：农行贷记卡支付 5：基于三方的跨行支付 6：银联跨行支付 7：对公户 A：支付方式合并
         */
        setValue(properties, ABCConstants.PAYMENT_TYPE, args);
        /*
         * 交易渠道 1：internet 网络接入 2：手机网络接入 3：数字电视网络接入 4：智能客户端
         */
        setValue(properties, ABCConstants.PAYMENT_LINKTYPE, args);

        /**
         * 通知方式 0:页面通知 1:服务器通知
         */
        setValue(properties, ABCConstants.NOTIFY_TYPE, args);
        // 设定通知地址
        setValue(properties, ABCConstants.RESULT_NOTIFY_URL, args);
        setValue(properties, ABCConstants.MERCHANT_REMARKS, args);
        setValue(properties, ABCConstants.IS_BREAK_ACCOUNT, args);

        int index = 1;
        if (args != null && args.containsKey(ABCConstants.CUSTOMER_ID)) {
            index = Integer.valueOf(args.get(ABCConstants.CUSTOMER_ID).toString());
        }
        JSON json = tPaymentRequest.extendPostRequest(index);

        String returnCode = json.GetKeyValue(ABCConstants.RETURN_CODE);
        String errorMessage = json.GetKeyValue(ABCConstants.ERROR_MESSAGE);
        if (ABCConstants.SUCCESS_CODE.equals(returnCode)) {
            return new PayBean(flowNum, json.GetKeyValue(ABCConstants.PAYMENT_URL));
        }
        else {
            throw new MyException(errorMessage);
        }
    }
    
    /**
     * 回调
     * 
     * @param flowNum
     *            流水号
     * @param args
     *            自定义参数
     * @return
     */
    public PayResultBean notifyCallBack(HttpServletRequest request, Map<String, Object> args) {
        String msg = request.getParameter("MSG");
        try {
            msg = Base64Util.encode(new String(Base64Util.decode(msg), Constants.GB2312).getBytes());
        }
        catch (UnsupportedEncodingException e) {
            throw MyException.getMyException(e);
        }

        String orderNum = null;
        try {
            PaymentResult tResult = new PaymentResult(msg);
            orderNum = tResult.getValue(ABCConstants.ORDER_NO);
            if (abcCallBack != null) {
                args = abcCallBack.callBackBeforeGetPayResult(args, tResult);
            }
        }
        catch (TrxException e) {
            throw MyException.getMyException(e);
        }
        return getPayResult(orderNum, args);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public PayResultBean getPayResult(String flowNum, Map<String, Object> args) {
        QueryOrderRequest tQueryRequest = new QueryOrderRequest();
        Map properties = tQueryRequest.queryRequest;
        // 设定交易类型
        setValue(properties, ABCConstants.PAY_TYPE_ID, args);
        // 设定订单编号 （必要信息）
        properties.put(ABCConstants.ORDER_NO, flowNum);
        // 设定查询方式
        setValue(properties, ABCConstants.QUERY_DETAIL, args);
        
        int index = 1;
        if (args != null && args.containsKey(ABCConstants.CUSTOMER_ID)) {
            index = Integer.valueOf(args.get(ABCConstants.CUSTOMER_ID).toString());
        }
        JSON json = tQueryRequest.extendPostRequest(index);
        
        // tQueryRequest.postRequest();
        String returnCode = json.GetKeyValue(ABCConstants.RETURN_CODE);
        if (ABCConstants.SUCCESS_CODE.equals(returnCode)) {
            String orderInfo = "";
            try {
                orderInfo = new String(Base64Util.decode(json.GetKeyValue(ABCConstants.ORDER)), Constants.GB2312);
            }
            catch (UnsupportedEncodingException e) {
                LogManager.error(e);
                throw MyException.getMyException(e);
            }

            JSONObject jsonObject = (JSONObject)JSONObject.parse(orderInfo);
            if (StringUtil.isEmpty(orderInfo)) {
                throw new MyException("no result！");
            }
            else {
                String status = jsonObject.getString(ABCConstants.STATUS);
                return handlerABCStatus(flowNum, status);
            }
        }
        else {
            throw new MyException("no result！");
        }
    }

    private static PayResultBean handlerABCStatus(String flowNum, String status) {
        ABCPayStatus abcStatus = ABCPayStatus.getABCPayStatus(status);
        if (abcStatus == null) throw new MyException("no result！");
        PayResultBean result = new PayResultBean(flowNum, abcStatus.getPlatFormResCode(), abcStatus.getOurResCode());
        return result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public RefundBean refund(String flowNum, long refundFee, long time, Map<String, Object> args) {
        String[] timeStr = TimeUtil.getTimeStr(TimeUtil.YYYY_MM_DD_HH_MM_SS, time).split(" ");
        // 1、生成退款请求对象
        RefundRequest tRequest = new RefundRequest();
        Map properties = tRequest.dicRequest;
        // 订单日期（必要信息）
        properties.put(ABCConstants.ORDER_DATE, timeStr[0]);
        // 订单时间（必要信息）
        properties.put(ABCConstants.ORDER_TIME, timeStr[1]);
        properties.put(ABCConstants.ORDER_NO, flowNum);
        properties.put(ABCConstants.NEW_ORDER_NO, generateBackFlowNum(flowNum)); // 交易编号（必要信息）
        setValue(properties, ABCConstants.CURRENCY_CODE, args);
        properties.put(ABCConstants.TRX_AMOUNT, NumberUtil.round(refundFee / 100d, 2));

        int index = 1;
        if (args != null && args.containsKey(ABCConstants.CUSTOMER_ID)) {
            index = Integer.valueOf(args.get(ABCConstants.CUSTOMER_ID).toString());
        }
        JSON json = tRequest.extendPostRequest(index);

        // 3、判断退款结果状态，进行后续操作
        String returnCode = json.GetKeyValue(ABCConstants.RETURN_CODE);
        String errorMessage = json.GetKeyValue(ABCConstants.ERROR_MESSAGE);
        if (ABCConstants.SUCCESS_CODE.equals(returnCode)) {
            return new RefundBean(json.GetKeyValue(ABCConstants.IRSPREF));
        }
        else {
            throw new MyException(errorMessage);
        }

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setValue(Map properties, String key, Map<String, Object> args) {
        properties.put(key, getArgValue(key, args));
    }

    private static Object getArgValue(String key, Map<String, Object> args) {
        if (args != null && args.containsKey(key)) return args.get(key);

        String value = ABCConfig.getValue(key);
        return value;
    }
    
    public static final void registerCallBack(IABCCallBack callBack) {
        abcCallBack = callBack;
    }
}
