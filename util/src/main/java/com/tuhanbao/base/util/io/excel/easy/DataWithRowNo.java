package com.td.ca.base.util.io.excel.easy;

import com.td.ca.base.util.objutil.StringUtil;

public interface DataWithRowNo {
    void setRowNo(int row);

    default boolean isEmpty() {
        return false;
    };

    static boolean isAllEmpty(String... args) {
        if (args == null) {
            return true;
        }

        for (String arg : args) {
            if (!StringUtil.isEmptyTrim(arg)) {
                return false;
            }
        }
        return true;
    }
}
