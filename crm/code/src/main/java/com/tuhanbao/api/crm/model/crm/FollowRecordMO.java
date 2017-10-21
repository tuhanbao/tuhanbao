package com.tuhanbao.api.crm.model.crm;

import com.tuhanbao.base.dataservice.ICTBean;
import com.tuhanbao.base.dataservice.CTServiceBean;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.tuhanbao.api.crm.constants.enums.FollowState;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;

public class FollowRecordMO extends CTServiceBean {
    protected FollowRecordMO(ICTBean ctBean) {
        this(ctBean, new MetaObject(TableConstants.T_FOLLOW_RECORD.TABLE));
    }

    protected FollowRecordMO(ICTBean ctBean, MetaObject mo) {
        super(ctBean, mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_FOLLOW_RECORD.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_FOLLOW_RECORD.ID, LongValue.valueOf(value));
    }

    public long getCustomId() {
        LongValue value = (LongValue)getValue(TableConstants.T_FOLLOW_RECORD.CUSTOM_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCustomId(Long value) {
        setValue(TableConstants.T_FOLLOW_RECORD.CUSTOM_ID, LongValue.valueOf(value));
    }

    public Custom getCustom() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_FOLLOW_RECORD.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (Custom)result.get(0);
    }

    public Custom removeCustom() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_FOLLOW_RECORD.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (Custom)result.get(0);
    }

    public void setCustom(Custom value) {
        this.setFKBean(TableConstants.T_FOLLOW_RECORD.CUSTOM_ID, value);
    }

    public FollowState getState() {
        IntValue value = (IntValue)getValue(TableConstants.T_FOLLOW_RECORD.STATE);
        if (value == null) return null;
        else return FollowState.getFollowState(value.getValue());
    }

    public void setState(FollowState value) {
        setValue(TableConstants.T_FOLLOW_RECORD.STATE, IntValue.valueOf(value.value));
    }

    public String getMark() {
        StringValue value = (StringValue)getValue(TableConstants.T_FOLLOW_RECORD.MARK);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setMark(String value) {
        setValue(TableConstants.T_FOLLOW_RECORD.MARK, StringValue.valueOf(value));
    }

    public Date getFollowTime() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_FOLLOW_RECORD.FOLLOW_TIME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setFollowTime(Date value) {
        setValue(TableConstants.T_FOLLOW_RECORD.FOLLOW_TIME, TimeValue.valueOf(value));
    }

    public String getDiyColumn1() {
        StringValue value = (StringValue)getValue(TableConstants.T_FOLLOW_RECORD.DIY_COLUMN1);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn1(String value) {
        setValue(TableConstants.T_FOLLOW_RECORD.DIY_COLUMN1, StringValue.valueOf(value));
    }

    public String getDiyColumn2() {
        StringValue value = (StringValue)getValue(TableConstants.T_FOLLOW_RECORD.DIY_COLUMN2);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn2(String value) {
        setValue(TableConstants.T_FOLLOW_RECORD.DIY_COLUMN2, StringValue.valueOf(value));
    }

    public String getDiyColumn3() {
        StringValue value = (StringValue)getValue(TableConstants.T_FOLLOW_RECORD.DIY_COLUMN3);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiyColumn3(String value) {
        setValue(TableConstants.T_FOLLOW_RECORD.DIY_COLUMN3, StringValue.valueOf(value));
    }

}