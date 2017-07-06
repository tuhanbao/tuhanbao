package com.tuhanbao.web.controller.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.db.table.data.DataValue;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.json.JsonUtil;

public class ResultBean {

    private int code = 0;

    private String msg;

    private Object data;

    public ResultBean() {
    }

    public ResultBean(Object data, String msg) {
        this(BaseErrorCode.NO_ERROR, data, msg);
    }

    public ResultBean(int code, Object data, String msg) {
        this.code = code;
        this.data = JsonUtil.toJSON(data);
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Map<String, Object> getJSONData(ServiceBean bean) {
        Map<String, Object> values = new HashMap<String, Object>(bean.getProperties().size());
        for (Entry<Column, DataValue> entry : bean.getProperties().entrySet()) {
            values.put(entry.getKey().getName(), entry.getValue().getValue4DB());
        }
        return values;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
