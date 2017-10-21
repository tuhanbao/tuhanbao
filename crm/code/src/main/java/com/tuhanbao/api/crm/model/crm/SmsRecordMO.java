package com.tuhanbao.api.crm.model.crm;

import com.tuhanbao.base.dataservice.ICTBean;
import com.tuhanbao.base.dataservice.CTServiceBean;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.tuhanbao.api.crm.constants.enums.SmsState;

public class SmsRecordMO extends CTServiceBean {
    protected SmsRecordMO(ICTBean ctBean) {
        this(ctBean, new MetaObject(TableConstants.T_SMS_RECORD.TABLE));
    }

    protected SmsRecordMO(ICTBean ctBean, MetaObject mo) {
        super(ctBean, mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_SMS_RECORD.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_SMS_RECORD.ID, LongValue.valueOf(value));
    }

    public long getCustomId() {
        LongValue value = (LongValue)getValue(TableConstants.T_SMS_RECORD.CUSTOM_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCustomId(Long value) {
        setValue(TableConstants.T_SMS_RECORD.CUSTOM_ID, LongValue.valueOf(value));
    }

    public Custom getCustom() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SMS_RECORD.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (Custom)result.get(0);
    }

    public Custom removeCustom() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SMS_RECORD.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (Custom)result.get(0);
    }

    public void setCustom(Custom value) {
        this.setFKBean(TableConstants.T_SMS_RECORD.CUSTOM_ID, value);
    }

    public long getUserId() {
        LongValue value = (LongValue)getValue(TableConstants.T_SMS_RECORD.USER_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setUserId(Long value) {
        setValue(TableConstants.T_SMS_RECORD.USER_ID, LongValue.valueOf(value));
    }

    public User getUser() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SMS_RECORD.USER_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public User removeUser() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SMS_RECORD.USER_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public void setUser(User value) {
        this.setFKBean(TableConstants.T_SMS_RECORD.USER_ID, value);
    }

    public Date getSendDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_SMS_RECORD.SEND_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setSendDate(Date value) {
        setValue(TableConstants.T_SMS_RECORD.SEND_DATE, TimeValue.valueOf(value));
    }

    public String getSendContent() {
        StringValue value = (StringValue)getValue(TableConstants.T_SMS_RECORD.SEND_CONTENT);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setSendContent(String value) {
        setValue(TableConstants.T_SMS_RECORD.SEND_CONTENT, StringValue.valueOf(value));
    }

    public SmsState getState() {
        IntValue value = (IntValue)getValue(TableConstants.T_SMS_RECORD.STATE);
        if (value == null) return null;
        else return SmsState.getSmsState(value.getValue());
    }

    public void setState(SmsState value) {
        setValue(TableConstants.T_SMS_RECORD.STATE, IntValue.valueOf(value.value));
    }

}