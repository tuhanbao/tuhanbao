package com.tuhanbao.base.dataservice.filter;

public enum JoinType {
	LEFT_JOIN("LEFT JOIN"), RIGHT_JOIN("RIGHT JOIN"), INNER_JOIN("INNER JOIN");
	
	private String value;
	
	private JoinType(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
}
