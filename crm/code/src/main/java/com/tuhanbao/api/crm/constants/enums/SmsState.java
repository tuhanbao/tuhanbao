package com.tuhanbao.api.crm.constants.enums;

public enum SmsState {
    /**
     * 已发送
     */
    SEND_SUCCESS(1),

    /**
     * 未发送
     */
    TO_SEND(2),

    /**
     * 发送失败
     */
    SEND_FAIL(3);

    public final int value;

    private SmsState(int value) {
        this.value = value;
    }

    public static SmsState getSmsState(int value) {
        for (SmsState temp : SmsState.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}