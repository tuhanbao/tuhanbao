package com.hhnz.api.cfcrm.model.fz;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.List;

public class CExtendInfoMO extends ServiceBean {
    protected CExtendInfoMO() {
        this(new MetaObject(TableConstants.C_EXTEND_INFO.TABLE));
    }

    protected CExtendInfoMO(MetaObject mo) {
        super(mo);
    }

    public long getNongzhuangId() {
        LongValue value = (LongValue)getValue(TableConstants.C_EXTEND_INFO.NONGZHUANG_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setNongzhuangId(Long value) {
        setValue(TableConstants.C_EXTEND_INFO.NONGZHUANG_ID, LongValue.valueOf(value));
    }

    public String getCustomer() {
        StringValue value = (StringValue)getValue(TableConstants.C_EXTEND_INFO.CUSTOMER);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setCustomer(String value) {
        setValue(TableConstants.C_EXTEND_INFO.CUSTOMER, StringValue.valueOf(value));
    }

    public String getOffice() {
        StringValue value = (StringValue)getValue(TableConstants.C_EXTEND_INFO.OFFICE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setOffice(String value) {
        setValue(TableConstants.C_EXTEND_INFO.OFFICE, StringValue.valueOf(value));
    }

    public String getPosition() {
        StringValue value = (StringValue)getValue(TableConstants.C_EXTEND_INFO.POSITION);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setPosition(String value) {
        setValue(TableConstants.C_EXTEND_INFO.POSITION, StringValue.valueOf(value));
    }

    public String getIdNumber() {
        StringValue value = (StringValue)getValue(TableConstants.C_EXTEND_INFO.ID_NUMBER);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setIdNumber(String value) {
        setValue(TableConstants.C_EXTEND_INFO.ID_NUMBER, StringValue.valueOf(value));
    }

    public String getDetailAddress() {
        StringValue value = (StringValue)getValue(TableConstants.C_EXTEND_INFO.DETAIL_ADDRESS);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDetailAddress(String value) {
        setValue(TableConstants.C_EXTEND_INFO.DETAIL_ADDRESS, StringValue.valueOf(value));
    }

    public String getCompleteAddress() {
        StringValue value = (StringValue)getValue(TableConstants.C_EXTEND_INFO.COMPLETE_ADDRESS);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setCompleteAddress(String value) {
        setValue(TableConstants.C_EXTEND_INFO.COMPLETE_ADDRESS, StringValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.ROLE.NONGZHUANG_ID);
        return (List<Role>)result;
    }

    @SuppressWarnings("unchecked")
    public List<Role> removeRoles() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.ROLE.NONGZHUANG_ID);
        return (List<Role>)result;
    }

    public void setRoles(List<Role> value) {
        this.setFKBean(TableConstants.ROLE.NONGZHUANG_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<UsersBan> getUsersBans() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.USERS_BAN.NONGZHUANG_ID);
        return (List<UsersBan>)result;
    }

    @SuppressWarnings("unchecked")
    public List<UsersBan> removeUsersBans() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.USERS_BAN.NONGZHUANG_ID);
        return (List<UsersBan>)result;
    }

    public void setUsersBans(List<UsersBan> value) {
        this.setFKBean(TableConstants.USERS_BAN.NONGZHUANG_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<SeparateBillsApply> getSeparateBillsApplys() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_SEPARATE_BILLS_APPLY.APPLY_ID);
        return (List<SeparateBillsApply>)result;
    }

    @SuppressWarnings("unchecked")
    public List<SeparateBillsApply> removeSeparateBillsApplys() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_SEPARATE_BILLS_APPLY.APPLY_ID);
        return (List<SeparateBillsApply>)result;
    }

    public void setSeparateBillsApplys(List<SeparateBillsApply> value) {
        this.setFKBean(TableConstants.T_SEPARATE_BILLS_APPLY.APPLY_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<OfficeRelation> getOfficeRelations() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.OFFICE_RELATION.NONGZHUANG_ID);
        return (List<OfficeRelation>)result;
    }

    @SuppressWarnings("unchecked")
    public List<OfficeRelation> removeOfficeRelations() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.OFFICE_RELATION.NONGZHUANG_ID);
        return (List<OfficeRelation>)result;
    }

    public void setOfficeRelations(List<OfficeRelation> value) {
        this.setFKBean(TableConstants.OFFICE_RELATION.NONGZHUANG_ID, value);
    }

}