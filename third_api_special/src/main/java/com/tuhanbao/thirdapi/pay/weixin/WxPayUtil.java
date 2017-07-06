package com.tuhanbao.thirdapi.pay.weixin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.RandomUtil;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.thirdapi.pay.FlowStatus;
import com.tuhanbao.thirdapi.pay.PayResultBean;
import com.tuhanbao.thirdapi.pay.PayType;
import com.tuhanbao.thirdapi.pay.PayUtil;
import com.tuhanbao.thirdapi.pay.RefundBean;

public class WxPayUtil extends PayUtil {
    private static final Map<String, String> DEFAULT_VALUES = new HashMap<String, String>();

    private static final WxPayUtil INSTANCE = new WxPayUtil();

    static {
        // 如果忘记配置，以下属性会有默认值
        DEFAULT_VALUES.put(WeixinConstants.TRADE_TYPE, "APP");
        DEFAULT_VALUES.put(WeixinConstants.PACKAGE, "Sign=WXPay");
        DEFAULT_VALUES.put(WeixinConstants.PACKAGE, "Sign=WXPay");
        DEFAULT_VALUES.put(WeixinConstants.PACKAGE, "Sign=WXPay");
    }

    private WxPayUtil() {

    }

    public static WxPayUtil getInstance() {
        return INSTANCE;
    }

    /***
     * 微信支付
     * @param notifyUrl  回调URL
     * @param clientIp   Ip地址
     * @param outTradeNo 交易编号
     * @param body       购买的商品名字
     * @param totalFee   支付价格
     * @return
     */
    public WxPayBean pay(PayType payType, long orderNum, String orderDesc, long time, long total, Map<String, Object> args) {
        String nonceStr = nonceStr();
        SortedMap<String, Object> params = new TreeMap<String, Object>();
        setValue(params, WeixinConstants.APPID, args);
        setValue(params, WeixinConstants.MCH_ID, args);
        params.put(WeixinConstants.NONCE_STR, nonceStr);
        params.put(WeixinConstants.BODY, orderDesc);
        String flowNum = generateFlowNum(payType, orderNum, time);
        params.put(WeixinConstants.OUT_TRADE_NO, flowNum);
        params.put(WeixinConstants.TOTAL_FEE, total);
        setValue(params, WeixinConstants.NOTIFY_URL, args);
        setValue(params, WeixinConstants.TRADE_TYPE, args);
        params.put(WeixinConstants.SIGN, opSign(params));
        Map<String, Object> result = WxPayUtil.unifiedOrder(params, (String)getArgValue(WeixinConstants.PAY_URL, args));

        String returnCode = (String)result.get(WeixinConstants.RETURN_CODE);
        String resultCode = (String)result.get(WeixinConstants.RESULT_CODE);
        String prepayId = (String)result.get(WeixinConstants.PREPAY_ID);
        if (WeixinConstants.SUCCESS.equals(returnCode) && WeixinConstants.SUCCESS.equals(resultCode) && !StringUtil.isEmpty(prepayId)) {
            WxPayBean payBean = new WxPayBean(flowNum, null);
            payBean.setAppId((String)params.get(WeixinConstants.APPID));
            payBean.setMchId((String)params.get(WeixinConstants.MCH_ID));
            payBean.setPrepayId(prepayId);
            payBean.setPackageVal((String)getArgValue(WeixinConstants.PACKAGE, args));
            payBean.setNonceStr(nonceStr);
            payBean.setTime(time / 1000);

            SortedMap<String, Object> map = new TreeMap<String, Object>();
            map.put(WeixinConstants.APPID, payBean.getAppId());
            map.put(WeixinConstants.MCH_ID, payBean.getMchId());
            map.put(WeixinConstants.PREPAY_ID, prepayId);
            map.put(WeixinConstants.PACKAGE, payBean.getPackageVal());
            map.put(WeixinConstants.NONCESTR, nonceStr);
            map.put(WeixinConstants.TIME_STAMP, payBean.getTime());

            payBean.setSign(opSign(map));
            return payBean;
        }
        else {
            throw new MyException((String)result.get(WeixinConstants.RETURN_MSG));
        }
    }

