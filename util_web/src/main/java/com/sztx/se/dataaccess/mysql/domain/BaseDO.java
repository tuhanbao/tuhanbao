package com.sztx.se.dataaccess.mysql.domain;

import java.io.Serializable;
import java.util.Date;

public class BaseDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 281608583054364506L;

	protected String createUser;

	protected Date createTime;

	protected String updateUser;

	protected Date updateTime;

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
