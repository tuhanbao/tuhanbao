package com.tuhanbao.api.crm.model.crm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.tuhanbao.api.crm.constants.enums.ServerState;
import java.util.Date;
import com.tuhanbao.base.util.db.table.data.TimeValue;
import com.tuhanbao.base.util.db.table.data.BooleanValue;
import com.tuhanbao.base.util.db.table.data.FloatValue;
import com.tuhanbao.base.util.db.table.data.DoubleValue;
import java.util.List;

public class CompanyMO extends ServiceBean {
    protected CompanyMO() {
        this(new MetaObject(TableConstants.T_COMPANY.TABLE));
    }

    protected CompanyMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_COMPANY.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_COMPANY.ID, LongValue.valueOf(value));
    }

    public String getName() {
        StringValue value = (StringValue)getValue(TableConstants.T_COMPANY.NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setName(String value) {
        setValue(TableConstants.T_COMPANY.NAME, StringValue.valueOf(value));
    }

    public String getShortName() {
        StringValue value = (StringValue)getValue(TableConstants.T_COMPANY.SHORT_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setShortName(String value) {
        setValue(TableConstants.T_COMPANY.SHORT_NAME, StringValue.valueOf(value));
    }

    public ServerState getServerState() {
        IntValue value = (IntValue)getValue(TableConstants.T_COMPANY.SERVER_STATE);
        if (value == null) return null;
        else return ServerState.getServerState(value.getValue());
    }

    public void setServerState(ServerState value) {
        setValue(TableConstants.T_COMPANY.SERVER_STATE, IntValue.valueOf(value.value));
    }

    public Date getValidTerm() {
        TimeValue value = (TimeValue)getValue(TableConstants.T_COMPANY.VALID_TERM);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setValidTerm(Date value) {
        setValue(TableConstants.T_COMPANY.VALID_TERM, TimeValue.valueOf(value));
    }

    public boolean getTestBool() {
        BooleanValue value = (BooleanValue)getValue(TableConstants.T_COMPANY.TEST_BOOL);
        if (value == null) return false;
        else return value.getValue();
    }

    public void setTestBool(Boolean value) {
        setValue(TableConstants.T_COMPANY.TEST_BOOL, BooleanValue.valueOf(value));
    }

    public float getTestFloat() {
        FloatValue value = (FloatValue)getValue(TableConstants.T_COMPANY.TEST_FLOAT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setTestFloat(Float value) {
        setValue(TableConstants.T_COMPANY.TEST_FLOAT, FloatValue.valueOf(value));
    }

    public double getTestDouble() {
        DoubleValue value = (DoubleValue)getValue(TableConstants.T_COMPANY.TEST_DOUBLE);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setTestDouble(Double value) {
        setValue(TableConstants.T_COMPANY.TEST_DOUBLE, DoubleValue.valueOf(value));
    }

    public int getTestInt() {
        IntValue value = (IntValue)getValue(TableConstants.T_COMPANY.TEST_INT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setTestInt(Integer value) {
        setValue(TableConstants.T_COMPANY.TEST_INT, IntValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_USER.COMPANY_ID);
        return (List<User>)result;
    }

    @SuppressWarnings("unchecked")
    public List<User> removeUsers() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_USER.COMPANY_ID);
        return (List<User>)result;
    }

    public void setUsers(List<User> value) {
        this.setFKBean(TableConstants.T_USER.COMPANY_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<DiyFilter> getDiyFilters() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_DIY_FILTER.COMPANY_ID);
        return (List<DiyFilter>)result;
    }

    @SuppressWarnings("unchecked")
    public List<DiyFilter> removeDiyFilters() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_DIY_FILTER.COMPANY_ID);
        return (List<DiyFilter>)result;
    }

    public void setDiyFilters(List<DiyFilter> value) {
        this.setFKBean(TableConstants.T_DIY_FILTER.COMPANY_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<DiyColInfo> getDiyColInfos() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_DIY_COL_INFO.COMPANY_ID);
        return (List<DiyColInfo>)result;
    }

    @SuppressWarnings("unchecked")
    public List<DiyColInfo> removeDiyColInfos() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_DIY_COL_INFO.COMPANY_ID);
        return (List<DiyColInfo>)result;
    }

    public void setDiyColInfos(List<DiyColInfo> value) {
        this.setFKBean(TableConstants.T_DIY_COL_INFO.COMPANY_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<CompanySmsInfo> getCompanySmsInfos() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_COMPANY_SMS_INFO.COMPANY_ID);
        return (List<CompanySmsInfo>)result;
    }

    @SuppressWarnings("unchecked")
    public List<CompanySmsInfo> removeCompanySmsInfos() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_COMPANY_SMS_INFO.COMPANY_ID);
        return (List<CompanySmsInfo>)result;
    }

    public void setCompanySmsInfos(List<CompanySmsInfo> value) {
        this.setFKBean(TableConstants.T_COMPANY_SMS_INFO.COMPANY_ID, value);
    }

}