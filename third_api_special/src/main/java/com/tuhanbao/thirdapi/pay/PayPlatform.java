package com.tuhanbao.thirdapi.pay;

public enum PayPlatform {
    WECHAT(1), ABC(2), ZHONGJIN(3);

    public int value;

    private PayPlatform(int value) {
        this.value = value;
    }

    public static PayPlatform getPayPlatform(int value) {
        for (PayPlatform item : PayPlatform.values()) {
            if (item.value == value) return item;
        }
        return null;
    }
}
