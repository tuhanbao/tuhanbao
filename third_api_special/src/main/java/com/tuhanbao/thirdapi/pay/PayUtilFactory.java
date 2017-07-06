/**
 * 
 */
package com.tuhanbao.thirdapi.pay;

import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.thirdapi.pay.abc.ABCPayUtil;
import com.tuhanbao.thirdapi.pay.zhongjin.ZhongJinPayUtil;

/**
 * 2016年11月22日
 * @author liuhanhui
 */
public class PayUtilFactory {
    public static PayUtil getPayUtil(PayPlatform platform) {
        if (platform == PayPlatform.ABC) return ABCPayUtil.getInstance();
        else if (platform == PayPlatform.ZHONGJIN) return ZhongJinPayUtil.getInstance();
        throw new MyException("we donot support this pay type : " + platform.name());
    }
}
