/**
 * 
 */
package com.tuhanbao.thirdapi.pay;

/**
 * 2016年11月22日
 * @author liuhanhui
 */
public class PayBean {
    private String flowNum;

    private String payUrl;

    public PayBean(String flowNum, String payUrl) {
        super();
        this.flowNum = flowNum;
        this.payUrl = payUrl;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public String getFlowNum() {
        return this.flowNum;
    }
}