    /***
     * 微信退款（需要配置正确的证书）
     * 
     * args需要传入的参数，原价
     * 
     * @param orderNum
     * @param totalFee
     * @param refundFee
     * @param tradeNum
     * @return
     * @throws Exception
     */
    public RefundBean refund(String flowNum, long refundFee, long time, Map<String, Object> args) {
        SortedMap<String, Object> params = new TreeMap<String, Object>();
        String nonceStr = WxPayUtil.nonceStr();
        setValue(params, WeixinConstants.APPID, args);
        setValue(params, WeixinConstants.MCH_ID, args);
        params.put(WeixinConstants.NONCE_STR, nonceStr);
        String generateBackFlowNum = generateBackFlowNum(flowNum);
        params.put(WeixinConstants.OUT_REFUND_NO, generateBackFlowNum);

        params.put(WeixinConstants.OUT_TRADE_NO, flowNum);
        params.put(WeixinConstants.TOTAL_FEE, args.get(WeixinConstants.TOTAL_FEE));
        params.put(WeixinConstants.REFUND_FEE, refundFee);
        setValue(params, WeixinConstants.MCH_ID, args);
        // op_user_id传入的mch_id
        params.put(WeixinConstants.OP_USER_ID, getArgValue(WeixinConstants.MCH_ID, args));
        params.put(WeixinConstants.SIGN, WxPayUtil.opSign(params));
        String xml = WxPayUtil.xml(params);

        StringBuilder msg = new StringBuilder("wx refund, flow num : ").append(flowNum);
        msg.append(", refundFee : ").append(refundFee).append(Constants.ENTER);
        try {
            String s = ClientCustomSSL.doRefund((String)getArgValue(WeixinConstants.REFUND_URL, args), xml);
            Map<String, Object> map = WxPayUtil.parseXml(new ByteArrayInputStream(s.getBytes(Constants.ISO)));
            String code = map.get(WeixinConstants.RETURN_CODE).toString();
            msg.append("return code : ").append(code);
            if (WeixinConstants.SUCCESS.equals(code)) {
                msg.append("  success!");
                LogManager.notify(msg);
                return new RefundBean(generateBackFlowNum);
            }
            msg.append("  fail!");
            throw new MyException("wx refund fail : " + code);
        }
        catch (Exception e) {
            msg.append("  fail!").append(Constants.ENTER);
            msg.append(e.getMessage());
            throw MyException.getMyException(e);
        }
    }

