package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;

public class AwardItemMO extends ServiceBean {
    protected AwardItemMO() {
        this(new MetaObject(TableConstants.T_AWARD_ITEM.TABLE));
    }

    protected AwardItemMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_AWARD_ITEM.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_AWARD_ITEM.ID, LongValue.valueOf(value));
    }

    public long getAwardId() {
        LongValue value = (LongValue)getValue(TableConstants.T_AWARD_ITEM.AWARD_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setAwardId(Long value) {
        setValue(TableConstants.T_AWARD_ITEM.AWARD_ID, LongValue.valueOf(value));
    }

    public VipAwardManage getAward() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_AWARD_ITEM.AWARD_ID);
        return result == null || result.isEmpty() ? null : (VipAwardManage)result.get(0);
    }

    public VipAwardManage removeAward() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_AWARD_ITEM.AWARD_ID);
        return result == null || result.isEmpty() ? null : (VipAwardManage)result.get(0);
    }

    public void setAward(VipAwardManage value) {
        this.setFKBean(TableConstants.T_AWARD_ITEM.AWARD_ID, value);
    }

    public String getAwardTypeName() {
        StringValue value = (StringValue)getValue(TableConstants.T_AWARD_ITEM.AWARD_TYPE_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setAwardTypeName(String value) {
        setValue(TableConstants.T_AWARD_ITEM.AWARD_TYPE_NAME, StringValue.valueOf(value));
    }

    public long getAwardNum() {
        LongValue value = (LongValue)getValue(TableConstants.T_AWARD_ITEM.AWARD_NUM);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setAwardNum(Long value) {
        setValue(TableConstants.T_AWARD_ITEM.AWARD_NUM, LongValue.valueOf(value));
    }

    public String getAwardContent() {
        StringValue value = (StringValue)getValue(TableConstants.T_AWARD_ITEM.AWARD_CONTENT);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setAwardContent(String value) {
        setValue(TableConstants.T_AWARD_ITEM.AWARD_CONTENT, StringValue.valueOf(value));
    }

}