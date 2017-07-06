/** 
 * Copyright(C) 2010-2011 xmall WuXi LTD. All Rights Reserved.                               
 * Project: <FileUpload>
 * Module ID: <0001>
 * Comments: <类描述>                          
 * JDK version used: <JDK1.6.0_25>                              
 * Author: baieqiuzhu        
 * Create Date：Sep 26, 2013 5:37:23 PM
 * Modified By: baieqiuzhu                                
 * Modified Date: Sep 26, 2013 5:37:23 PM                                   
 * Why & What is modified: <修改原因描述>    
 * Version: V1.0                  
 */

package com.tuhanbao.base.dataservice.filter.page;

/**
 * 
 * @ClassName: Page
 * @Description: Page工具类
 * @author wangbing
 * @date Oct 7, 2013 11:07:19 AM
 * 
 */
public class Page {

	/**
	 * 默认每页记录数
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 当前页码 
	 */
	protected int pageNum;
	
	/**
	 * 每页条数
	 */
	protected int numPerPage;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 总记录数
	 */
	protected long totalCount = 0;

	
	public Page() {
	    this(1, DEFAULT_PAGE_SIZE);
	}
	
	/**
	 * 
	 * @param pageNum  第几页
	 * @param pageSize  每页的条数
	 */
	public Page(int pageNum, int pageSize) {
	    this.pageNum = pageNum;
	    this.numPerPage = pageSize;
	}

	/**
	 * 返回 pageNum 的值
	 * 
	 * @return pageNum
	 */
	public int getPageNum() {
		if (pageNum > totalPage) {
			pageNum = totalPage;
		}
		return pageNum;
	}

	/**
	 * 设置 pageNum 的值
	 * 
	 * @param pageNum
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum > 0 ? pageNum : 1;
	}

	/**
	 * 返回 numPerPage 的值
	 * 
	 * @return numPerPage
	 */
	public int getNumPerPage() {
		return numPerPage;
	}

	/**
	 * 设置 numPerPage 的值
	 * 
	 * @param numPerPage
	 */
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage > 0 ? numPerPage : 10;
	}


	/**
	 * 返回 totalPage 的值
	 * 
	 * @return totalPage
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 设置 totalPage 的值
	 * 
	 * @param totalPage
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}


	/**
	 * 返回 totalCount 的值
	 * 
	 * @return totalCount
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置 totalCount 的值
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
		totalPage = (int) (totalCount - 1) / this.numPerPage + 1;
	}

	/**
	 * 获取当页的第一条数据行数
	 * @return
	 */
	public int getBegin() {
		return (pageNum - 1) * numPerPage;
	}

	/**
	 * 获取当页的最后一条一条数据行数
	 * @return
	 */
	public int getEnd() {
		return (pageNum * numPerPage > Integer.parseInt(String.valueOf(totalCount))) ? Integer.parseInt(String.valueOf(totalCount)) : pageNum * numPerPage;
	}

}
