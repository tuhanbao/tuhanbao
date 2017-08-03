package com.tuhanbao.study.lock.aqs;

import java.util.concurrent.TimeUnit;

import com.tuhanbao.study.thread.consumer_producer.RandomUtil;
import com.tuhanbao.util.thread.ThreadUtil;

public class TestRunnable implements Runnable {
    public TestRunnable(String name) {
        
    }

    @Override
    public void run() {
        ThreadUtil.sleep(RandomUtil.randomInt(5, 10), TimeUnit.SECONDS);
    }
    
}
