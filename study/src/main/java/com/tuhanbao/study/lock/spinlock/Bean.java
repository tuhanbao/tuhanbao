package com.tuhanbao.study.lock.spinlock;

public class Bean {
	private int i = 0;
	
	public void add() {
		Object o = null;
		for (int j = 1; j < 100000; j++) {
			o = new Object();
		}
		sdsOb(o);
		i++;
	}
	
	private void sdsOb(Object o) {
		
	}
	
	public int get() {
		 return i;
	}
}
