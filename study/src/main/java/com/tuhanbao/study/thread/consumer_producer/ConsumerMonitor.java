package com.tuhanbao.study.thread.consumer_producer;

import com.tuhanbao.util.thread.ThreadUtil;

public class ConsumerMonitor implements Runnable {

	private static final int TIME_GAP = 10;
	
	@Override
	public void run() {
		while (true) {
			ConsumerManager.checkConsumerState();
			ThreadUtil.sleep(TIME_GAP);
		}
	}
}
