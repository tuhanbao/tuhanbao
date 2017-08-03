package com.tuhanbao.study.lock.spinlock;

import com.tuhanbao.study.ITest;

public class TimeCost implements Lock, ITest {  
  
    private final Lock lock;  
    
    private Bean bean;
      
    public TimeCost(Lock lock){  
        this.lock = lock;  
        bean = new Bean();
    }  
      
    @Override  
    public void lock() {  
        long start = System.nanoTime();  
        lock.lock();  
        long duration = System.nanoTime() - start;  
        System.out.println(duration);
    }  
  
    @Override  
    public void unlock() {  
        lock.unlock();  
    }  
    
	@Override
	public Object test() {
		for(int i = 0; i < 100; i ++){  
            Thread t = new Thread(new Runnable(){  
      
                @Override  
                public void run() {  
                    method();  
                }  
                  
            });  
            t.start();  
        }  
		return null;
	}     
	
	public void method(){  
        this.lock();  
        bean.add();
        this.unlock();  
    }  
  
} 