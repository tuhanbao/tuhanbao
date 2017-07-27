package com.sztx.se.core.tbschedule.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zhihongp
 *
 */
public class ScheduleParameter {

	public static String STATUS_RESUME = "resume";
	public static String STATUS_PAUSE = "pause";
	public static String TASKKIND_STATIC = "static";
	public static String TASKKIND_DYNAMIC = "dynamic";
	public static int SLEEP_MODEL = 0;
	public static int NOTSLEEP_MODEL = 1;

	/**
	 * 调度器名字
	 */
	private String scheduleName;

	/**
	 * 任务名称(scheduleName+taskName=全局唯一标示)
	 */
	private String taskName;

	/**
	 * 任务的具体类被spring管理的bean
	 */
	private String taskBeanName;

	/**
	 * 心跳频率(毫秒), 默认3秒
	 */
	private int heartBeatRate = 3000;

	/**
	 * 假定服务死亡间隔(毫秒), 默认30秒
	 */
	private int judgeDeadInterval = 30000;

	/**
	 * 线程数, 默认10
	 */
	private int threadNumber = 10;

	/**
	 * 每次获取数据量, 默认100
	 */
	private int fetchDataNumber = 100;

	/**
	 * 每次执行数量(只在批量任务才生效，单个任务每次执行一条), 默认和每次获取数据量一样，即100
	 */
	private int executeNumber = 100;

	/**
	 * 没有数据时休眠时长(毫秒), 默认1秒
	 */
	private int sleepTimeNoData = 1000;

	/**
	 * 每次处理完数据后休眠时间(毫秒), 默认0秒
	 */
	private int sleepTimeInterval = 0;

	/**
	 * 执行开始时间, cron表达式(如果不设置，调度启动时即实时执行任务调度)
	 */
	private String startTime;

	/**
	 * 执行结束时间, cron表达式(如果不设置，表示取不到数据就停止)
	 */
	private String endTime;

	/**
	 * 单线程组最大任务项，每一组线程能分配的最大任务数量，避免在随着机器的减少把正常的服务器压死，0或者空表示不限制
	 */
	private int maxTaskItemsOfOneThreadGroup = 0;

	/**
	 * 清除过期环境信息的时间间隔,以天为单位
	 */
	private double expireOwnSignInterval = 1;

	/**
	 * 任务项列表, 默认0到9共10项
	 */
	private List<String> taskItemList;

	/**
	 * 自定义扩展信息
	 */
	private String extraInfo;

	/**
	 * 服务状态: pause,resume
	 */
	private String status;

	/**
	 * 处理模式(0-SLEEP, 1-NOTSLEEP), 默认SLEEP模式
	 */
	private int processType = SLEEP_MODEL;

	/**
	 * 策略名称(全局唯一标示)
	 */
	private String strategyName;

	/**
	 * 策略类型(0-Schedule, 1-JAVA, 2-Bean), 默认Schedule模式
	 */
	private int strategyType = 0;

	/**
	 * 单JVM最大线程组数量, 默认1
	 */
	private int numOfSingleServer = 1;

	/**
	 * 最大线程组数量, 默认10
	 */
	private int assignNum = 10;

	/**
	 * 调度的服务器ip列表
	 */
	private List<String> ipList;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskBeanName() {
		return taskBeanName;
	}

	public void setTaskBeanName(String taskBeanName) {
		this.taskBeanName = taskBeanName;
	}

	public int getHeartBeatRate() {
		return heartBeatRate;
	}

	public void setHeartBeatRate(int heartBeatRate) {
		this.heartBeatRate = heartBeatRate;
	}

	public int getJudgeDeadInterval() {
		return judgeDeadInterval;
	}

	public void setJudgeDeadInterval(int judgeDeadInterval) {
		this.judgeDeadInterval = judgeDeadInterval;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	public int getFetchDataNumber() {
		return fetchDataNumber;
	}

	public void setFetchDataNumber(int fetchDataNumber) {
		this.fetchDataNumber = fetchDataNumber;
	}

	public int getExecuteNumber() {
		return executeNumber;
	}

	public void setExecuteNumber(int executeNumber) {
		this.executeNumber = executeNumber;
	}

	public int getSleepTimeNoData() {
		return sleepTimeNoData;
	}

	public void setSleepTimeNoData(int sleepTimeNoData) {
		this.sleepTimeNoData = sleepTimeNoData;
	}

	public int getSleepTimeInterval() {
		return sleepTimeInterval;
	}

	public void setSleepTimeInterval(int sleepTimeInterval) {
		this.sleepTimeInterval = sleepTimeInterval;
	}

	public int getMaxTaskItemsOfOneThreadGroup() {
		return maxTaskItemsOfOneThreadGroup;
	}

	public void setMaxTaskItemsOfOneThreadGroup(int maxTaskItemsOfOneThreadGroup) {
		this.maxTaskItemsOfOneThreadGroup = maxTaskItemsOfOneThreadGroup;
	}

	public List<String> getTaskItemList() {
		if (taskItemList == null || taskItemList.isEmpty()) {
			taskItemList = new ArrayList<String>();

			for (int i = 0; i < 10; i++) {
				String taskItem = i + ":{TYPE=A,KIND=1}";
				taskItemList.add(taskItem);
			}
		}

		return taskItemList;
	}

	public void setTaskItemList(List<String> taskItemList) {
		this.taskItemList = taskItemList;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public int getProcessType() {
		return processType;
	}

	public void setProcessType(int processType) {
		this.processType = processType;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public int getStrategyType() {
		return strategyType;
	}

	public void setStrategyType(int strategyType) {
		this.strategyType = strategyType;
	}

	public int getNumOfSingleServer() {
		return numOfSingleServer;
	}

	public void setNumOfSingleServer(int numOfSingleServer) {
		this.numOfSingleServer = numOfSingleServer;
	}

	public int getAssignNum() {
		return assignNum;
	}

	public void setAssignNum(int assignNum) {
		this.assignNum = assignNum;
	}

	public List<String> getIpList() {
		if (ipList == null || ipList.isEmpty()) {
			ipList = new ArrayList<String>();
			String ip = "127.0.0.1";
			ipList.add(ip);
		}

		return ipList;
	}

	public void setIpList(List<String> ipList) {
		this.ipList = ipList;
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

	public double getExpireOwnSignInterval() {
		return expireOwnSignInterval;
	}

	public void setExpireOwnSignInterval(double expireOwnSignInterval) {
		this.expireOwnSignInterval = expireOwnSignInterval;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	@Override
	public String toString() {
		return "ScheduleParameter [scheduleName=" + scheduleName + ", taskName=" + taskName + ", taskBeanName=" + taskBeanName + ", heartBeatRate="
				+ heartBeatRate + ", judgeDeadInterval=" + judgeDeadInterval + ", threadNumber=" + threadNumber + ", fetchDataNumber=" + fetchDataNumber
				+ ", executeNumber=" + executeNumber + ", sleepTimeNoData=" + sleepTimeNoData + ", sleepTimeInterval=" + sleepTimeInterval + ", startTime="
				+ startTime + ", endTime=" + endTime + ", maxTaskItemsOfOneThreadGroup=" + maxTaskItemsOfOneThreadGroup + ", expireOwnSignInterval="
				+ expireOwnSignInterval + ", taskItemList=" + taskItemList + ", extraInfo=" + extraInfo + ", status=" + status + ", processType=" + processType
				+ ", strategyName=" + strategyName + ", strategyType=" + strategyType + ", numOfSingleServer=" + numOfSingleServer + ", assignNum=" + assignNum
				+ ", ipList=" + ipList + "]";
	}

}
