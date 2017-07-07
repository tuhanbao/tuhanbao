package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;
import com.tuhanbao.base.util.db.table.data.IntValue;

public class VipInvestFlowMO extends ServiceBean {
    protected VipInvestFlowMO() {
        this(new MetaObject(TableConstants.T_VIP_INVEST_FLOW.TABLE));
    }

    protected VipInvestFlowMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_FLOW.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.ID, LongValue.valueOf(value));
    }

    public long getCustomId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_FLOW.CUSTOM_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCustomId(Long value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.CUSTOM_ID, LongValue.valueOf(value));
    }

    public VipPrimaryInfo getCustom() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_INVEST_FLOW.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public VipPrimaryInfo removeCustom() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_INVEST_FLOW.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public void setCustom(VipPrimaryInfo value) {
        this.setFKBean(TableConstants.T_VIP_INVEST_FLOW.CUSTOM_ID, value);
    }

    public String getName() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setName(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.NAME, StringValue.valueOf(value));
    }

    public long getInvestSource() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_FLOW.INVEST_SOURCE);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setInvestSource(Long value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.INVEST_SOURCE, LongValue.valueOf(value));
    }

    public String getExpectedBackRate() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.EXPECTED_BACK_RATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setExpectedBackRate(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.EXPECTED_BACK_RATE, StringValue.valueOf(value));
    }

    public Date getInvestorRegDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_INVEST_FLOW.INVESTOR_REG_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setInvestorRegDate(Date value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.INVESTOR_REG_DATE, TimeValue.valueOf(value));
    }

    public long getInvestAmount() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_FLOW.INVEST_AMOUNT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setInvestAmount(Long value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.INVEST_AMOUNT, LongValue.valueOf(value));
    }

    public int getInvestTerm() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_INVEST_FLOW.INVEST_TERM);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setInvestTerm(Integer value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.INVEST_TERM, IntValue.valueOf(value));
    }

    public String getAnnualizedInvest() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.ANNUALIZED_INVEST);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setAnnualizedInvest(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.ANNUALIZED_INVEST, StringValue.valueOf(value));
    }

    public String getDiscountCode() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_CODE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiscountCode(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_CODE, StringValue.valueOf(value));
    }

    public long getDiscountCustomId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_CUSTOM_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setDiscountCustomId(Long value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_CUSTOM_ID, LongValue.valueOf(value));
    }

    public String getDiscountCustomName() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_CUSTOM_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiscountCustomName(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_CUSTOM_NAME, StringValue.valueOf(value));
    }

    public Date getDiscountCustomCreateDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_CUSTOM_CREATE_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiscountCustomCreateDate(Date value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_CUSTOM_CREATE_DATE, TimeValue.valueOf(value));
    }

    public long getInvitationLevel() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_FLOW.INVITATION_LEVEL);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setInvitationLevel(Long value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.INVITATION_LEVEL, LongValue.valueOf(value));
    }

    public String getInvitationMoney() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.INVITATION_MONEY);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setInvitationMoney(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.INVITATION_MONEY, StringValue.valueOf(value));
    }

    public String getStanderType() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.STANDER_TYPE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setStanderType(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.STANDER_TYPE, StringValue.valueOf(value));
    }

    public String getStanderStatus() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.STANDER_STATUS);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setStanderStatus(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.STANDER_STATUS, StringValue.valueOf(value));
    }

    public long getSettlementStatus() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_FLOW.SETTLEMENT_STATUS);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setSettlementStatus(Long value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.SETTLEMENT_STATUS, LongValue.valueOf(value));
    }

    public Date getSettlementDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_INVEST_FLOW.SETTLEMENT_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setSettlementDate(Date value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.SETTLEMENT_DATE, TimeValue.valueOf(value));
    }

    public String getRepaymentTyep() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.REPAYMENT_TYEP);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setRepaymentTyep(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.REPAYMENT_TYEP, StringValue.valueOf(value));
    }

    public long getDiscountPersonInvitationId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_PERSON_INVITATION_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setDiscountPersonInvitationId(Long value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_PERSON_INVITATION_ID, LongValue.valueOf(value));
    }

    public String getDiscountPersonInvitationName() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_PERSON_INVITATION_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiscountPersonInvitationName(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_PERSON_INVITATION_NAME, StringValue.valueOf(value));
    }

    public String getDiscountPersonInvitationGroup() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_PERSON_INVITATION_GROUP);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDiscountPersonInvitationGroup(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.DISCOUNT_PERSON_INVITATION_GROUP, StringValue.valueOf(value));
    }

    public String getStanderTag() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.STANDER_TAG);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setStanderTag(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.STANDER_TAG, StringValue.valueOf(value));
    }

    public String getInvestInvitation() {
        StringValue value = (StringValue)getValue(TableConstants.T_VIP_INVEST_FLOW.INVEST_INVITATION);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setInvestInvitation(String value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.INVEST_INVITATION, StringValue.valueOf(value));
    }

    public Date getInvestDate() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_VIP_INVEST_FLOW.INVEST_DATE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setInvestDate(Date value) {
        setValue(TableConstants.T_VIP_INVEST_FLOW.INVEST_DATE, TimeValue.valueOf(value));
    }

}