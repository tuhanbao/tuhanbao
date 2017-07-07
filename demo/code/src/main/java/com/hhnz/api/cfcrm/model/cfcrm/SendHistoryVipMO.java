package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.hhnz.api.cfcrm.constants.enums.SendStatus;

public class SendHistoryVipMO extends ServiceBean {
    protected SendHistoryVipMO() {
        this(new MetaObject(TableConstants.T_SEND_HISTORY_VIP.TABLE));
    }

    protected SendHistoryVipMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_SEND_HISTORY_VIP.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_SEND_HISTORY_VIP.ID, LongValue.valueOf(value));
    }

    public long getMsgId() {
        LongValue value = (LongValue)getValue(TableConstants.T_SEND_HISTORY_VIP.MSG_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setMsgId(Long value) {
        setValue(TableConstants.T_SEND_HISTORY_VIP.MSG_ID, LongValue.valueOf(value));
    }

    public VipMsgSendingHistory getMsg() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SEND_HISTORY_VIP.MSG_ID);
        return result == null || result.isEmpty() ? null : (VipMsgSendingHistory)result.get(0);
    }

    public VipMsgSendingHistory removeMsg() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SEND_HISTORY_VIP.MSG_ID);
        return result == null || result.isEmpty() ? null : (VipMsgSendingHistory)result.get(0);
    }

    public void setMsg(VipMsgSendingHistory value) {
        this.setFKBean(TableConstants.T_SEND_HISTORY_VIP.MSG_ID, value);
    }

    public String getVipPhone() {
        StringValue value = (StringValue)getValue(TableConstants.T_SEND_HISTORY_VIP.VIP_PHONE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setVipPhone(String value) {
        setValue(TableConstants.T_SEND_HISTORY_VIP.VIP_PHONE, StringValue.valueOf(value));
    }

    public long getCustomId() {
        LongValue value = (LongValue)getValue(TableConstants.T_SEND_HISTORY_VIP.CUSTOM_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCustomId(Long value) {
        setValue(TableConstants.T_SEND_HISTORY_VIP.CUSTOM_ID, LongValue.valueOf(value));
    }

    public VipPrimaryInfo getCustom() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SEND_HISTORY_VIP.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public VipPrimaryInfo removeCustom() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SEND_HISTORY_VIP.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public void setCustom(VipPrimaryInfo value) {
        this.setFKBean(TableConstants.T_SEND_HISTORY_VIP.CUSTOM_ID, value);
    }

    public SendStatus getSendStatus() {
        IntValue value = (IntValue)getValue(TableConstants.T_SEND_HISTORY_VIP.SEND_STATUS);
        if (value == null) return null;
        else return SendStatus.getSendStatus(value.getValue());
    }

    public void setSendStatus(SendStatus value) {
        setValue(TableConstants.T_SEND_HISTORY_VIP.SEND_STATUS, IntValue.valueOf(value.value));
    }

}