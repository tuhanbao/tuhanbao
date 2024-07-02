package {package};

import com.alibaba.fastjson2.JSONObject;
import {base_project_head}.base.Constants;
import {base_project_head}.base.dataservice.ServiceBean;
import {base_project_head}.base.dataservice.cache.Event;
import {base_project_head}.base.util.cache.CacheManager;
import {base_project_head}.base.util.db.table.data.AbstractDataValue;
import {base_project_head}.base.util.json.JSONUtil;
import {base_project_head}.base.util.objutil.ArrayUtil;
import {base_project_head}.base.util.objutil.RandomUtil;
import {base_project_head}.base.util.thread.ScheduledThreadManager;
import {base_project_head}.web.util.mq.MQUtil;
import {base_project_head}.web.util.mq.PodTopicMessageListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalCacheServiceImpl<E extends ServiceBean> extends ServiceImpl<E> implements PodTopicMessageListener {

    protected Type type;

    public void init() {
        super.init();
        // 错峰执行
        ScheduledThreadManager.executeOnTimer(4, RandomUtil.randomInt(1, 59), RandomUtil.randomInt(1, 59),
                () -> this.refreshData());
    }

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
        if (message == null) {
            return;
        }

        JSONObject json = (JSONObject) JSONUtil.parse(message.toString());
        Event event = Event.valueOf(json.getString("event"));
        E e = json.getObject(Constants.DATA, getMyType());

        if (event == Event.DELETE) {
            CacheManager.getLocalCacheManagerImpl().delete(this.getCacheTable(), e.getKeyValue().toString());
        } else if (event == Event.ADD) {
            CacheManager.getLocalCacheManagerImpl().add(e);
        } else if (event == Event.UPDATE) {
            CacheManager.getLocalCacheManagerImpl().update(e, false);
        } else if (event == Event.UPDATE_SELECTIVE) {
            CacheManager.getLocalCacheManagerImpl().update(e, true);
        } else {
            // 其余情况就全刷新
            refreshData();
        }
    }

    private void refreshData() {
        List<E> list = this.select(null);
        Map<String, Object> datas = new HashMap<>();
        for (E item : list) {
            AbstractDataValue keyValue = item.getKeyValue();
            datas.put(keyValue.toString(), item);
        }
        CacheManager.getLocalCacheManagerImpl().init(this.getCacheTable(), datas);
    }

    @Override
    public String getTopic() {
        return MQUtil.getTopic(this.getCacheTable());
    }
}
