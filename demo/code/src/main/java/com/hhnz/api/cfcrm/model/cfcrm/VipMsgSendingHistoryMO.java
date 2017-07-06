package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;

public class VipMsgSendingHistoryMO extends ServiceBean {
    protected VipMsgSendingHistoryMO() {
        this(new MetaObject(TableConstants.T_VIP_MSG_SENDING_HISTORY.TABLE));
    }

    protected VipMsgSendingHistoryMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_MSG_SENDING_HISTORY.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_VIP_MSG_SENDING_HISTORY.ID, LongValue.valueOf(value));
    }

    public long getMsgId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_MSG_SENDING_HISTORY.MSG_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setMsgId(Long value) {
        setValue(TableConstants.T_VIP_MSG_SENDING_HISTORY.MSG_ID, LongValue.valueOf(value));
    }

    public MsgAutoSend getMsg() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_MSG_SENDING_HISTORY.MSG_ID);
        return result == null || result.isEmpty() ? null : (MsgAutoSend)result.get(0);
    }

    public MsgAutoSend removeMsg() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_MSG_SENDING_HISTORY.MSG_ID);
        return result == null || result.isEmpty() ? null : (MsgAutoSend)result.get(0);
    }

    public void setMsg(MsgAutoSend value) {
        this.setFKBean(TableConstants.T_VIP_MSG_SENDING_HISTORY.MSG_ID, value);
    }

    public String getMsgName() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_MSG_SENDING_HISTORY.MSG_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setMsgName(String value) {
        setValue(TableConstants.T_VIP_MSG_SENDING_HISTORY.MSG_NAME, StringValue.valueOf(value));
    }

    public Date getSendDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_MSG_SENDING_HISTORY.SEND_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setSendDate(Date value) {
        setValue(TableConstants.T_VIP_MSG_SENDING_HISTORY.SEND_DATE, TimeValue.valueOf(value));
    }

    public String getSendContent() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_MSG_SENDING_HISTORY.SEND_CONTENT);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setSendContent(String value) {
        setValue(TableConstants.T_VIP_MSG_SENDING_HISTORY.SEND_CONTENT, StringValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<SendHistoryVip> getSendHistoryVips() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SEND_HISTORY_VIP.MSG_ID);
        return (List<SendHistoryVip>)result;
    }

    @SuppressWarnings("unchecked")
    public List<SendHistoryVip> removeSendHistoryVips() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SEND_HISTORY_VIP.MSG_ID);
        return (List<SendHistoryVip>)result;
    }

    public void setSendHistoryVips(List<SendHistoryVip> value) {
        this.setFKBean(TableConstants.T_SEND_HISTORY_VIP.MSG_ID, value);
    }

}