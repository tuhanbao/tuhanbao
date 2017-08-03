package com.tuhanbao.study.unsafe;

import com.tuhanbao.util.exception.MyException;

public class UnsafeBean {
	private int id;
	
	private String name;
	
	public static final int PI = 3;
	
	public UnsafeBean() {
		throw new MyException("cannot invoke default construct method");
	}

	public UnsafeBean(int id, String name) {
		if (id == 1) {
			throw new MyException("cannot set id 1");
		}
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
