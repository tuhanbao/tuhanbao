package com.tuhanbao.study.thread.consumer_producer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.tuhanbao.util.log.LogManager;



public class ConsumerManager {
	
	//消费者个数的上下限
	private static int MIN_SIZE = 2;
	private static int MAX_SIZE = 10;

	//产品个数的上下阈值，低于下限阈值，允许消费者休息。高于上限阈值，唤醒休息的消费者
	private static int PRODUCT_NUM_LOWER_THRESHOLD = 3;
	private static int PRODUCT_NUM_UPPER_THRESHOLD = 10;
	
	
	//每次递增的幅度
	private static int SIZE_INCREASE = 2;
	
	private static Object WORK_ROOM = new Object();
	
	private static Object REST_ROOM = new Object();
	
//	private static List<Consumer> restList = new ArrayList<Consumer>(MAX_SIZE);

	private static List<Consumer> consumerList = new ArrayList<Consumer>(MAX_SIZE);
	
	private static LinkedList<Product> products = new LinkedList<Product>();
	
	//连续低于下限阈值的次数
	private static int LOWER_THRESHOLD_TIME = 0;
	
	//如果连续低于下限阈值的次数等于这个，则替换一批consumer下去休息
	private static final int LOWER_THRESHOLD_TIME_THRESHOLD = 3;
	
	//即将去休息的消费者名额，谁先到谁先休息
	private static int READY_FOR_REST = 0;
	
	private static int restNum = 0;
	
	private static int workNum = 0;
	
	public static void start() {
		addConsumer(MIN_SIZE);

		new Thread(new ConsumerMonitor()).start();
	}

	private static void addConsumer(int num) {
		for (int i = 0; i < num; i++) {
			if (consumerList.size() >= MAX_SIZE) {
				LogManager.debug("no more consumer can be add!!");
				break;
			}
			Consumer consumer = new Consumer();
			consumerList.add(consumer);
			workNum++;
			consumer.start();
			LogManager.debug(consumer.getName() + " goto work.");
		}
	}
	
	private static void callConsumerWork() {
		synchronized (REST_ROOM) {
			if (restNum >= SIZE_INCREASE) {
				for (int i = 0; i < SIZE_INCREASE; i++) {
					REST_ROOM.notify();
					restNum--;
					workNum++;
					LogManager.debug("some one goto work.");
				}
			}
			else {
				for (int i = 0; i < restNum; i++) {
					REST_ROOM.notify();
					restNum--;
					workNum++;
				}
				addConsumer(SIZE_INCREASE - restNum);
			}
		}
	}

	private static void callConsumerRest() {
		if (workNum > MIN_SIZE) {
			if (workNum - MIN_SIZE >  SIZE_INCREASE) {
				READY_FOR_REST = SIZE_INCREASE;
			}
			else {
				READY_FOR_REST = workNum - MIN_SIZE;
			}
		}
	}

	public static void waitProduct(Consumer consumer) {
		try {
			if (READY_FOR_REST > 0) {
				synchronized (REST_ROOM) {
					//必须再判断
					if (READY_FOR_REST > 0) {
						READY_FOR_REST--;
						
						workNum--;
						restNum++;
						LogManager.debug(Thread.currentThread().getName() + " goto rest.");
						REST_ROOM.wait();
						return;
					}
				}
			}
			synchronized (WORK_ROOM) {
				WORK_ROOM.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Product getProduct() {
		synchronized (WORK_ROOM) {
			return products.poll();
		}
	}
	
	public static void addProduct(Product product) {
		synchronized (WORK_ROOM) {
			products.add(product);
			
			WORK_ROOM.notify();
		}
	}
	
	public static void checkConsumerState() {
		int size = products.size();
		
		if (size > PRODUCT_NUM_UPPER_THRESHOLD) {
			callConsumerWork();
			LOWER_THRESHOLD_TIME = 0;
		}
		else {
			if (size < PRODUCT_NUM_LOWER_THRESHOLD) LOWER_THRESHOLD_TIME++;
			else LOWER_THRESHOLD_TIME = 0;
			
			if (LOWER_THRESHOLD_TIME >= LOWER_THRESHOLD_TIME_THRESHOLD) {
				callConsumerRest();
				LOWER_THRESHOLD_TIME = 0;
			}
		}
		
		LogManager.debug("rest num : " + restNum + ",  workNum : " + workNum + ", total : " + consumerList.size()
				+ ", product size : " + products.size());
	}
}
