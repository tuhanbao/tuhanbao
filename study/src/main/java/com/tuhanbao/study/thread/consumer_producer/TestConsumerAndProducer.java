package com.tuhanbao.study.thread.consumer_producer;
import java.util.Scanner;


public class TestConsumerAndProducer {
    
	private static Scanner input;

    public static void main(String args[]) {
		ConsumerManager.start();
		Producer producer = new Producer("boss");
		producer.start();
		
		input = new Scanner(System.in);
		while(true) {
			int i = input.nextInt();
			if (i == 1) {
				Producer.addSpeed();
			}
			else {
				Producer.slowSpeed();
			}
		}
	}
}
