package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.tuhanbao.base.dataservice.filter.LogicType;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.dataservice.filter.operator.Operator;

public class DiyFilterItemMO extends ServiceBean {
    protected DiyFilterItemMO() {
        this(new MetaObject(TableConstants.T_DIY_FILTER_ITEM.TABLE));
    }

    protected DiyFilterItemMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_DIY_FILTER_ITEM.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_DIY_FILTER_ITEM.ID, LongValue.valueOf(value));
    }

    public long getFilterId() {
        LongValue value = (LongValue)getValue(TableConstants.T_DIY_FILTER_ITEM.FILTER_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setFilterId(Long value) {
        setValue(TableConstants.T_DIY_FILTER_ITEM.FILTER_ID, LongValue.valueOf(value));
    }

    public DiyFilter getFilter() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_DIY_FILTER_ITEM.FILTER_ID);
        return result == null || result.isEmpty() ? null : (DiyFilter)result.get(0);
    }

    public DiyFilter removeFilter() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_DIY_FILTER_ITEM.FILTER_ID);
        return result == null || result.isEmpty() ? null : (DiyFilter)result.get(0);
    }

    public void setFilter(DiyFilter value) {
        this.setFKBean(TableConstants.T_DIY_FILTER_ITEM.FILTER_ID, value);
    }

    public LogicType getLogicType() {
        IntValue value = (IntValue)getValue(TableConstants.T_DIY_FILTER_ITEM.LOGIC_TYPE);
        if (value == null) return null;
        else return LogicType.getLogicType(value.getValue());
    }

    public void setLogicType(LogicType value) {
        setValue(TableConstants.T_DIY_FILTER_ITEM.LOGIC_TYPE, IntValue.valueOf(value.value));
    }

    public String getName() {
        StringValue value = (StringValue)getValue(TableConstants.T_DIY_FILTER_ITEM.NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setName(String value) {
        setValue(TableConstants.T_DIY_FILTER_ITEM.NAME, StringValue.valueOf(value));
    }

    public Operator getOperator() {
        IntValue value = (IntValue)getValue(TableConstants.T_DIY_FILTER_ITEM.OPERATOR);
        if (value == null) return null;
        else return Operator.getOperator(value.getValue());
    }

    public void setOperator(Operator value) {
        setValue(TableConstants.T_DIY_FILTER_ITEM.OPERATOR, IntValue.valueOf(value.value));
    }

    public String getValue() {
        StringValue value = (StringValue)getValue(TableConstants.T_DIY_FILTER_ITEM.VALUE);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setValue(String value) {
        setValue(TableConstants.T_DIY_FILTER_ITEM.VALUE, StringValue.valueOf(value));
    }

}