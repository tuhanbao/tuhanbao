package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.hhnz.api.cfcrm.constants.enums.MsgType;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;
import com.tuhanbao.base.util.db.table.data.BooleanValue;
import com.hhnz.api.cfcrm.constants.enums.SendStatus;
import com.hhnz.api.cfcrm.constants.enums.SendType;
import java.util.List;

public class MsgAutoSendMO extends ServiceBean {
    protected MsgAutoSendMO() {
        this(new MetaObject(TableConstants.T_MSG_AUTO_SEND.TABLE));
    }

    protected MsgAutoSendMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_MSG_AUTO_SEND.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_MSG_AUTO_SEND.ID, LongValue.valueOf(value));
    }

    public MsgType getType() {
        IntValue value = (IntValue)getValue(TableConstants.T_MSG_AUTO_SEND.TYPE);
        if (value == null) return null;
        else return MsgType.getMsgType(value.getValue());
    }

    public void setType(MsgType value) {
        setValue(TableConstants.T_MSG_AUTO_SEND.TYPE, IntValue.valueOf(value.value));
    }

    public String getName() {
        StringValue value = (StringValue)getValue(TableConstants.T_MSG_AUTO_SEND.NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setName(String value) {
        setValue(TableConstants.T_MSG_AUTO_SEND.NAME, StringValue.valueOf(value));
    }

    public Date getSendDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_MSG_AUTO_SEND.SEND_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setSendDate(Date value) {
        setValue(TableConstants.T_MSG_AUTO_SEND.SEND_DATE, TimeValue.valueOf(value));
    }

    public int getTime() {
        IntValue value = (IntValue)getValue(TableConstants.T_MSG_AUTO_SEND.TIME);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setTime(Integer value) {
        setValue(TableConstants.T_MSG_AUTO_SEND.TIME, IntValue.valueOf(value));
    }

    public String getContent() {
        StringValue value = (StringValue)getValue(TableConstants.T_MSG_AUTO_SEND.CONTENT);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setContent(String value) {
        setValue(TableConstants.T_MSG_AUTO_SEND.CONTENT, StringValue.valueOf(value));
    }

    public boolean isOpen() {
        BooleanValue value = (BooleanValue)getValue(TableConstants.T_MSG_AUTO_SEND.IS_OPEN);
        if (value == null) return false;
        else return value.getValue();
    }

    public void setIsOpen(Boolean value) {
        setValue(TableConstants.T_MSG_AUTO_SEND.IS_OPEN, BooleanValue.valueOf(value));
    }

    public SendStatus getSendStatus() {
        IntValue value = (IntValue)getValue(TableConstants.T_MSG_AUTO_SEND.SEND_STATUS);
        if (value == null) return null;
        else return SendStatus.getSendStatus(value.getValue());
    }

    public void setSendStatus(SendStatus value) {
        setValue(TableConstants.T_MSG_AUTO_SEND.SEND_STATUS, IntValue.valueOf(value.value));
    }

    public SendType getSendType() {
        IntValue value = (IntValue)getValue(TableConstants.T_MSG_AUTO_SEND.SEND_TYPE);
        if (value == null) return null;
        else return SendType.getSendType(value.getValue());
    }

    public void setSendType(SendType value) {
        setValue(TableConstants.T_MSG_AUTO_SEND.SEND_TYPE, IntValue.valueOf(value.value));
    }

    @SuppressWarnings("unchecked")
    public List<VipMsgSendingHistory> getVipMsgSendingHistorys() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_MSG_SENDING_HISTORY.MSG_ID);
        return (List<VipMsgSendingHistory>)result;
    }

    @SuppressWarnings("unchecked")
    public List<VipMsgSendingHistory> removeVipMsgSendingHistorys() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_MSG_SENDING_HISTORY.MSG_ID);
        return (List<VipMsgSendingHistory>)result;
    }

    public void setVipMsgSendingHistorys(List<VipMsgSendingHistory> value) {
        this.setFKBean(TableConstants.T_VIP_MSG_SENDING_HISTORY.MSG_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<SendToVip> getSendToVips() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SEND_TO_VIP.MSG_ID);
        return (List<SendToVip>)result;
    }

    @SuppressWarnings("unchecked")
    public List<SendToVip> removeSendToVips() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SEND_TO_VIP.MSG_ID);
        return (List<SendToVip>)result;
    }

    public void setSendToVips(List<SendToVip> value) {
        this.setFKBean(TableConstants.T_SEND_TO_VIP.MSG_ID, value);
    }

}