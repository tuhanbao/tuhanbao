package com.hhnz.api.cfcrm.model.fz;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.IntValue;

public class RelationMO extends ServiceBean {
    protected RelationMO() {
        this(new MetaObject(TableConstants.RELATION.TABLE));
    }

    protected RelationMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.RELATION.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.RELATION.ID, LongValue.valueOf(value));
    }

    public long getChild() {
        LongValue value = (LongValue)getValue(TableConstants.RELATION.CHILD);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setChild(Long value) {
        setValue(TableConstants.RELATION.CHILD, LongValue.valueOf(value));
    }

    public long getFather() {
        LongValue value = (LongValue)getValue(TableConstants.RELATION.FATHER);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setFather(Long value) {
        setValue(TableConstants.RELATION.FATHER, LongValue.valueOf(value));
    }

    public int getYear() {
        IntValue value = (IntValue)getValue(TableConstants.RELATION.YEAR);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setYear(Integer value) {
        setValue(TableConstants.RELATION.YEAR, IntValue.valueOf(value));
    }

    public int getMonth() {
        IntValue value = (IntValue)getValue(TableConstants.RELATION.MONTH);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setMonth(Integer value) {
        setValue(TableConstants.RELATION.MONTH, IntValue.valueOf(value));
    }

}