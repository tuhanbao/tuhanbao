package com.sztx.se.core.tbschedule.config;

import java.util.Date;

public class TaskRunningInfo {
	                                              
	public static final String FETCH_DATA_NUM = "FetchDataNum";
	
	public static final String DEAL_DATA_SUCESS = "DealDataSucess";
	
	public static final String DEAL_DATA_FAIL = "DealDataFail";
	
	public static final String DEAL_SPEND_TIME = "DealSpendTime";
	
	/**
	 * 全局唯一标示(当前执行线程的UUID)
	 */
	private long id;

	/**
	 * 调度器名字
	 */
	private String scheduleName;

	/**
	 * 任务名字
	 */
	private String taskName;

	/**
	 * 机器IP地址
	 */
	private String ip;

	/**
	 * 机器名称
	 */
	private String hostName;

	/**
	 * 处理线程数量
	 */
	private int threadNum;

	/**
	 * 开始时间
	 */
	private Date registerTime;

	/**
	 * 最后一次心跳通知时间
	 */
	private Date lastHeartBeatTime;

	/**
	 * 最后一次取数据时间
	 */
	private Date lastFetchDataTime;

	/**
	 * 读取的任务数量
	 */
	private long fetchDataNum;

	/**
	 * 处理成功的任务数量
	 */
	private long dealSucessNum;

	/**
	 * 处理失败的任务数量
	 */
	private long dealFailNum;

	/**
	 * 处理时长(单位:毫秒)
	 */
	private long dealSpendTime;

	/**
	 * 数据版本号
	 */
	private long version;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}

	public void setLastHeartBeatTime(Date lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}

	public Date getLastFetchDataTime() {
		return lastFetchDataTime;
	}

	public void setLastFetchDataTime(Date lastFetchDataTime) {
		this.lastFetchDataTime = lastFetchDataTime;
	}

	public long getFetchDataNum() {
		return fetchDataNum;
	}

	public void setFetchDataNum(long fetchDataNum) {
		this.fetchDataNum = fetchDataNum;
	}

	public long getDealSucessNum() {
		return dealSucessNum;
	}

	public void setDealSucessNum(long dealSucessNum) {
		this.dealSucessNum = dealSucessNum;
	}

	public long getDealFailNum() {
		return dealFailNum;
	}

	public void setDealFailNum(long dealFailNum) {
		this.dealFailNum = dealFailNum;
	}

	public long getDealSpendTime() {
		return dealSpendTime;
	}

	public void setDealSpendTime(long dealSpendTime) {
		this.dealSpendTime = dealSpendTime;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "TaskRunningInfo [id=" + id + ", scheduleName=" + scheduleName + ", taskName=" + taskName + ", ip=" + ip + ", hostName=" + hostName
				+ ", threadNum=" + threadNum + ", registerTime=" + registerTime + ", lastHeartBeatTime=" + lastHeartBeatTime + ", lastFetchDataTime="
				+ lastFetchDataTime + ", fetchDataNum=" + fetchDataNum + ", dealSucessNum=" + dealSucessNum + ", dealFailNum=" + dealFailNum
				+ ", dealSpendTime=" + dealSpendTime + ", version=" + version + "]";
	}

}
