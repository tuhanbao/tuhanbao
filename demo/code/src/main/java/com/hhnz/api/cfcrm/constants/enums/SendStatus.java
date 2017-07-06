package com.hhnz.api.cfcrm.constants.enums;

public enum SendStatus {
    /**
     * 已发送
     */
    SEND_SUCCESS(0),

    /**
     * 未发送
     */
    TO_SEND(1),

    /**
     * 发送失败
     */
    SEND_FAIL(2);

    public final int value;

    private SendStatus(int value) {
        this.value = value;
    }

    public static SendStatus getSendStatus(int value) {
        for (SendStatus temp : SendStatus.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}