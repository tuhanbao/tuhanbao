package com.tuhanbao.thirdapi.sms;

public class AuthCodeBean {
    private String value;
    
    private long createTime;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

}
