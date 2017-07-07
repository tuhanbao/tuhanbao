package com.hhnz.api.cfcrm.model.fz;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.BooleanValue;
import java.util.List;

public class OfficeMO extends ServiceBean {
    protected OfficeMO() {
        this(new MetaObject(TableConstants.OFFICE.TABLE));
    }

    protected OfficeMO(MetaObject mo) {
        super(mo);
    }

    public String getOffice() {
        StringValue value = (StringValue)getValue(TableConstants.OFFICE.OFFICE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setOffice(String value) {
        setValue(TableConstants.OFFICE.OFFICE, StringValue.valueOf(value));
    }

    public String getOfficeId() {
        StringValue value = (StringValue)getValue(TableConstants.OFFICE.OFFICE_ID);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setOfficeId(String value) {
        setValue(TableConstants.OFFICE.OFFICE_ID, StringValue.valueOf(value));
    }

    public String getAreaId() {
        StringValue value = (StringValue)getValue(TableConstants.OFFICE.AREA_ID);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setAreaId(String value) {
        setValue(TableConstants.OFFICE.AREA_ID, StringValue.valueOf(value));
    }

    public String getDetailAddress() {
        StringValue value = (StringValue)getValue(TableConstants.OFFICE.DETAIL_ADDRESS);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setDetailAddress(String value) {
        setValue(TableConstants.OFFICE.DETAIL_ADDRESS, StringValue.valueOf(value));
    }

    public String getCompleteAddress() {
        StringValue value = (StringValue)getValue(TableConstants.OFFICE.COMPLETE_ADDRESS);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setCompleteAddress(String value) {
        setValue(TableConstants.OFFICE.COMPLETE_ADDRESS, StringValue.valueOf(value));
    }

    public boolean isStart() {
        BooleanValue value = (BooleanValue)getValue(TableConstants.OFFICE.IS_START);
        if (value == null) return false;
        else return value.getValue();
    }

    public void setIsStart(Boolean value) {
        setValue(TableConstants.OFFICE.IS_START, BooleanValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<OfficeRelation> getOfficeRelations() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.OFFICE_RELATION.OFFICE_ID);
        return (List<OfficeRelation>)result;
    }

    @SuppressWarnings("unchecked")
    public List<OfficeRelation> removeOfficeRelations() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.OFFICE_RELATION.OFFICE_ID);
        return (List<OfficeRelation>)result;
    }

    public void setOfficeRelations(List<OfficeRelation> value) {
        this.setFKBean(TableConstants.OFFICE_RELATION.OFFICE_ID, value);
    }

}