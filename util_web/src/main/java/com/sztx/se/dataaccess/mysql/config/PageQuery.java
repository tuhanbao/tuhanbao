package com.sztx.se.dataaccess.mysql.config;

import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 分页工具
 * 
 * @author zhihongp
 * 
 */
public class PageQuery {

	private static final int MAX_PAGE_NO = 5000;

	private int pageNo;

	private int pageSize;

	private int start;

	private int end;

	/**
	 * total count
	 */
	private int totalCount = -1;

	private String orderBy;

	private String orderType;

	/**
	 * 单页最多条数默认5000
	 */
	private int maxPageNo;

	public PageQuery() {

	}

	public PageQuery(int pageNo, int pageSize) {
		this(pageNo, pageSize, MAX_PAGE_NO, null, "asc");
	}

	public PageQuery(int pageNo, int pageSize, int maxPageNo) {
		this(pageNo, pageSize, maxPageNo, null, "asc");
	}

	public PageQuery(int pageNo, int pageSize, int maxPageNo, String orderBy, String orderType) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.orderBy = orderBy;
		this.maxPageNo = maxPageNo;
		this.setOrderType(orderType);

		if (pageNo == 1) {
			this.start = 0;
		} else {
			this.start = (pageNo - 1) * pageSize;
		}

		this.end = this.start + pageSize - 1;
	}

	public PageQuery(int pageNo, int pageSize, String orderBy, String orderType) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.orderBy = orderBy;
		this.maxPageNo = MAX_PAGE_NO;
		this.setOrderType(orderType);

		if (pageNo == 1) {
			this.start = 0;
		} else {
			this.start = (pageNo - 1) * pageSize;
		}

		this.end = this.start + pageSize - 1;
	}

	public PageQuery(String orderBy, String orderType, int start, int pageSize) {
		this.pageSize = pageSize;
		this.orderBy = orderBy;
		this.maxPageNo = MAX_PAGE_NO;
		this.setOrderType(orderType);
		;
		if (start < 0) {
			start = 0;
		}
		this.start = start;

		this.pageNo = this.start % this.pageSize == 0 ? this.start / this.pageSize : (this.start / this.pageSize + 1);

		this.end = this.start + pageSize - 1;
	}

	public PageQuery(int maxPageNo, String orderBy, String orderType, int start, int pageSize) {
		this.maxPageNo = maxPageNo;
		this.pageSize = pageSize;
		this.orderBy = orderBy;
		this.setOrderType(orderType);
		;
		if (start < 0) {
			start = 0;
		}
		this.start = start;

		this.pageNo = this.start % this.pageSize == 0 ? this.start / this.pageSize : (this.start / this.pageSize + 1);

		this.end = this.start + pageSize - 1;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		if (this.maxPageNo == 0) {
			this.maxPageNo = MAX_PAGE_NO;
		}

		if (pageSize > this.maxPageNo) {
			pageSize = this.maxPageNo;
		}

		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		if (totalCount > 0) {
			return Math.min(end, totalCount - 1);
		}
		return end;
	}

	public int getCount() {
		int count = getEnd() - getStart();
		return count < 0 ? 0 : count + 1;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		if (!StringUtil.isEmpty(orderType)) {
			validateOrderType(orderType);
		}

		this.orderType = orderType;
	}

	private void validateOrderType(String orderType) {
		if (!"asc".equalsIgnoreCase(orderType) && !"desc".equalsIgnoreCase(orderType)) {
			throw new MyException("错误的排序类型，orderType：" + orderType);
		}
	}

	@Override
	public String toString() {
		return "PageQuery [pageNo=" + pageNo + ", pageSize=" + pageSize + ", start=" + start + ", end=" + end + ", totalCount=" + totalCount + ", orderBy="
				+ orderBy + ", orderType=" + orderType + ", maxPageNo=" + maxPageNo + "]";
	}

	public static void main(String[] args) {
		printMsg(new PageQuery(1, 20));
		printMsg(new PageQuery(38, 20));
		PageQuery query = new PageQuery(38, 20);
		query.setTotalCount(750);
		printMsg(query);
		printMsg(new PageQuery("name", "asc", 81, 10));
		printMsg(new PageQuery("name", "asc", 80, 10));
	}

	private static void printMsg(PageQuery query) {
		System.out.println(query.toString() + "    count:" + query.getCount());
	}

}
