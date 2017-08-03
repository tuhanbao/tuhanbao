package com.tuhanbao.study.thread.consumer_producer;

public class Baozi implements Product {

	private int i;
	
	public Baozi(int i) {
		this.i = i;
	}
	
	public int getI() {
		return i;
	}
	
	public String toString() {
		return "baozi" + i;
	}
}
