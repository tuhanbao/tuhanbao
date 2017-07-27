package com.sztx.se.core.tbschedule.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.taobao.pamirs.schedule.strategy.ScheduleStrategy.Kind;
import com.sztx.se.core.tbschedule.config.ScheduleParameter;
import com.taobao.pamirs.schedule.strategy.ScheduleStrategy;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskType;

public class TbscheduleUtil {

	/**
	 * 创建一个task，并将其转换为ScheduleParameter
	 * 
	 * @param taskName
	 * @param taskBeanName
	 * @param strategyName
	 * @param fetchDataNumber
	 * @param executeNumber
	 * @param taskItemList
	 * @param processType
	 * @param startTime
	 * @param endTime
	 * @param extraInfo
	 * @return
	 */
	public static ScheduleParameter creatScheduleParameter(String scheduleName, String taskName, String taskBeanName, String strategyName, int fetchDataNumber,
			int executeNumber, List<String> taskItemList, int processType, String startTime, String endTime, String extraInfo) {
		ScheduleParameter scheduleParameter = new ScheduleParameter();
		scheduleParameter.setScheduleName(scheduleName);
		scheduleParameter.setTaskName(taskName);
		scheduleParameter.setTaskBeanName(taskBeanName);
		scheduleParameter.setStrategyName(strategyName);
		scheduleParameter.setFetchDataNumber(fetchDataNumber);
		scheduleParameter.setExecuteNumber(executeNumber);
		scheduleParameter.setTaskItemList(taskItemList);
		scheduleParameter.setProcessType(processType);
		scheduleParameter.setStartTime(startTime);
		scheduleParameter.setEndTime(endTime);
		scheduleParameter.setExtraInfo(extraInfo);
		return scheduleParameter;
	}

	/**
	 * 获取某个task，并将其转换为 ScheduleParameter
	 * 
	 * @param scheduleName
	 * @param scheduleTaskType
	 * @param scheduleStrategy
	 * @return
	 */
	public static ScheduleParameter getScheduleParameter(String scheduleName, ScheduleTaskType scheduleTaskType, ScheduleStrategy scheduleStrategy) {
		String taskName = scheduleTaskType.getBaseTaskType();
		String taskBeanName = scheduleTaskType.getDealBeanName();
		int executeNumber = scheduleTaskType.getExecuteNumber();
		double expireOwnSignInterval = scheduleTaskType.getExpireOwnSignInterval();
		int fetchDataNumber = scheduleTaskType.getFetchDataNumber();
		int heartBeatRate = (int) scheduleTaskType.getHeartBeatRate();
		int judgeDeadInterval = (int) scheduleTaskType.getJudgeDeadInterval();
		int maxTaskItemsOfOneThreadGroup = scheduleTaskType.getMaxTaskItemsOfOneThreadGroup();
		String startTime = scheduleTaskType.getPermitRunStartTime();
		String endTime = scheduleTaskType.getPermitRunEndTime();
		int sleepTimeInterval = scheduleTaskType.getSleepTimeInterval();
		int sleepTimeNoData = scheduleTaskType.getSleepTimeNoData();
		String status = scheduleTaskType.getSts();
		String extraInfo = scheduleTaskType.getTaskParameter();
		int threadNumber = scheduleTaskType.getThreadNumber();
		int processType = 0;

		if ("NOTSLEEP".equals(scheduleTaskType.getProcessorType())) {
			processType = 1;
		}

		String[] taskItems = scheduleTaskType.getTaskItems();
		List<String> taskItemList = new ArrayList<String>();
		Collections.addAll(taskItemList, taskItems);
		String strategyName = scheduleStrategy.getStrategyName();
		int assignNum = scheduleStrategy.getAssignNum();
		String[] ips = scheduleStrategy.getIPList();
		List<String> ipList = new ArrayList<String>();
		Collections.addAll(ipList, ips);
		int numOfSingleServer = scheduleStrategy.getNumOfSingleServer();
		Kind kind = scheduleStrategy.getKind();
		int strategyType = 0;

		if (kind == ScheduleStrategy.Kind.Java) {
			strategyType = 1;
		} else if (kind == ScheduleStrategy.Kind.Bean) {
			strategyType = 2;
		}

		ScheduleParameter scheduleParameter = new ScheduleParameter();
		scheduleParameter.setScheduleName(scheduleName);
		scheduleParameter.setTaskName(taskName);
		scheduleParameter.setTaskBeanName(taskBeanName);
		scheduleParameter.setExecuteNumber(executeNumber);
		scheduleParameter.setExpireOwnSignInterval(expireOwnSignInterval);
		scheduleParameter.setFetchDataNumber(fetchDataNumber);
		scheduleParameter.setHeartBeatRate(heartBeatRate);
		scheduleParameter.setJudgeDeadInterval(judgeDeadInterval);
		scheduleParameter.setMaxTaskItemsOfOneThreadGroup(maxTaskItemsOfOneThreadGroup);
		scheduleParameter.setStartTime(startTime);
		scheduleParameter.setEndTime(endTime);
		scheduleParameter.setSleepTimeInterval(sleepTimeInterval);
		scheduleParameter.setSleepTimeNoData(sleepTimeNoData);
		scheduleParameter.setStatus(status);
		scheduleParameter.setExtraInfo(extraInfo);
		scheduleParameter.setThreadNumber(threadNumber);
		scheduleParameter.setTaskItemList(taskItemList);
		scheduleParameter.setProcessType(processType);
		scheduleParameter.setStrategyName(strategyName);
		scheduleParameter.setAssignNum(assignNum);
		scheduleParameter.setIpList(ipList);
		scheduleParameter.setNumOfSingleServer(numOfSingleServer);
		scheduleParameter.setStrategyType(strategyType);
		return scheduleParameter;
	}

}