    public PayResultBean notifyCallBack(HttpServletRequest request, Map<String, Object> args) {
        Map<String, Object> map;
        try {
            map = parseXml(request.getInputStream());
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
        String orderflow = map.get(WeixinConstants.OUT_TRADE_NO).toString();
        return getPayResult(orderflow, args);
    }

    public PayResultBean getPayResult(String flowNum, Map<String, Object> args) {
        SortedMap<String, Object> params = new TreeMap<String, Object>();
        String nonceStr = nonceStr();
        setValue(params, WeixinConstants.APPID, args);
        setValue(params, WeixinConstants.MCH_ID, args);
        params.put(WeixinConstants.NONCE_STR, nonceStr);
        params.put(WeixinConstants.OUT_TRADE_NO, flowNum);
        params.put(WeixinConstants.SIGN, WxPayUtil.opSign(params));
        Map<String, Object> result = WxPayUtil.unifiedOrder(params, (String)getArgValue(WeixinConstants.QUERY_URL, null));

        String resultCode = result.get(WeixinConstants.RESULT_CODE).toString();
        boolean isSuccess = result.get(WeixinConstants.RETURN_CODE).toString().equals(WeixinConstants.SUCCESS)
                && resultCode.equals(WeixinConstants.SUCCESS)
                && result.get(WeixinConstants.TRADE_STATE).equals(WeixinConstants.SUCCESS);
        if (isSuccess) {
            return new PayResultBean(flowNum, WeixinConstants.SUCCESS, FlowStatus.SUCCESS);
        }
        else {
            return new PayResultBean(flowNum, resultCode, FlowStatus.FAIL);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> unifiedOrder(Map<String, Object> map, String url) {
        StringBuilder builder = new StringBuilder();
        builder.append("<xml>");
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> en = it.next();
            String key = en.getKey();
            String value = en.getValue().toString();
            builder.append("<").append(key).append("><![CDATA[").append(value).append("]]></").append(key).append(">");
        }
        builder.append("</xml>");
        String content = builder.toString();
        Map<String, Object> result = null;
        PostMethod method = new PostMethod(url);
        try {
            method.setRequestEntity(new StringRequestEntity(content, WeixinConstants.TEXT_XML, Constants.UTF_8));
            int status = getHttpClient().executeMethod(method);
            if (HttpStatus.SC_OK == status) {
                Document document = DocumentHelper.parseText(method.getResponseBodyAsString());
                result = new HashMap<String, Object>();
                List<Element> list = document.getRootElement().elements();
                for (Element element : list) {
                    result.put(element.getName(), element.getStringValue());
                }
            }
            else {
                result = new HashMap<String, Object>();
                result.put(WeixinConstants.RETURN_CODE, WeixinConstants.FAIL);
            }
        }
        catch (Exception e) {
            result = new HashMap<String, Object>();
            result.put(WeixinConstants.RETURN_CODE, WeixinConstants.FAIL);
            result.put(WeixinConstants.RETURN_MSG, e);
        }
        finally {
            method.releaseConnection();
        }
        return result;
    }

    private static HttpClient getHttpClient() {
        HttpClient client = new HttpClient();
        client.getParams().setContentCharset(Constants.UTF_8);
        client.getHttpConnectionManager().getParams()
                .setConnectionTimeout(Integer.valueOf((String)getArgValue(WeixinConstants.CONNECTION_TIMEOUT, null)));
        client.getHttpConnectionManager().getParams().setSoTimeout(Integer.valueOf((String)getArgValue(WeixinConstants.SO_TIMEOUT, null)));
        return client;
    }

    /**
     * 生成随机字符串
     * 
     * @return
     */
    private static String nonceStr() {
        return RandomUtil.randomLetterAndNumberString(Integer.valueOf((String)getArgValue(WeixinConstants.NONCE_STR_LENGTH, null)));
    }

    /**
     * 生成签名
     * 
     * @param map
     * @return
     */
    private static String opSign(Map<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> en = it.next();
            String key = en.getKey();
            if (!WeixinConstants.SIGN.equals(key)) {
                String value = en.getValue().toString();
                if (value != null && value.trim().length() != 0) builder.append(key).append(Constants.EQUAL).append(value).append(Constants.AND_MARK);
            }
        }
        builder.append(WeixinConstants.KEY).append(Constants.EQUAL).append(getArgValue(WeixinConstants.KEY, null));
        return DigestUtils.md5Hex(builder.toString()).toUpperCase();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseXml(InputStream str) throws Exception {
        // 解析结果存储在HashMap
        Map<String, Object> map = new HashMap<String, Object>();
        // InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(str);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getStringValue());

        // 释放资源
        str.close();

        return map;
    }

    /**
     * 转xml
     * 
     * @param map
     * @return
     */
    private static String xml(Map<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        builder.append("<xml>");
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> en = it.next();
            String key = en.getKey();
            String value = en.getValue().toString();
            builder.append("<").append(key).append("><![CDATA[").append(value).append("]]></").append(key).append(">");
        }
        builder.append("</xml>");
        String content = builder.toString();
        return content;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setValue(Map properties, String key, Map<String, Object> args) {
        properties.put(key, getArgValue(key, args));
    }

    private static Object getArgValue(String key, Map<String, Object> args) {
        if (args != null && args.containsKey(key)) return args.get(key);

        String value = WeixinConfig.getValue(key);
        // 为null证明未配置，为empty时代表是配置了的，所以必须以==null进行判断
        if (value == null) {
            value = getDefaultValue(key);
        }
        return value;
    }

    private static String getDefaultValue(String key) {
        return DEFAULT_VALUES.get(key);
    }

}
