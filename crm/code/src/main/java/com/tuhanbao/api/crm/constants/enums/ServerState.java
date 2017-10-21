package com.tuhanbao.api.crm.constants.enums;

public enum ServerState {
    /**
     * 运行
     */
    RUNNING(1),

    /**
     * 欠费
     */
    ARREARAGE(2),

    /**
     * 维护
     */
    MAINTAINING(3);

    public final int value;

    private ServerState(int value) {
        this.value = value;
    }

    public static ServerState getServerState(int value) {
        for (ServerState temp : ServerState.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}