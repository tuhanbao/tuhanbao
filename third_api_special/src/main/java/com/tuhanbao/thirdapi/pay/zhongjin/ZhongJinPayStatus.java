package com.tuhanbao.thirdapi.pay.zhongjin;

import com.tuhanbao.base.Constants;
import com.tuhanbao.thirdapi.pay.FlowStatus;

public enum ZhongJinPayStatus {
    // 成功
    SUCCESS(20, FlowStatus.SUCCESS),
    // 处理中
    DEALING(10, FlowStatus.UNKNOWN),
    // 失败
    FAIL(30, FlowStatus.FAIL);

    private int code;

    private FlowStatus flowStatus;

    private ZhongJinPayStatus(int code, FlowStatus flowStatus) {
        this.code = code;
        this.flowStatus = flowStatus;
    }

    public static ZhongJinPayStatus getZhongJinPayStatus(int code) {
        for (ZhongJinPayStatus item : ZhongJinPayStatus.values()) {
            if (item.code == code) {
                return item;
            }
        }
        return null;
    }

    public String getPlatFormResCode() {
        return code + Constants.EMPTY;
    }

    public FlowStatus getOurResCode() {
        return flowStatus;
    }
}
