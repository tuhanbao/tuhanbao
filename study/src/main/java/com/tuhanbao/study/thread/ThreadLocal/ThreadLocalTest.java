package com.tuhanbao.study.thread.ThreadLocal;


public class ThreadLocalTest implements com.tuhanbao.study.ITest {

	ThreadLocal<String> tl = new ThreadLocal<String>();
	
	@Override
	public Object test() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				printName();
			}
		}, "t1").start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				printName();
			}
		}, "t2").start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				printName();
			}
		}, "t3").start();
		return null;
	}

	private void printName() {
		String name = Thread.currentThread().getName();
		tl.set(name);
		System.out.println(name + " name is " + tl.get());
	}
}
