package com.tuhanbao.api.crm.model.crm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.api.crm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.tuhanbao.api.crm.constants.enums.FilterType;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.List;

public class DiyFilterMO extends ServiceBean {
    protected DiyFilterMO() {
        this(new MetaObject(TableConstants.T_DIY_FILTER.TABLE));
    }

    protected DiyFilterMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_DIY_FILTER.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_DIY_FILTER.ID, LongValue.valueOf(value));
    }

    public FilterType getFilterType() {
        IntValue value = (IntValue)getValue(TableConstants.T_DIY_FILTER.FILTER_TYPE);
        if (value == null) return null;
        else return FilterType.getFilterType(value.getValue());
    }

    public void setFilterType(FilterType value) {
        setValue(TableConstants.T_DIY_FILTER.FILTER_TYPE, IntValue.valueOf(value.value));
    }

    public String getName() {
        StringValue value = (StringValue)getValue(TableConstants.T_DIY_FILTER.NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setName(String value) {
        setValue(TableConstants.T_DIY_FILTER.NAME, StringValue.valueOf(value));
    }

    public int getSort() {
        IntValue value = (IntValue)getValue(TableConstants.T_DIY_FILTER.SORT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setSort(Integer value) {
        setValue(TableConstants.T_DIY_FILTER.SORT, IntValue.valueOf(value));
    }

    public long getCompanyId() {
        LongValue value = (LongValue)getValue(TableConstants.T_DIY_FILTER.COMPANY_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setCompanyId(Long value) {
        setValue(TableConstants.T_DIY_FILTER.COMPANY_ID, LongValue.valueOf(value));
    }

    public Company getCompany() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_DIY_FILTER.COMPANY_ID);
        return result == null || result.isEmpty() ? null : (Company)result.get(0);
    }

    public Company removeCompany() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_DIY_FILTER.COMPANY_ID);
        return result == null || result.isEmpty() ? null : (Company)result.get(0);
    }

    public void setCompany(Company value) {
        this.setFKBean(TableConstants.T_DIY_FILTER.COMPANY_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<DiyFilterItem> getDiyFilterItems() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_DIY_FILTER_ITEM.FILTER_ID);
        return (List<DiyFilterItem>)result;
    }

    @SuppressWarnings("unchecked")
    public List<DiyFilterItem> removeDiyFilterItems() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_DIY_FILTER_ITEM.FILTER_ID);
        return (List<DiyFilterItem>)result;
    }

    public void setDiyFilterItems(List<DiyFilterItem> value) {
        this.setFKBean(TableConstants.T_DIY_FILTER_ITEM.FILTER_ID, value);
    }

}