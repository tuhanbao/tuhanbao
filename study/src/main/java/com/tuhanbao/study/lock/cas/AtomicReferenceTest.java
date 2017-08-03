package com.tuhanbao.study.lock.cas;

import java.util.concurrent.atomic.AtomicReference;

import com.tuhanbao.study.Child;
import com.tuhanbao.study.ITest;

public class AtomicReferenceTest implements ITest {
	
	private AtomicReference<Child> asr;
	
	
	public AtomicReferenceTest() {
		asr = new AtomicReference<Child>();
	}

	@Override
	public Object test() {
		new Thread(new AtomicReferenceRunnable(), "t1").start();
		new Thread(new AtomicReferenceRunnable(), "t2").start();
		new Thread(new AtomicReferenceRunnable(), "t3").start();
		new Thread(new AtomicReferenceRunnable(), "t4").start();
		new Thread(new AtomicReferenceRunnable(), "t5").start();
		return null;
	}
	


	private class AtomicReferenceRunnable implements Runnable {
		public AtomicReferenceRunnable() {
		}

		@Override
		public void run() {
			AtomicReference<Child> asr = AtomicReferenceTest.this.asr;
			Child child = asr.getAndSet(new Child(Thread.currentThread().getName()));
			System.out.println(Thread.currentThread().getName() + " getPreChild : " + child);
		}
		
	}
}
