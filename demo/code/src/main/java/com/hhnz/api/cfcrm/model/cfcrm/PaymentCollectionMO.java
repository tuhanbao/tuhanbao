package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.hhnz.api.cfcrm.constants.enums.MoneyBackStatus;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;

public class PaymentCollectionMO extends ServiceBean {
    protected PaymentCollectionMO() {
        this(new MetaObject(TableConstants.T_PAYMENT_COLLECTION.TABLE));
    }

    protected PaymentCollectionMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_PAYMENT_COLLECTION.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_PAYMENT_COLLECTION.ID, LongValue.valueOf(value));
    }

    public long getCustomId() {
        LongValue value = (LongValue)getValue(TableConstants.T_PAYMENT_COLLECTION.CUSTOM_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCustomId(Long value) {
        setValue(TableConstants.T_PAYMENT_COLLECTION.CUSTOM_ID, LongValue.valueOf(value));
    }

    public VipPrimaryInfo getCustom() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_PAYMENT_COLLECTION.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public VipPrimaryInfo removeCustom() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_PAYMENT_COLLECTION.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public void setCustom(VipPrimaryInfo value) {
        this.setFKBean(TableConstants.T_PAYMENT_COLLECTION.CUSTOM_ID, value);
    }

    public String getProjectName() {
        StringValue value = (StringValue)getValue(TableConstants.T_PAYMENT_COLLECTION.PROJECT_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setProjectName(String value) {
        setValue(TableConstants.T_PAYMENT_COLLECTION.PROJECT_NAME, StringValue.valueOf(value));
    }

    public int getCapital() {
        IntValue value = (IntValue)getValue(TableConstants.T_PAYMENT_COLLECTION.CAPITAL);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCapital(Integer value) {
        setValue(TableConstants.T_PAYMENT_COLLECTION.CAPITAL, IntValue.valueOf(value));
    }

    public int getProfit() {
        IntValue value = (IntValue)getValue(TableConstants.T_PAYMENT_COLLECTION.PROFIT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setProfit(Integer value) {
        setValue(TableConstants.T_PAYMENT_COLLECTION.PROFIT, IntValue.valueOf(value));
    }

    public MoneyBackStatus getStatus() {
        IntValue value = (IntValue)getValue(TableConstants.T_PAYMENT_COLLECTION.STATUS);
        if (value == null) return null;
        else return MoneyBackStatus.getMoneyBackStatus(value.getValue());
    }

    public void setStatus(MoneyBackStatus value) {
        setValue(TableConstants.T_PAYMENT_COLLECTION.STATUS, IntValue.valueOf(value.value));
    }

    public Date getPaymentCollectionDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_PAYMENT_COLLECTION.PAYMENT_COLLECTION_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setPaymentCollectionDate(Date value) {
        setValue(TableConstants.T_PAYMENT_COLLECTION.PAYMENT_COLLECTION_DATE, TimeValue.valueOf(value));
    }

    public long getPaymentCollection() {
        LongValue value = (LongValue)getValue(TableConstants.T_PAYMENT_COLLECTION.PAYMENT_COLLECTION);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setPaymentCollection(Long value) {
        setValue(TableConstants.T_PAYMENT_COLLECTION.PAYMENT_COLLECTION, LongValue.valueOf(value));
    }

}