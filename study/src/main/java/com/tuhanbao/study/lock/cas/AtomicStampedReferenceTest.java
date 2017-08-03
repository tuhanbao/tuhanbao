package com.tuhanbao.study.lock.cas;

import java.util.concurrent.atomic.AtomicStampedReference;

import com.tuhanbao.io.base.RandomUtil;
import com.tuhanbao.study.Child;
import com.tuhanbao.study.ITest;

public class AtomicStampedReferenceTest implements ITest {
	
	private AtomicStampedReference<Child> asr;
	
	private Child child;
	
	public AtomicStampedReferenceTest() {
		child = new Child("tubie");
		asr = new AtomicStampedReference<Child>(child, 0);
	}

	@Override
	public Object test() {
		new Thread(new AtomicStampedReferenceRunnable()).start();
		new Thread(new AtomicStampedReferenceRunnable()).start();
		return null;
	}
	


	private class AtomicStampedReferenceRunnable implements Runnable {
		public AtomicStampedReferenceRunnable() {
		}

		@Override
		public void run() {
			AtomicStampedReference<Child> asr = AtomicStampedReferenceTest.this.asr;
			Child child = AtomicStampedReferenceTest.this.child;
			String newName = RandomUtil.randomLetterAndNumberString(6);
			int stamp = asr.getStamp();
			child.setName(newName);
			if (stamp == 0) {
				System.out.println(newName + " " + stamp + "  " + asr.compareAndSet(child, child, stamp, stamp + 1));
			}
			else {
				System.out.println(newName + " " + stamp + "  " + asr.compareAndSet(child, child, stamp, stamp - 1));
			}
			System.out.println(asr.getReference().getName());
		}
		
	}
}
