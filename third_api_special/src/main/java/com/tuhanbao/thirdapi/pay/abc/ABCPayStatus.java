package com.tuhanbao.thirdapi.pay.abc;

import com.tuhanbao.thirdapi.pay.FlowStatus;

public enum ABCPayStatus {
    // 成功
    SUCCESS("04", FlowStatus.SUCCESS),
    // 未支付
    NO_PAY("01", FlowStatus.UNKNOWN),
    // 无回应
    NO_RESPONSE("02", FlowStatus.UNKNOWN),
    // 已请款
    YIQINGKUAN("03", FlowStatus.UNKNOWN),
    // 已退款
    REFUND("05", FlowStatus.REFUND),
    // 授权确认成功
    AUDIT_PASS("07", FlowStatus.SUCCESS),
    // 授权已取消
    AUDIT_CANCEL("00", FlowStatus.SUCCESS),
    // 失败
    FAIL("99", FlowStatus.FAIL);

    private String code;

    private FlowStatus flowStatus;

    private ABCPayStatus(String code, FlowStatus flowStatus) {
        this.code = code;
        this.flowStatus = flowStatus;
    }

    public static ABCPayStatus getABCPayStatus(String code) {
        for (ABCPayStatus item : ABCPayStatus.values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public String getPlatFormResCode() {
        return code;
    }

    public FlowStatus getOurResCode() {
        return flowStatus;
    }
}
