/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.td.ca.base.util.mq;

public interface IMQTransport {
    void sendMessage(String topic, Object message);

    void sendBroadcast(String topic, Object message);
}
