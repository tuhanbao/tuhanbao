package com.tuhanbao.study.map;

import java.util.HashMap;
import java.util.Map;

public class HashMapTest {
	private String name;
	
	public HashMapTest(String name) {
		this.name = name;
	}

	public int hashCode() {
		return 2;
	}
	
	public boolean equals(Object o) {
		return ((HashMapTest)o).name.equals(this.name);
	}
	
	public static void main(String args[]) {
		final Map<Object, String> map = new HashMap<Object, String>(2);
		map.put(new Object(), "lhh");
		map.put(new HashMapTest("liuhanhui"), "lhh");
		map.put(new HashMapTest("wangshuo"), "wangshuo");
		
		new Thread() {
			public void run() {
				map.put(new HashMapTest(""), "16");
			}
		}.start();
		new Thread() {
			public void run() {
				map.put(new HashMapTest(""), "16");
			}
		}.start();
//		for (int i = 0; i < 1000000; i++) {
//			HashMapTest key = new HashMapTest();
//
//			if (i == 100) {
//				temp = key;
//			}
//		}
//		System.out.println(map.size());
//		long start = System.currentTimeMillis();
//		System.out.println(map.get(new HashMapTest("liuhanhui")));
//		System.out.println(System.currentTimeMillis() - start);
	}
}
