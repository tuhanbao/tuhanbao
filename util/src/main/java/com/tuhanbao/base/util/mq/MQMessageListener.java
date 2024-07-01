/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.td.ca.base.util.mq;

public interface MQMessageListener {
    void receiveMessage(Object message);

    String getTopic();

    /**
     * 不为空代表是广播消息，需要分组获取消息
     *
     * @return
     */
    default String getGroup() {
        return null;
    }

    default boolean isBroadCast() {
        return false;
    }
}
