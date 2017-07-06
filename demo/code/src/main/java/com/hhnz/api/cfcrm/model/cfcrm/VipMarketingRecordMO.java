package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.hhnz.api.cfcrm.constants.enums.FollowingStage;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;

public class VipMarketingRecordMO extends ServiceBean {
    protected VipMarketingRecordMO() {
        this(new MetaObject(TableConstants.T_VIP_MARKETING_RECORD.TABLE));
    }

    protected VipMarketingRecordMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_MARKETING_RECORD.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_VIP_MARKETING_RECORD.ID, LongValue.valueOf(value));
    }

    public long getCustomId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_MARKETING_RECORD.CUSTOM_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCustomId(Long value) {
        setValue(TableConstants.T_VIP_MARKETING_RECORD.CUSTOM_ID, LongValue.valueOf(value));
    }

    public VipPrimaryInfo getCustom() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_MARKETING_RECORD.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public VipPrimaryInfo removeCustom() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_MARKETING_RECORD.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public void setCustom(VipPrimaryInfo value) {
        this.setFKBean(TableConstants.T_VIP_MARKETING_RECORD.CUSTOM_ID, value);
    }

    public FollowingStage getFollowingStage() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_MARKETING_RECORD.FOLLOWING_STAGE);
        if (value == null) return null;
        else return FollowingStage.getFollowingStage(value.getValue());
    }

    public void setFollowingStage(FollowingStage value) {
        setValue(TableConstants.T_VIP_MARKETING_RECORD.FOLLOWING_STAGE, IntValue.valueOf(value.value));
    }

    public String getContents() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_MARKETING_RECORD.CONTENTS);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setContents(String value) {
        setValue(TableConstants.T_VIP_MARKETING_RECORD.CONTENTS, StringValue.valueOf(value));
    }

    public long getUserId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_MARKETING_RECORD.USER_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setUserId(Long value) {
        setValue(TableConstants.T_VIP_MARKETING_RECORD.USER_ID, LongValue.valueOf(value));
    }

    public User getUser() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_MARKETING_RECORD.USER_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public User removeUser() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_MARKETING_RECORD.USER_ID);
        return result == null || result.isEmpty() ? null : (User)result.get(0);
    }

    public void setUser(User value) {
        this.setFKBean(TableConstants.T_VIP_MARKETING_RECORD.USER_ID, value);
    }

    public long getEditorId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_MARKETING_RECORD.EDITOR_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setEditorId(Long value) {
        setValue(TableConstants.T_VIP_MARKETING_RECORD.EDITOR_ID, LongValue.valueOf(value));
    }

    public Date getCreateDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_MARKETING_RECORD.CREATE_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setCreateDate(Date value) {
        setValue(TableConstants.T_VIP_MARKETING_RECORD.CREATE_DATE, TimeValue.valueOf(value));
    }

    public Date getEditDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_MARKETING_RECORD.EDIT_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setEditDate(Date value) {
        setValue(TableConstants.T_VIP_MARKETING_RECORD.EDIT_DATE, TimeValue.valueOf(value));
    }

    public Date getFollowingDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_MARKETING_RECORD.FOLLOWING_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setFollowingDate(Date value) {
        setValue(TableConstants.T_VIP_MARKETING_RECORD.FOLLOWING_DATE, TimeValue.valueOf(value));
    }

}