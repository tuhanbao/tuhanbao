package com.tuhanbao.study.lock.aqs;

import java.util.concurrent.TimeUnit;

import com.tuhanbao.study.thread.consumer_producer.RandomUtil;
import com.tuhanbao.util.thread.ThreadUtil;

public class AQSTest {
    
    public static void main(String args[]) {
        test();
    }

    public static Object test() {
        final Semaphore lock = new Semaphore(2);
        Runnable r = new Runnable() {
            
            @Override
            public void run() {
                try {
                    lock.acquire();
                    System.out.println(Thread.currentThread().getName() + " has get lock!");
                    ThreadUtil.sleep(RandomUtil.randomInt(300, 1000), TimeUnit.SECONDS);
                    lock.release();
                    System.out.println(Thread.currentThread().getName() + " has release lock!");
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        
        new Thread(r, "t1").start();
        ThreadUtil.sleep(10, TimeUnit.MILLISECONDS);
        new Thread(r, "t2").start();
        ThreadUtil.sleep(10, TimeUnit.MILLISECONDS);
        new Thread(r, "t3").start();
        ThreadUtil.sleep(10, TimeUnit.MILLISECONDS);
        new Thread(r, "t4").start();
        ThreadUtil.sleep(10, TimeUnit.MILLISECONDS);
        new Thread(r, "t5").start();
        ThreadUtil.sleep(10, TimeUnit.MILLISECONDS);
        new Thread(r, "t6").start();
        ThreadUtil.sleep(10, TimeUnit.MILLISECONDS);
        new Thread(r, "t7").start();
        return null;
    }

}
