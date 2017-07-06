package com.tuhanbao.web.controller.helper;

public class Pagination {

	private long totalNum;
	
	private Object dataList;

	public Pagination(){
		
	}
	
	public Pagination(long totalNum, Object dataList) {
		super();
		this.totalNum = totalNum;
		this.dataList = dataList;
	}

	public long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}

	public Object getDataList() {
		return dataList;
	}

	public void setDataList(Object dataList) {
		this.dataList = dataList;
	}
	
}
