package com.tuhanbao.study.lock.spinlock;  
  
import java.util.concurrent.atomic.AtomicBoolean;  
  
/** 
 * 测试-设置自旋锁，使用AtomicBoolean原子变量保存状态 
 * 每次都使用getAndSet原子操作来判断锁状态并尝试获取锁 
 * 缺点是getAndSet底层使用CAS来实现，一直在修改共享变量的值，会引发缓存一致性流量风暴 
 * **/  
public class TASLock implements Lock{  
    private AtomicBoolean mutex = new AtomicBoolean(false);  
      
    @Override  
    public void lock() {  
        // getAndSet方法会设置mutex变量为true，并返回mutex之前的值  
        // 当mutex之前是false时才返回，表示获取锁  
        // getAndSet方法是原子操作，mutex原子变量的改动对所有线程可见  
        while(mutex.getAndSet(true)){  
              
        }  
    }  
  
    @Override  
    public void unlock() {  
        mutex.set(false);  
    }  
  
    public String toString(){  
        return "TASLock";  
    }  
}  