package com.tuhanbao.api.crm.constants.enums;

public enum RoleType {
    /**
     * 超级管理员
     */
    ROOT(1),

    /**
     * 系统管理员
     */
    SYSTEM_ADMIN(5),

    /**
     * 公司管理员
     */
    COMPANY_ADMIN(20),

    /**
     * 业务经理
     */
    SERVICE_MANAGER(30),

    /**
     * 业务员
     */
    STAFF(50);

    public final int value;

    private RoleType(int value) {
        this.value = value;
    }

    public static RoleType getRoleType(int value) {
        for (RoleType temp : RoleType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}