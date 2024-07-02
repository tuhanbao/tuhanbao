package com.td.ca.web.util.mq;

import com.td.ca.base.util.mq.MQMessageListener;
import com.td.ca.web.util.spring.SpringUtil;

public interface ServiceTopicMessageListener extends MQMessageListener {

    @Override
    default String getGroup() {
        return SpringUtil.getServiceName();
    }
}
