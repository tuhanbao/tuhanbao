package com.tuhanbao.study.thread.consumer_producer;

import com.tuhanbao.util.log.LogManager;
import com.tuhanbao.util.thread.ThreadUtil;


public class Producer extends Thread {
	private static int NUM = 1;
	
	private static int SPEED = 5;
	
	public Producer(String name) {
		super("produer " + name);
	}
	
	public void run() {
		while (true) {
			ConsumerManager.addProduct(createBaozi());
		}
	}
	
	private Product createBaozi() {
		ThreadUtil.sleep(SPEED);
		Product baozi = new Baozi(NUM++);
		LogManager.debug(this.getName() + " create " + baozi);
		return baozi;
	}
	
	public static void addSpeed() {
		if (SPEED > 1) {
			SPEED--;
		}
	}
	
	public static void slowSpeed() {
		SPEED++;
	}
}
