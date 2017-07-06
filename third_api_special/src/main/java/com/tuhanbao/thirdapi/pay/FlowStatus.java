package com.tuhanbao.thirdapi.pay;

public enum FlowStatus {
    /**
     * 支付成功
     */
    SUCCESS(0),

    /**
     * 支付失败
     */
    FAIL(1),

    /**
     * 支付取消
     */
    CANCEL(2),

    /**
     * 退款成功
     */
    REFUND(3),

    /**
     * 未知
     */
    UNKNOWN(4);

    public final int value;

    private FlowStatus(int value) {
        this.value = value;
    }

    public static FlowStatus getFlowStatus(int value) {
        for (FlowStatus temp : FlowStatus.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}