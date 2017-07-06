package com.hhnz.api.cfcrm.constants.enums;

public enum FollowingStage {
    /**
     * 初步跟进
     */
    FOLLOW_UP(0),

    /**
     * 深度谈判
     */
    DEEP_NEGOTIATION(1),

    /**
     * 谈判成功
     */
    NEGOTIATING_SUCCESS(2);

    public final int value;

    private FollowingStage(int value) {
        this.value = value;
    }

    public static FollowingStage getFollowingStage(int value) {
        for (FollowingStage temp : FollowingStage.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}