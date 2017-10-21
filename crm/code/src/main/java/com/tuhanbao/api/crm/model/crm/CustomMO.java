package com.tuhanbao.api.crm.model.crm;

import com.tuhanbao.base.dataservice.ICTBean;
import com.tuhanbao.base.dataservice.CTServiceBean;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.tuhanbao.api.crm.constants.enums.CustomState;
import java.util.List;
import com.tuhanbao.api.crm.constants.enums.Sex;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;

public class CustomMO extends CTServiceBean {
    protected CustomMO(ICTBean ctBean) {
        this(ctBean, new MetaObject(TableConstants.T_CUSTOM.TABLE));
    }

    protected CustomMO(ICTBean ctBean, MetaObject mo) {
        super(ctBean, mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_CUSTOM.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_CUSTOM.ID, LongValue.valueOf(value));
    }

    public String getName() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setName(String value) {
        setValue(TableConstants.T_CUSTOM.NAME, StringValue.valueOf(value));
    }

    public String getTelNum() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.TEL_NUM);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setTelNum(String value) {
        setValue(TableConstants.T_CUSTOM.TEL_NUM, StringValue.valueOf(value));
    }

    public CustomState getState() {
        IntValue value = (IntValue)getValue(TableConstants.T_CUSTOM.STATE);
        if (value == null) return null;
        else return CustomState.getCustomState(value.getValue());
    }

    public void setState(CustomState value) {
        setValue(TableConstants.T_CUSTOM.STATE, IntValue.valueOf(value.value));
    }

    public String getMark() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.MARK);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setMark(String value) {
        setValue(TableConstants.T_CUSTOM.MARK, StringValue.valueOf(value));
    }

    public long getOperatorId() {
        LongValue value = (LongValue)getValue(TableConstants.T_CUSTOM.OPERATOR_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setOperatorId(Long value) {
        setValue(TableConstants.T_CUSTOM.OPERATOR_ID, LongValue.valueOf(value));
    }

    public User getOperator() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_CUSTOM.OPERATOR_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public User removeOperator() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_CUSTOM.OPERATOR_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public void setOperator(User value) {
        this.setFKBean(TableConstants.T_CUSTOM.OPERATOR_ID, value);
    }

    public Sex getSex() {
        IntValue value = (IntValue)getValue(TableConstants.T_CUSTOM.SEX);
        if (value == null) return null;
        else return Sex.getSex(value.getValue());
    }

    public void setSex(Sex value) {
        setValue(TableConstants.T_CUSTOM.SEX, IntValue.valueOf(value.value));
    }

    public Date getBirthday() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_CUSTOM.BIRTHDAY);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setBirthday(Date value) {
        setValue(TableConstants.T_CUSTOM.BIRTHDAY, TimeValue.valueOf(value));
    }

    public String getDiyColumn1() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.DIY_COLUMN1);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn1(String value) {
        setValue(TableConstants.T_CUSTOM.DIY_COLUMN1, StringValue.valueOf(value));
    }

    public String getDiyColumn2() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.DIY_COLUMN2);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn2(String value) {
        setValue(TableConstants.T_CUSTOM.DIY_COLUMN2, StringValue.valueOf(value));
    }

    public String getDiyColumn3() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.DIY_COLUMN3);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn3(String value) {
        setValue(TableConstants.T_CUSTOM.DIY_COLUMN3, StringValue.valueOf(value));
    }

    public String getDiyColumn4() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.DIY_COLUMN4);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn4(String value) {
        setValue(TableConstants.T_CUSTOM.DIY_COLUMN4, StringValue.valueOf(value));
    }

    public String getDiyColumn5() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.DIY_COLUMN5);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn5(String value) {
        setValue(TableConstants.T_CUSTOM.DIY_COLUMN5, StringValue.valueOf(value));
    }

    public String getDiyColumn6() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.DIY_COLUMN6);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn6(String value) {
        setValue(TableConstants.T_CUSTOM.DIY_COLUMN6, StringValue.valueOf(value));
    }

    public String getDiyColumn7() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.DIY_COLUMN7);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn7(String value) {
        setValue(TableConstants.T_CUSTOM.DIY_COLUMN7, StringValue.valueOf(value));
    }

    public String getDiyColumn8() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.DIY_COLUMN8);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn8(String value) {
        setValue(TableConstants.T_CUSTOM.DIY_COLUMN8, StringValue.valueOf(value));
    }

    public String getDiyColumn9() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.DIY_COLUMN9);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn9(String value) {
        setValue(TableConstants.T_CUSTOM.DIY_COLUMN9, StringValue.valueOf(value));
    }

    public String getDiyColumn10() {
        StringValue value = (StringValue)getValue(TableConstants.T_CUSTOM.DIY_COLUMN10);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn10(String value) {
        setValue(TableConstants.T_CUSTOM.DIY_COLUMN10, StringValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<FollowRecord> getFollowRecords() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_FOLLOW_RECORD.CUSTOM_ID);
        return (List<FollowRecord>)result;
    }

    @SuppressWarnings("unchecked")
    public List<FollowRecord> removeFollowRecords() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_FOLLOW_RECORD.CUSTOM_ID);
        return (List<FollowRecord>)result;
    }

    public void setFollowRecords(List<FollowRecord> value) {
        this.setFKBean(TableConstants.T_FOLLOW_RECORD.CUSTOM_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<SmsRecord> getSmsRecords() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SMS_RECORD.CUSTOM_ID);
        return (List<SmsRecord>)result;
    }

    @SuppressWarnings("unchecked")
    public List<SmsRecord> removeSmsRecords() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SMS_RECORD.CUSTOM_ID);
        return (List<SmsRecord>)result;
    }

    public void setSmsRecords(List<SmsRecord> value) {
        this.setFKBean(TableConstants.T_SMS_RECORD.CUSTOM_ID, value);
    }

}