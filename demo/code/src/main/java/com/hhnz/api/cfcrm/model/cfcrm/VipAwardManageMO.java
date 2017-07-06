package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.List;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.hhnz.api.cfcrm.constants.enums.SendStatus;
import com.hhnz.api.cfcrm.constants.enums.SendType;

public class VipAwardManageMO extends ServiceBean {
    protected VipAwardManageMO() {
        this(new MetaObject(TableConstants.T_VIP_AWARD_MANAGE.TABLE));
    }

    protected VipAwardManageMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_AWARD_MANAGE.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_VIP_AWARD_MANAGE.ID, LongValue.valueOf(value));
    }

    public String getAwardName() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_AWARD_MANAGE.AWARD_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setAwardName(String value) {
        setValue(TableConstants.T_VIP_AWARD_MANAGE.AWARD_NAME, StringValue.valueOf(value));
    }

    public long getUserId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_AWARD_MANAGE.USER_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setUserId(Long value) {
        setValue(TableConstants.T_VIP_AWARD_MANAGE.USER_ID, LongValue.valueOf(value));
    }

    public User getUser() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_AWARD_MANAGE.USER_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public User removeUser() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_AWARD_MANAGE.USER_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public void setUser(User value) {
        this.setFKBean(TableConstants.T_VIP_AWARD_MANAGE.USER_ID, value);
    }

    public Date getCreatingTime() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_AWARD_MANAGE.CREATING_TIME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setCreatingTime(Date value) {
        setValue(TableConstants.T_VIP_AWARD_MANAGE.CREATING_TIME, TimeValue.valueOf(value));
    }

    public Date getAwardDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_AWARD_MANAGE.AWARD_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setAwardDate(Date value) {
        setValue(TableConstants.T_VIP_AWARD_MANAGE.AWARD_DATE, TimeValue.valueOf(value));
    }

    public SendStatus getSendStatus() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_AWARD_MANAGE.SEND_STATUS);
        if (value == null) return null;
        else return SendStatus.getSendStatus(value.getValue());
    }

    public void setSendStatus(SendStatus value) {
        setValue(TableConstants.T_VIP_AWARD_MANAGE.SEND_STATUS, IntValue.valueOf(value.value));
    }

    public SendType getSendType() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_AWARD_MANAGE.SEND_TYPE);
        if (value == null) return null;
        else return SendType.getSendType(value.getValue());
    }

    public void setSendType(SendType value) {
        setValue(TableConstants.T_VIP_AWARD_MANAGE.SEND_TYPE, IntValue.valueOf(value.value));
    }

    @SuppressWarnings("unchecked")
    public List<AwardItem> getAwardItems() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_AWARD_ITEM.AWARD_ID);
        return (List<AwardItem>)result;
    }

    @SuppressWarnings("unchecked")
    public List<AwardItem> removeAwardItems() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_AWARD_ITEM.AWARD_ID);
        return (List<AwardItem>)result;
    }

    public void setAwardItems(List<AwardItem> value) {
        this.setFKBean(TableConstants.T_AWARD_ITEM.AWARD_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<AwardVip> getAwardVips() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_AWARD_VIP.AWARD_ID);
        return (List<AwardVip>)result;
    }

    @SuppressWarnings("unchecked")
    public List<AwardVip> removeAwardVips() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_AWARD_VIP.AWARD_ID);
        return (List<AwardVip>)result;
    }

    public void setAwardVips(List<AwardVip> value) {
        this.setFKBean(TableConstants.T_AWARD_VIP.AWARD_ID, value);
    }

}