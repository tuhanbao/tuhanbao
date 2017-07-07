package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.hhnz.api.cfcrm.constants.enums.SendStatus;
import com.hhnz.api.cfcrm.constants.enums.SendType;

public class AwardVipMO extends ServiceBean {
    protected AwardVipMO() {
        this(new MetaObject(TableConstants.T_AWARD_VIP.TABLE));
    }

    protected AwardVipMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_AWARD_VIP.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_AWARD_VIP.ID, LongValue.valueOf(value));
    }

    public long getCustomId() {
        LongValue value = (LongValue)getValue(TableConstants.T_AWARD_VIP.CUSTOM_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCustomId(Long value) {
        setValue(TableConstants.T_AWARD_VIP.CUSTOM_ID, LongValue.valueOf(value));
    }

    public VipPrimaryInfo getCustom() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_AWARD_VIP.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public VipPrimaryInfo removeCustom() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_AWARD_VIP.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public void setCustom(VipPrimaryInfo value) {
        this.setFKBean(TableConstants.T_AWARD_VIP.CUSTOM_ID, value);
    }

    public long getAwardId() {
        LongValue value = (LongValue)getValue(TableConstants.T_AWARD_VIP.AWARD_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setAwardId(Long value) {
        setValue(TableConstants.T_AWARD_VIP.AWARD_ID, LongValue.valueOf(value));
    }

    public VipAwardManage getAward() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_AWARD_VIP.AWARD_ID);
        return result == null || result.isEmpty() ? null : (VipAwardManage)result.get(0);
    }

    public VipAwardManage removeAward() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_AWARD_VIP.AWARD_ID);
        return result == null || result.isEmpty() ? null : (VipAwardManage)result.get(0);
    }

    public void setAward(VipAwardManage value) {
        this.setFKBean(TableConstants.T_AWARD_VIP.AWARD_ID, value);
    }

    public SendStatus getSendStatus() {
        IntValue value = (IntValue)getValue(TableConstants.T_AWARD_VIP.SEND_STATUS);
        if (value == null) return null;
        else return SendStatus.getSendStatus(value.getValue());
    }

    public void setSendStatus(SendStatus value) {
        setValue(TableConstants.T_AWARD_VIP.SEND_STATUS, IntValue.valueOf(value.value));
    }

    public SendType getSendType() {
        IntValue value = (IntValue)getValue(TableConstants.T_AWARD_VIP.SEND_TYPE);
        if (value == null) return null;
        else return SendType.getSendType(value.getValue());
    }

    public void setSendType(SendType value) {
        setValue(TableConstants.T_AWARD_VIP.SEND_TYPE, IntValue.valueOf(value.value));
    }

}