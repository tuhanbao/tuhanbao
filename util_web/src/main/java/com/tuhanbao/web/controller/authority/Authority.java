package com.tuhanbao.web.controller.authority;

import com.tuhanbao.base.util.exception.MyException;

/**
 * 权限项
 * 
 * 只能通过newAuthority和add方法构造权限项
 * 
 * 根据0,1判断权限项
 * 
 * @author Administrator
 *
 */
public class Authority {
    public static int NULL = 0;
    
    private Authority() {
    }
    
    public static int getAuthorityValue(int index) {
        if (index < 0 || index > 32) throw new MyException("just support 0 ~ 32");
        if (index == 0) return NULL;
        
        return 1 << (index - 1);
    }
    
    public static int[] getAuthorityValueByIndex(int[] values) {
        int length = values.length;
        for (int i = 0; i < length; i++) {
            values[i] = getAuthorityValue(values[i]);
        }
        return values;
    }
    
    public static int getAuthorityValue(Enum<?> e) {
        return getAuthorityValue(e.ordinal() + 1);
    }

    public static int add(int ... authoritys) {
        if (authoritys == null || authoritys.length == 0) return NULL;
        int value = 0;
        for (int item : authoritys) {
            value = value | item;
        }
        return value;
    }

    public static boolean hasPower(int authority, int ... authoritys) {
        return hasPower(authority, true, authoritys);
    }
    
    /**
     * 是或权限还是且权限
     * @param authoritys
     * @param isOr
     * @return
     */
    public static boolean hasPower(int authority, boolean isOr, int ... authoritys) {
        if (authoritys == null || authoritys.length == 0) return true;

        if (isOr) {
            for (int authorityItem : authoritys) {
                if ((authority & authorityItem) == authorityItem) {
                    return true;
                }
            }
            return false;
        }
        else {
            int value = 0;
            for (int authorityItem : authoritys) {
                value |= authorityItem;
            }
            return (authority & value) == value;
        }
    }
}
