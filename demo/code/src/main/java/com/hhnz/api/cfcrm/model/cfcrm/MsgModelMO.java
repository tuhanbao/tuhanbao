package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.LongValue;
import java.util.List;
import com.tuhanbao.base.util.db.table.data.StringValue;

public class MsgModelMO extends ServiceBean {
    protected MsgModelMO() {
        this(new MetaObject(TableConstants.T_MSG_MODEL.TABLE));
    }

    protected MsgModelMO(MetaObject mo) {
        super(mo);
    }

    public long getId() {
        LongValue value = (LongValue)getValue(TableConstants.T_MSG_MODEL.ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setId(Long value) {
        setValue(TableConstants.T_MSG_MODEL.ID, LongValue.valueOf(value));
    }

    public long getGroupId() {
        LongValue value = (LongValue)getValue(TableConstants.T_MSG_MODEL.GROUP_ID);
        if (value == null) return 0;
        else return value.getValue();
    }

    public void setGroupId(Long value) {
        setValue(TableConstants.T_MSG_MODEL.GROUP_ID, LongValue.valueOf(value));
    }

    public MsgGroup getGroup() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_MSG_MODEL.GROUP_ID);
        return result == null || result.isEmpty() ? null : (MsgGroup)result.get(0);
    }

    public MsgGroup removeGroup() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_MSG_MODEL.GROUP_ID);
        return result == null || result.isEmpty() ? null : (MsgGroup)result.get(0);
    }

    public void setGroup(MsgGroup value) {
        this.setFKBean(TableConstants.T_MSG_MODEL.GROUP_ID, value);
    }

    public String getContent() {
        StringValue value = (StringValue)getValue(TableConstants.T_MSG_MODEL.CONTENT);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setContent(String value) {
        setValue(TableConstants.T_MSG_MODEL.CONTENT, StringValue.valueOf(value));
    }

}