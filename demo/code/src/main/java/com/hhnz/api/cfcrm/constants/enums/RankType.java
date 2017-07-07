package com.hhnz.api.cfcrm.constants.enums;

public enum RankType {
    /**
     * 当年排名
     */
    RANK(1),

    /**
     * 累计排名
     */
    RANK_ACC(2);

    public final int value;

    private RankType(int value) {
        this.value = value;
    }

    public static RankType getRankType(int value) {
        for (RankType temp : RankType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}