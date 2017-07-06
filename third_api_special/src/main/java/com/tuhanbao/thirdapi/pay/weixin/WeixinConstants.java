package com.tuhanbao.thirdapi.pay.weixin;

import java.util.HashMap;
import java.util.Map;

public class WeixinConstants {

    public static final String APPID = "appid";
    public static final String MCH_ID = "mch_id";
    public static final String NONCE_STR = "nonce_str";
    public static final String BODY = "body";
    public static final String OUT_TRADE_NO = "out_trade_no";
    public static final String TOTAL_FEE = "total_fee";

    public static final String TRADE_TYPE = "trade_type";
    public static final String SIGN = "sign";
    public static final String KEY = "key";

    public static final String PREPAY_ID = "prepay_id";
    public static final String NONCESTR = "noncestr";
    public static final String PACKAGE = "package";
    public static final String TIME_STAMP = "timestamp";

    public static final String RETURN_CODE = "return_code";
    public static final String RESULT_CODE = "result_code";
    public static final String RETURN_MSG = "return_msg";
    public static final String TRADE_STATE = "trade_state";

    public static final String OP_USER_ID = "op_user_id";
    public static final String OUT_REFUND_NO = "out_refund_no";
    public static final String REFUND_FEE = "refund_fee";

    public static final String PAY_URL = "pay_url";
    public static final String QUERY_URL = "query_Url";
    public static final String NOTIFY_URL = "notify_url";
    public static final String REFUND_URL = "refund_Url";
    public static final String NONCE_STR_LENGTH = "nonce_str_length";

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    public static final String PKCS12 = "PKCS12";
    public static final String TLSV1 = "TLSv1";
    public static final String TEXT_XML = "text/xml";

    public static final String CONNECTION_TIMEOUT = "connection_timeout";
    public static final String SO_TIMEOUT = "so_timeout";
    public static final String PROXY = "proxy";
	
	public static final Map<String, String> HEADER_PARAMS = new HashMap<>();
	static{
	    HEADER_PARAMS.put("Connection", "keep-alive");
	    HEADER_PARAMS.put("Accept", "*/*");
	    HEADER_PARAMS.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    HEADER_PARAMS.put("Host", "api.mch.weixin.qq.com");
	    HEADER_PARAMS.put("X-Requested-With", "XMLHttpRequest");
	    HEADER_PARAMS.put("Cache-Control", "max-age=0");
	    HEADER_PARAMS.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
	}
	
}
