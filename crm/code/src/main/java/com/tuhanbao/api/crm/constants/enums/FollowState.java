package com.tuhanbao.api.crm.constants.enums;

public enum FollowState {
    /**
     * 初步跟进
     */
    FOLLOW_UP(1),

    /**
     * 深度谈判
     */
    DEEP_NEGOTIATION(2),

    /**
     * 谈判成功
     */
    NEGOTIATING_SUCCESS(3);

    public final int value;

    private FollowState(int value) {
        this.value = value;
    }

    public static FollowState getFollowState(int value) {
        for (FollowState temp : FollowState.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}