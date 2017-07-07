package com.hhnz.api.cfcrm.constants.enums;

public enum MsgType {
    /**
     * 节日短信
     */
    FESTIVAL_MSG(1),

    /**
     * 生日短信
     */
    BIRTHDAY_MSG(2),

    /**
     * 定时短信
     */
    TIMER_MSG(3);

    public final int value;

    private MsgType(int value) {
        this.value = value;
    }

    public static MsgType getMsgType(int value) {
        for (MsgType temp : MsgType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}