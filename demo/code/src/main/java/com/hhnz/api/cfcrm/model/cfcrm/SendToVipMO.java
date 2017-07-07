package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;

public class SendToVipMO extends ServiceBean {
    protected SendToVipMO() {
        this(new MetaObject(TableConstants.T_SEND_TO_VIP.TABLE));
    }

    protected SendToVipMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_SEND_TO_VIP.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_SEND_TO_VIP.ID, LongValue.valueOf(value));
    }

    public long getMsgId() {
        LongValue value = (LongValue)getValue(TableConstants.T_SEND_TO_VIP.MSG_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setMsgId(Long value) {
        setValue(TableConstants.T_SEND_TO_VIP.MSG_ID, LongValue.valueOf(value));
    }

    public MsgAutoSend getMsg() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SEND_TO_VIP.MSG_ID);
        return result == null || result.isEmpty() ? null : (MsgAutoSend)result.get(0);
    }

    public MsgAutoSend removeMsg() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SEND_TO_VIP.MSG_ID);
        return result == null || result.isEmpty() ? null : (MsgAutoSend)result.get(0);
    }

    public void setMsg(MsgAutoSend value) {
        this.setFKBean(TableConstants.T_SEND_TO_VIP.MSG_ID, value);
    }

    public String getVipPhone() {
        StringValue value = (StringValue)getValue(TableConstants.T_SEND_TO_VIP.VIP_PHONE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setVipPhone(String value) {
        setValue(TableConstants.T_SEND_TO_VIP.VIP_PHONE, StringValue.valueOf(value));
    }

    public long getCustomId() {
        LongValue value = (LongValue)getValue(TableConstants.T_SEND_TO_VIP.CUSTOM_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCustomId(Long value) {
        setValue(TableConstants.T_SEND_TO_VIP.CUSTOM_ID, LongValue.valueOf(value));
    }

    public VipPrimaryInfo getCustom() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SEND_TO_VIP.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public VipPrimaryInfo removeCustom() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SEND_TO_VIP.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public void setCustom(VipPrimaryInfo value) {
        this.setFKBean(TableConstants.T_SEND_TO_VIP.CUSTOM_ID, value);
    }

}