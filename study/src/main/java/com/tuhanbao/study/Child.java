package com.tuhanbao.study;

public class Child extends Father {
	public Child(String name) {
		super(name);
	}

	@Override
	public void say() {
		System.out.println("I am child!");
	}
}
