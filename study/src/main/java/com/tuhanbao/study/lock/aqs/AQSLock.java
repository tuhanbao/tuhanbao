package com.tuhanbao.study.lock.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import com.tuhanbao.study.ITest;

public class AQSLock extends AbstractQueuedSynchronizer {

    private static final long serialVersionUID = -4649604678774109286L;

    public void lock() {
        this.acquire(0);
    }

    public void unlock() {
        this.release(0);
    }

    public boolean tryAcquire(int ignore) {
        boolean result = compareAndSetState(0, 1);
        System.out.println(Thread.currentThread().getName() + " try acquire " + result);
        return result;
    }

    public boolean tryRelease(int ignore) {
        setState(0);
        return true;
    }

}
