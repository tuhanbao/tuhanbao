package com.tuhanbao.study;

public class Father extends AbstractFather {
	private int age;
	
	private String name;
	
	public Father(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void say() {
		System.out.println("I am father!");
	}
	
	public String toString() {
		return name;
	}
}
