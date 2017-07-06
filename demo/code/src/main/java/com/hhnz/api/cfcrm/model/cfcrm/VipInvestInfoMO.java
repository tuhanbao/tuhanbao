package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.hhnz.api.cfcrm.constants.enums.VipType;

public class VipInvestInfoMO extends ServiceBean {
    protected VipInvestInfoMO() {
        this(new MetaObject(TableConstants.T_VIP_INVEST_INFO.TABLE));
    }

    protected VipInvestInfoMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_INFO.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.ID, LongValue.valueOf(value));
    }

    public long getCustomId() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_INFO.CUSTOM_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCustomId(Long value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.CUSTOM_ID, LongValue.valueOf(value));
    }

    public VipPrimaryInfo getCustom() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_INVEST_INFO.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public VipPrimaryInfo removeCustom() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_INVEST_INFO.CUSTOM_ID);
        return result == null || result.isEmpty() ? null : (VipPrimaryInfo)result.get(0);
    }

    public void setCustom(VipPrimaryInfo value) {
        this.setFKBean(TableConstants.T_VIP_INVEST_INFO.CUSTOM_ID, value);
    }

    public long getDayInvestBalance() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_INFO.DAY_INVEST_BALANCE);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setDayInvestBalance(Long value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.DAY_INVEST_BALANCE, LongValue.valueOf(value));
    }

    public long getDayAccountBalance() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_INFO.DAY_ACCOUNT_BALANCE);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setDayAccountBalance(Long value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.DAY_ACCOUNT_BALANCE, LongValue.valueOf(value));
    }

    public long getDayAddInvestAmount() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_INFO.DAY_ADD_INVEST_AMOUNT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setDayAddInvestAmount(Long value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.DAY_ADD_INVEST_AMOUNT, LongValue.valueOf(value));
    }

    public int getTotalInvestTimes() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_INVEST_INFO.TOTAL_INVEST_TIMES);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setTotalInvestTimes(Integer value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.TOTAL_INVEST_TIMES, IntValue.valueOf(value));
    }

    public long getAccInvAmount() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_INFO.ACC_INV_AMOUNT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setAccInvAmount(Long value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.ACC_INV_AMOUNT, LongValue.valueOf(value));
    }

    public long getAccInvYearAmount() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_INFO.ACC_INV_YEAR_AMOUNT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setAccInvYearAmount(Long value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.ACC_INV_YEAR_AMOUNT, LongValue.valueOf(value));
    }

    public int getAccInviInvestPersons() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_INVEST_INFO.ACC_INVI_INVEST_PERSONS);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setAccInviInvestPersons(Integer value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.ACC_INVI_INVEST_PERSONS, IntValue.valueOf(value));
    }

    public long getAccInviInvestAmount() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_INFO.ACC_INVI_INVEST_AMOUNT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setAccInviInvestAmount(Long value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.ACC_INVI_INVEST_AMOUNT, LongValue.valueOf(value));
    }

    public long getAccInviInvestAmountYear() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_INFO.ACC_INVI_INVEST_AMOUNT_YEAR);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setAccInviInvestAmountYear(Long value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.ACC_INVI_INVEST_AMOUNT_YEAR, LongValue.valueOf(value));
    }

    public int getDailyDecreasement() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_INVEST_INFO.DAILY_DECREASEMENT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setDailyDecreasement(Integer value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.DAILY_DECREASEMENT, IntValue.valueOf(value));
    }

    public int getOffDay() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_INVEST_INFO.OFF_DAY);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setOffDay(Integer value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.OFF_DAY, IntValue.valueOf(value));
    }

    public int getRank() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_INVEST_INFO.RANK);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setRank(Integer value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.RANK, IntValue.valueOf(value));
    }

    public VipType getVipType() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_INVEST_INFO.VIP_TYPE);
        if (value == null) return null;
        else return VipType.getVipType(value.getValue());
    }

    public void setVipType(VipType value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.VIP_TYPE, IntValue.valueOf(value.value));
    }

    public int getYear() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_INVEST_INFO.YEAR);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setYear(Integer value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.YEAR, IntValue.valueOf(value));
    }

    public int getMonth() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_INVEST_INFO.MONTH);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setMonth(Integer value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.MONTH, IntValue.valueOf(value));
    }

    public long getYearMonthInvestAmount() {
        LongValue value = (LongValue)getValue(TableConstants.T_VIP_INVEST_INFO.YEAR_MONTH_INVEST_AMOUNT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setYearMonthInvestAmount(Long value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.YEAR_MONTH_INVEST_AMOUNT, LongValue.valueOf(value));
    }

    public int getRankAcc() {
        IntValue value = (IntValue)getValue(TableConstants.T_VIP_INVEST_INFO.RANK_ACC);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setRankAcc(Integer value) {
        setValue(TableConstants.T_VIP_INVEST_INFO.RANK_ACC, IntValue.valueOf(value));
    }

}