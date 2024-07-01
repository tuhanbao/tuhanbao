/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.td.ca.base.util.mq;

import com.td.ca.base.util.objutil.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.security.auth.callback.Callback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 因为增删监听器的频率很低，而且大概率不会并发
 * 所以通过简单的拷贝赋值，解决并发冲突问题。
 */
@Slf4j
public class MQCenter {

    private static IMQTransport transport;

    private static Map<String, List<MQMessageListener>> topicListeners;

    private static List<MQMessageListener> allMessageListeners;

    /**
     * 只会有一个消费者，监听topic的人只会有一个人处理消息，适用于点对点消息处理
     */
    public static void sendMessage(String topic, Object message) {
        if (transport != null) {
            transport.sendMessage(topic, message);
        }
    }

    /**
     * 所有的消费者都会收到消息，在微服务场景下，相同的服务只会有一人处理消息。
     */
    public static void sendBroadcast(String topic, Object message) {
        if (transport != null) {
            transport.sendBroadcast(topic, message);
        }
    }

    public static void receiveMessage(String topic, Object message) {
        receiveMessage(topic, message, null);
    }

    public static void receiveMessage(String topic, Object message, MQConsumerCallback callback) {
        log.info("receive {} : {}", topic, message);
        try {
            if (topicListeners != null) {
                List<MQMessageListener> listeners = topicListeners.get(topic);
                if (listeners != null) {
                    for (MQMessageListener listener : listeners) {
                        listener.receiveMessage(message);
                    }
                }
            }

            if (allMessageListeners != null) {
                for (MQMessageListener listener : allMessageListeners) {
                    listener.receiveMessage(message);
                }
            }
            if (callback != null) {
                callback.call(null);
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.call(e);
            }
        }
    }

    public static void setTransport(IMQTransport transport) {
        MQCenter.transport = transport;
    }

    public synchronized static void addListener(MQMessageListener listener) {
        String topic = listener.getTopic();
        if (StringUtil.isEmpty(topic)) {
            addAllMsgListener(listener);
        } else {
            if (topicListeners == null) {
                topicListeners = new HashMap<>();
            }

            List<MQMessageListener> list = new ArrayList<>();
            List<MQMessageListener> oldList = topicListeners.get(topic);
            if (oldList != null) {
                list.addAll(oldList);
            }
            list.add(listener);
            topicListeners.put(topic, list);
        }
    }

    public synchronized static void addAllMsgListener(MQMessageListener listener) {
        List<MQMessageListener> list = new ArrayList<>();
        if (allMessageListeners != null) {
            list.addAll(allMessageListeners);
        }
        list.add(listener);
        allMessageListeners = list;
    }

    public synchronized static void removeListener(MQMessageListener listener) {
        String topic = listener.getTopic();
        if (StringUtil.isEmpty(topic)) {
            removeAllMsgListener(listener);
        } else {
            if (topicListeners == null) {
                return;
            }

            List<MQMessageListener> oldList = topicListeners.get(topic);
            if (oldList != null) {
                List<MQMessageListener> list = new ArrayList<>();
                list.addAll(oldList);
                list.remove(listener);
                if (list.isEmpty()) {
                    topicListeners.remove(topic);
                } else {
                    topicListeners.put(topic, list);
                }
            }
        }
    }

    /**
     * 因为增删监听器的频率很低，而且大概率不会并发
     * 所以通过简单的拷贝赋值，解决并发冲突问题。
     */
    public synchronized static void removeAllMsgListener(MQMessageListener listener) {
        if (allMessageListeners != null) {
            List<MQMessageListener> list = new ArrayList<>();
            list.addAll(allMessageListeners);
            list.remove(listener);
            allMessageListeners = list;
        }
    }
}
