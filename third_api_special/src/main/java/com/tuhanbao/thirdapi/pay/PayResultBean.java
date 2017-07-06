/**
 * 
 */
package com.tuhanbao.thirdapi.pay;

/**
 * 2016年11月22日
 * @author liuhanhui
 */
public class PayResultBean {

    private String resCode;

    private FlowStatus status;

    private String flowNum;

    public PayResultBean(String flowNum, String resCode, FlowStatus status) {
        this.flowNum = flowNum;
        this.resCode = resCode;
        this.status = status;
    }

    public String getPlatFormResCode() {
        return resCode;
    }

    public FlowStatus getOurResCode() {
        return status;
    }

    public String getFlowNum() {
        return flowNum;
    }
}
