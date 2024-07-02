package com.td.ca.web.util.mq;

import com.alibaba.fastjson2.JSONObject;
import com.td.ca.base.Constants;
import com.td.ca.base.dataservice.cache.Event;
import com.td.ca.base.util.objutil.ArrayUtil;
import com.td.ca.web.util.spring.SpringUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class LocalCacheTableMQListener<E> implements PodTopicMessageListener {

    protected Type type;

    private Type getMyType() {
        if (type == null) {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            type = ArrayUtil.getValue(params, 0);
        }
        return type;
    }

    @Override
    public void receiveMessage(Object message) {
        JSONObject json = (JSONObject) message;
        Event event = Event.valueOf(json.getString("event"));
        E e = json.getObject(Constants.DATA, getMyType());

        if (event == Event.DELETE) {
            this.delete(e);
        } else if (event == Event.ADD || event == Event.UPDATE) {
            this.save(e, false);
        } else if (event == Event.UPDATE_SELECTIVE) {
            this.save(e, true);
        } else {
            // 其余情况就全刷新
            this.refreshData();
        }
    }

    protected abstract void delete(E e);

    protected abstract void save(E e, boolean b);

    protected abstract void refreshData();

    /**
     * 如果要监听的表不是本service，需要自行覆盖
     * @return
     */
    protected String getTableServiceName() {
        return SpringUtil.getServiceName();
    }

    @Override
    public String getTopic() {
        return MQUtil.getTopic(getTableName(), getTableServiceName());
    }

    protected abstract String getTableName();
}
