package com.td.ca.base.util.mq;

import java.io.IOException;

public interface MQConsumerCallback {
    void call(Exception e);
}
