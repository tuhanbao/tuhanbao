package com.sztx.se.core.quartz.config;

public class JobRunningInfo {

	/**
	 * 唯一标示
	 */
	private String id;

	/**
	 * sched名称
	 */
	private String schedName;

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * 任务组
	 */
	private String jobGroup;

	/**
	 * 任务的具体类名, 可以是全路径的java类, 也可以是被spring管理的简单类名
	 */
	private String jobClassName;

	/**
	 * 触发器的名称（如：每天2点触发库存对账）
	 */
	private String triggerName;

	/**
	 * 触发器分组
	 */
	private String triggerGroup;

	/**
	 * 执行者标示
	 */
	private String instanceName;

	/**
	 * 机器IP地址
	 */
	private String ip;

	/**
	 * 机器名称
	 */
	private String hostName;

	/**
	 * 执行开始时间
	 */
	private String startTime;

	/**
	 * 执行结束时间
	 */
	private String endTime;

	/**
	 * 处理时长(单位:毫秒)
	 */
	private long cost;

	/**
	 * 执行结果
	 */
	private String result;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchedName() {
		return schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public long getCost() {
		return cost;
	}

	public void setCost(long cost) {
		this.cost = cost;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "JobRunningInfo [id=" + id + ", schedName=" + schedName + ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", jobClassName=" + jobClassName
				+ ", triggerName=" + triggerName + ", triggerGroup=" + triggerGroup + ", instanceName=" + instanceName + ", ip=" + ip + ", hostName="
				+ hostName + ", startTime=" + startTime + ", endTime=" + endTime + ", cost=" + cost + ", result=" + result + "]";
	}

}
