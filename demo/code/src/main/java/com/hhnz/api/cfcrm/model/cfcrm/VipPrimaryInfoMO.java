package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.hhnz.api.cfcrm.constants.enums.Sex;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.BooleanValue;
import com.hhnz.api.cfcrm.constants.enums.CustomType;
import com.hhnz.api.cfcrm.constants.enums.OfficeType;

public class VipPrimaryInfoMO extends ServiceBean {
    protected VipPrimaryInfoMO() {
        this(new MetaObject(TableConstants.T_VIP_PRIMARY_INFO.TABLE));
    }

    protected VipPrimaryInfoMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.ID, LongValue.valueOf(value));
    }

    public long getNongzhuangId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.NONGZHUANG_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setNongzhuangId(Long value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.NONGZHUANG_ID, LongValue.valueOf(value));
    }

    public String getName() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setName(String value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.NAME, StringValue.valueOf(value));
    }

    public String getPhone() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.PHONE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setPhone(String value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.PHONE, StringValue.valueOf(value));
    }

    public Sex getSex() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.SEX);
        if (value == null) return null;
        else return Sex.getSex(value.getValue());
    }

    public void setSex(Sex value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.SEX, IntValue.valueOf(value.value));
    }

    public String getInvitateNum() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.INVITATE_NUM);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setInvitateNum(String value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.INVITATE_NUM, StringValue.valueOf(value));
    }

    public Date getBirthday() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.BIRTHDAY);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setBirthday(Date value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.BIRTHDAY, TimeValue.valueOf(value));
    }

    public int getMonth() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.MONTH);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setMonth(Integer value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.MONTH, IntValue.valueOf(value));
    }

    public int getDay() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.DAY);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setDay(Integer value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.DAY, IntValue.valueOf(value));
    }

    public String getVipProvinceId() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.VIP_PROVINCE_ID);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setVipProvinceId(String value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.VIP_PROVINCE_ID, StringValue.valueOf(value));
    }

    public Area getVipProvince() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_PROVINCE_ID);
        return result == null || result.isEmpty() ? null : (Area)result.get(0);
    }

    public Area removeVipProvince() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_PROVINCE_ID);
        return result == null || result.isEmpty() ? null : (Area)result.get(0);
    }

    public void setVipProvince(Area value) {
        this.setFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_PROVINCE_ID, value);
    }

    public String getVipCityId() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.VIP_CITY_ID);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setVipCityId(String value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.VIP_CITY_ID, StringValue.valueOf(value));
    }

    public Area getVipCity() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_CITY_ID);
        return result == null || result.isEmpty() ? null : (Area)result.get(0);
    }

    public Area removeVipCity() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_CITY_ID);
        return result == null || result.isEmpty() ? null : (Area)result.get(0);
    }

    public void setVipCity(Area value) {
        this.setFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_CITY_ID, value);
    }

    public String getVipDistrictId() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.VIP_DISTRICT_ID);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setVipDistrictId(String value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.VIP_DISTRICT_ID, StringValue.valueOf(value));
    }

    public Area getVipDistrict() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_DISTRICT_ID);
        return result == null || result.isEmpty() ? null : (Area)result.get(0);
    }

    public Area removeVipDistrict() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_DISTRICT_ID);
        return result == null || result.isEmpty() ? null : (Area)result.get(0);
    }

    public void setVipDistrict(Area value) {
        this.setFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_DISTRICT_ID, value);
    }

    public String getDetailAddress() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.DETAIL_ADDRESS);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDetailAddress(String value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.DETAIL_ADDRESS, StringValue.valueOf(value));
    }

    public boolean isDel() {
        BooleanValue value = (BooleanValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.IS_DEL);
        if (value == null) return false;
        else return value.getValue();
    }

    public void setIsDel(Boolean value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.IS_DEL, BooleanValue.valueOf(value));
    }

    public CustomType getCustomType() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.CUSTOM_TYPE);
        if (value == null) return null;
        else return CustomType.getCustomType(value.getValue());
    }

    public void setCustomType(CustomType value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.CUSTOM_TYPE, IntValue.valueOf(value.value));
    }

    public OfficeType getOfficeType() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.OFFICE_TYPE);
        if (value == null) return null;
        else return OfficeType.getOfficeType(value.getValue());
    }

    public void setOfficeType(OfficeType value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.OFFICE_TYPE, IntValue.valueOf(value.value));
    }

    public boolean getBeVip() {
        BooleanValue value = (BooleanValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.BE_VIP);
        if (value == null) return false;
        else return value.getValue();
    }

    public void setBeVip(Boolean value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.BE_VIP, BooleanValue.valueOf(value));
    }

    public Date getVipStart() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.VIP_START);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setVipStart(Date value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.VIP_START, TimeValue.valueOf(value));
    }

    public boolean isReset() {
        BooleanValue value = (BooleanValue)getValue(TableConstants.T_VIP_PRIMARY_INFO.IS_RESET);
        if (value == null) return false;
        else return value.getValue();
    }

    public void setIsReset(Boolean value) {
        setValue(TableConstants.T_VIP_PRIMARY_INFO.IS_RESET, BooleanValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<VipInvestInfo> getVipInvestInfos() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_INVEST_INFO.CUSTOM_ID);
        return (List<VipInvestInfo>)result;
    }

    @SuppressWarnings("unchecked")
    public List<VipInvestInfo> removeVipInvestInfos() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_INVEST_INFO.CUSTOM_ID);
        return (List<VipInvestInfo>)result;
    }

    public void setVipInvestInfos(List<VipInvestInfo> value) {
        this.setFKBean(TableConstants.T_VIP_INVEST_INFO.CUSTOM_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<AwardVip> getAwardVips() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_AWARD_VIP.CUSTOM_ID);
        return (List<AwardVip>)result;
    }

    @SuppressWarnings("unchecked")
    public List<AwardVip> removeAwardVips() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_AWARD_VIP.CUSTOM_ID);
        return (List<AwardVip>)result;
    }

    public void setAwardVips(List<AwardVip> value) {
        this.setFKBean(TableConstants.T_AWARD_VIP.CUSTOM_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<VipMarketingRecord> getVipMarketingRecords() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_MARKETING_RECORD.CUSTOM_ID);
        return (List<VipMarketingRecord>)result;
    }

    @SuppressWarnings("unchecked")
    public List<VipMarketingRecord> removeVipMarketingRecords() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_MARKETING_RECORD.CUSTOM_ID);
        return (List<VipMarketingRecord>)result;
    }

    public void setVipMarketingRecords(List<VipMarketingRecord> value) {
        this.setFKBean(TableConstants.T_VIP_MARKETING_RECORD.CUSTOM_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<PaymentCollection> getPaymentCollections() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_PAYMENT_COLLECTION.CUSTOM_ID);
        return (List<PaymentCollection>)result;
    }

    @SuppressWarnings("unchecked")
    public List<PaymentCollection> removePaymentCollections() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_PAYMENT_COLLECTION.CUSTOM_ID);
        return (List<PaymentCollection>)result;
    }

    public void setPaymentCollections(List<PaymentCollection> value) {
        this.setFKBean(TableConstants.T_PAYMENT_COLLECTION.CUSTOM_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<SendToVip> getSendToVips() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SEND_TO_VIP.CUSTOM_ID);
        return (List<SendToVip>)result;
    }

    @SuppressWarnings("unchecked")
    public List<SendToVip> removeSendToVips() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SEND_TO_VIP.CUSTOM_ID);
        return (List<SendToVip>)result;
    }

    public void setSendToVips(List<SendToVip> value) {
        this.setFKBean(TableConstants.T_SEND_TO_VIP.CUSTOM_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<VipInvestFlow> getVipInvestFlows() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_INVEST_FLOW.CUSTOM_ID);
        return (List<VipInvestFlow>)result;
    }

    @SuppressWarnings("unchecked")
    public List<VipInvestFlow> removeVipInvestFlows() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_INVEST_FLOW.CUSTOM_ID);
        return (List<VipInvestFlow>)result;
    }

    public void setVipInvestFlows(List<VipInvestFlow> value) {
        this.setFKBean(TableConstants.T_VIP_INVEST_FLOW.CUSTOM_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<SendHistoryVip> getSendHistoryVips() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SEND_HISTORY_VIP.CUSTOM_ID);
        return (List<SendHistoryVip>)result;
    }

    @SuppressWarnings("unchecked")
    public List<SendHistoryVip> removeSendHistoryVips() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SEND_HISTORY_VIP.CUSTOM_ID);
        return (List<SendHistoryVip>)result;
    }

    public void setSendHistoryVips(List<SendHistoryVip> value) {
        this.setFKBean(TableConstants.T_SEND_HISTORY_VIP.CUSTOM_ID, value);
    }

}