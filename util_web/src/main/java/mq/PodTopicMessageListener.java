package com.td.ca.web.util.mq;

import com.td.ca.base.util.mq.MQMessageListener;
import com.td.ca.base.util.objutil.RandomUtil;
import com.td.ca.base.util.snowflake.SnowFlakeUtil15;
import com.td.ca.web.util.spring.SpringUtil;

public interface PodTopicMessageListener extends MQMessageListener {
    @Override
    default String getGroup() {
        int nodeId = 0;
        try {
            nodeId = SnowFlakeUtil15.getNodeId();
        } catch (Exception e) {
            nodeId = RandomUtil.randomInt(0, Integer.MAX_VALUE - 1);
        }
        return SpringUtil.getServiceName() + "-" + nodeId;
    }

    @Override
    default boolean isBroadCast() {
        return true;
    }
}
