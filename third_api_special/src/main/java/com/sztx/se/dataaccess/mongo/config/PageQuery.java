package com.sztx.se.dataaccess.mongo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * mongo分页对象
 * 
 * @author zhihongp
 * 
 */
public class PageQuery {

	private int pageNo;

	private int pageSize;

	private int start;

	private int end;

	private int totalCount = -1;

	private List<Order> orderList;

	/**
	 * 分页对象
	 * 
	 * @param pageNo 页码，从1开始
	 * @param pageSize 每页个数
	 */
	public PageQuery(int pageNo, int pageSize) {
		this(pageNo, pageSize, null);
	}

	/**
	 * 分页对象
	 * 
	 * @param pageNo 页码，从1开始
	 * @param pageSize 每页个数
	 * @param orderList 排序对象
	 */
	public PageQuery(int pageNo, int pageSize, List<Order> orderList) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.orderList = orderList;

		if (pageNo == 1) {
			this.start = 0;
		} else {
			this.start = (pageNo - 1) * pageSize;
		}

		this.end = this.start + pageSize - 1;
	}
	
	/**
	 * 分页对象
	 * 
	 * @param orderList 排序对象
	 * @param skip 游标起始值，从0开始
	 * @param limit 每页个数
	 */
	public PageQuery(List<Order> orderList, int skip, int limit) {
		this.pageSize = limit;
		this.orderList = orderList;

		if (skip < 0) {
			skip = 0;
		}

		this.start = skip;
		this.pageNo = (this.start % this.pageSize == 0) ? this.start / this.pageSize : (this.start / this.pageSize + 1);
		this.end = this.start + pageSize - 1;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
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

	public List<Sort> getSortList() {
		List<Sort> sortList = new ArrayList<Sort>();

		if (orderList != null && !orderList.isEmpty()) {
			for (Order order : orderList) {
				if (order.getOrderBy().equalsIgnoreCase(Order.SORT_RULE_DESC)) {
					Sort sort = new Sort(Direction.DESC, order.getOrderBy());
					sortList.add(sort);
				} else {
					Sort sort = new Sort(Direction.ASC, order.getOrderBy());
					sortList.add(sort);
				}
			}
		}

		return sortList;
	}

	@Override
	public String toString() {
		return "PageQuery [pageNo=" + pageNo + ", pageSize=" + pageSize + ", start=" + start + ", end=" + end + ", totalCount=" + totalCount + ", orderList="
				+ orderList + "]";
	}

}
