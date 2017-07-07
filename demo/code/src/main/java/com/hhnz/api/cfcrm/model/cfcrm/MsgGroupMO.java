package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import java.util.List;

public class MsgGroupMO extends ServiceBean {
    protected MsgGroupMO() {
        this(new MetaObject(TableConstants.T_MSG_GROUP.TABLE));
    }

    protected MsgGroupMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_MSG_GROUP.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_MSG_GROUP.ID, LongValue.valueOf(value));
    }

    public String getGroupName() {
        StringValue value = (StringValue)getValue(TableConstants.T_MSG_GROUP.GROUP_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setGroupName(String value) {
        setValue(TableConstants.T_MSG_GROUP.GROUP_NAME, StringValue.valueOf(value));
    }

    public int getSort() {
        IntValue value = (IntValue)getValue(TableConstants.T_MSG_GROUP.SORT);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setSort(Integer value) {
        setValue(TableConstants.T_MSG_GROUP.SORT, IntValue.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<MsgModel> getMsgModels() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_MSG_MODEL.GROUP_ID);
        return (List<MsgModel>)result;
    }

    @SuppressWarnings("unchecked")
    public List<MsgModel> removeMsgModels() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_MSG_MODEL.GROUP_ID);
        return (List<MsgModel>)result;
    }

    public void setMsgModels(List<MsgModel> value) {
        this.setFKBean(TableConstants.T_MSG_MODEL.GROUP_ID, value);
    }

}