package com.tuhanbao.thirdapi.pay.zhongjin;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.RandomUtil;
import com.tuhanbao.thirdapi.pay.PayBean;
import com.tuhanbao.thirdapi.pay.PayResultBean;
import com.tuhanbao.thirdapi.pay.PayType;
import com.tuhanbao.thirdapi.pay.PayUtil;
import com.tuhanbao.thirdapi.pay.RefundBean;

import payment.api.notice.Notice2566Request;
import payment.api.notice.NoticeRequest;
import payment.api.system.PaymentEnvironment;
import payment.api.tx.paymentbinding.Tx2512Request;
import payment.api.tx.paymentbinding.Tx2512Response;
import payment.api.tx.paymentbinding.Tx2565Request;

public class ZhongJinPayUtil extends PayUtil {

    private static final ZhongJinPayUtil INSTANCE = new ZhongJinPayUtil();
    
    private static IZhongjinCallBack CALLBACK = null;

    private ZhongJinPayUtil() {
        // 初始化支付环境
        try {
            PaymentEnvironment.initialize(ZhongjinConfig.PAYMENT_CONFIG_PATH);
        }
        catch (Exception e) {
            LogManager.error(e);
        }
    }


    public static final ZhongJinPayUtil getInstance() {
        return INSTANCE;
    }

    public static final String generateSNBindId(long userId) {
        return userId + "" + RandomUtil.randomLetterAndNumberString(10);
    }
    
    public static final void registerCallBack(IZhongjinCallBack callBack) {
        CALLBACK = callBack;
    }

    /**
     * 中金快捷支付注意事项：
     * 
     * 1.需要业务模块自行存储用户的bindId，通过args传进来 2.用户的bindId唯一不变。
     */
    @Override
    public PayBean pay(PayType payType, long orderNum, String orderDesc, long time, long total, Map<String, Object> args) {
        try {
            // 取得参数
            String institutionID = ZhongjinConfig.INSTITUTION_ID;
            // 创建交易请求对象
            Tx2565Request tx2565Request = new Tx2565Request();
            tx2565Request.setInstitutionID(institutionID);
            tx2565Request.setTxSNBinding(args.get(ZhongJinConstants.ZJ_BIND_ID).toString());
            String flowNum = generateFlowNum(payType, orderNum, time);
            tx2565Request.setPaymentNo(flowNum);
            tx2565Request.setAmount(total);
            tx2565Request.setSettlementFlag(ZhongJinConstants.SETTLEMENT_FLAG);

            // 执行报文处理
            tx2565Request.process();

            String message = URLEncoder.encode(tx2565Request.getRequestMessage(), Constants.UTF_8);
            return new ZjPayBean(flowNum, message, tx2565Request.getRequestSignature(), PaymentEnvironment.paymentURL);
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
    }

    @Override
    public PayResultBean notifyCallBack(HttpServletRequest request, Map<String, Object> args) {
        try {
            Notice2566Request notice = new Notice2566Request(getNoticeRequest(request).getDocument());
            
            PayResultBean result = getPayResultBean(notice.getPaymentNo(), notice.getStatus());
            if (CALLBACK != null) CALLBACK.callBack(result);
            return result;
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
    }

    @Override
    public PayResultBean getPayResult(String flowNum, Map<String, Object> args) {
        try {
            // 1.获取参数
            String institutionID = ZhongjinConfig.INSTITUTION_ID;

            // 创建交易请求对象
            Tx2512Request tx2512Request = new Tx2512Request();

            tx2512Request.setInstitutionID(institutionID);
            tx2512Request.setPaymentNo(flowNum);

            // 3.执行报文处理
            tx2512Request.process();
            Tx2512Response tx2512Response = new Tx2512Response(tx2512Request.getRequestMessage(), tx2512Request.getRequestSignature());
            
            PayResultBean result = getPayResultBean(flowNum, tx2512Response.getStatus());
            if (CALLBACK != null) CALLBACK.callBack(result);
            return result;
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
    }

    private PayResultBean getPayResultBean(String flowNum, int status) {
        ZhongJinPayStatus zjStatus = ZhongJinPayStatus.getZhongJinPayStatus(status);
        return new PayResultBean(flowNum, zjStatus.getPlatFormResCode(), zjStatus.getOurResCode());
    }

    @Override
    public RefundBean refund(String flowNum, long refundFee, long time, Map<String, Object> args) {
        throw new MyException("have not impl refund for zhongjin pay!");
    }

    private static NoticeRequest getNoticeRequest(HttpServletRequest request) throws Exception {
        // 获得参数message和signature
        String message = request.getParameter("message");
        String signature = request.getParameter("signature");
        return new NoticeRequest(message, signature);
    }
}
