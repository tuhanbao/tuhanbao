package com.sztx.se.core.quartz.config;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * Quartz参数类(Quartz通过：schedName + jobName + jobGroup 作为任务唯一标识,
 * 通过jobClassName确定具体的任务执行类)
 * 
 * @author zhihongp
 *
 */
@JSONType(orders = { "schedName", "jobName", "jobGroup", "jobClassName", "description", "isRecovery", "triggerName", "triggerGroup", "isCronTrigger",
		"expression", "prevFireTime", "nextFireTime", "startAt", "endAt", "status", "extraInfo" })
public class QuartzParameter {

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
	 * 任务的描述
	 */
	private String description;

	/**
	 * 任务中断（如断电）是否是重新执行
	 */
	private boolean isRecovery;

	/**
	 * 触发器的名称（如：每天2点触发库存对账）
	 */
	private String triggerName;

	/**
	 * 触发器分组
	 */
	private String triggerGroup;

	/**
	 * 是否是cron的表达式
	 */
	private boolean isCronTrigger;

	/**
	 * 任务触发时间的表达式(isCronTrigger等于true时为cron的表达式，isCronTrigger等于false时为间隔时间，单位秒)
	 */
	private String expression;

	/**
	 * 上次触发时间
	 */
	private Date prevFireTime;

	/**
	 * 下次触发时间
	 */
	private Date nextFireTime;

	/**
	 * 任务开始时间
	 */
	private Date startAt;

	/**
	 * 任务结束时间
	 */
	private Date endAt;

	/**
	 * 触发器状态
	 */
	private String status;

	/**
	 * 自定义扩展信息
	 */
	private Map<String, String> extraInfo;

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getIsRecovery() {
		return isRecovery;
	}

	public void setIsRecovery(boolean isRecovery) {
		this.isRecovery = isRecovery;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean getIsCronTrigger() {
		return isCronTrigger;
	}

	public void setIsCronTrigger(boolean isCronTrigger) {
		this.isCronTrigger = isCronTrigger;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
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

	public Date getPrevFireTime() {
		return prevFireTime;
	}

	public void setPrevFireTime(Date prevFireTime) {
		this.prevFireTime = prevFireTime;
	}

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public Map<String, String> getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(Map<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}

	public String getSchedName() {
		return schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}

	@Override
	public String toString() {
		return "QuartzParameter [schedName=" + schedName + ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", jobClassName=" + jobClassName
				+ ", description=" + description + ", isRecovery=" + isRecovery + ", triggerName=" + triggerName + ", triggerGroup=" + triggerGroup
				+ ", isCronTrigger=" + isCronTrigger + ", expression=" + expression + ", prevFireTime=" + prevFireTime + ", nextFireTime=" + nextFireTime
				+ ", startAt=" + startAt + ", endAt=" + endAt + ", status=" + status + ", extraInfo=" + extraInfo + "]";
	}

}
