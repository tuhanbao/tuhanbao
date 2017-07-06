package com.hhnz.api.cfcrm.constants.enums;

public enum SendType {
    /**
     * 立即发送
     */
    IMMEDIATE_SEND(0),

    /**
     * 预约发送
     */
    RESERVATION_SEND(1);

    public final int value;

    private SendType(int value) {
        this.value = value;
    }

    public static SendType getSendType(int value) {
        for (SendType temp : SendType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}