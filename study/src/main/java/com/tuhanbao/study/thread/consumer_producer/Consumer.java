package com.tuhanbao.study.thread.consumer_producer;

import com.tuhanbao.util.log.LogManager;
import com.tuhanbao.util.thread.ThreadUtil;


public class Consumer extends Thread {
	
	private static int SPEED = 10;
	
	private static int NUM = 0;
	
	public Consumer() {
		super("consumer " + (++NUM));
	}
	
	public void run() {
		while (true) {
			Product product = ConsumerManager.getProduct();
			if (product == null) {
				ConsumerManager.waitProduct(this);
			}
			else {
				consume(product);
			}
		}
	}
	
	private void consume(Product product) {
		ThreadUtil.sleep(SPEED);
		LogManager.debug(this.getName() + " eat " + product);
	}
}
