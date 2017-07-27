package com.sztx.se.core.tbschedule.component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.sztx.se.core.tbschedule.config.ScheduleParameter;
import com.sztx.se.core.tbschedule.config.TaskRunningInfo;
import com.sztx.se.core.tbschedule.source.DynamicSchedule;
import com.sztx.se.core.tbschedule.util.TbscheduleUtil;
import com.taobao.pamirs.schedule.strategy.ScheduleStrategy;
import com.taobao.pamirs.schedule.taskmanager.ScheduleServer;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskType;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskTypeRunningInfo;

/**
 * 
 * @author zhihongp
 * 
 */
public class ScheduleManager {

	private DynamicSchedule dynamicSchedule;

	public void setDynamicSchedule(DynamicSchedule dynamicSchedule) {
		this.dynamicSchedule = dynamicSchedule;
	}

	/**
	 * 创建一个task，如果已存在该task，则创建失败(创建成功后自动启动)
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param taskBeanName
	 * @param strategyName
	 * @param extraInfo
	 * @return
	 */
	public boolean saveTask(String scheduleName, String taskName, String taskBeanName, String strategyName, String extraInfo) {
		return saveTask(scheduleName, taskName, taskBeanName, strategyName, 100, 100, null, ScheduleParameter.SLEEP_MODEL, extraInfo);
	}

	/**
	 * 创建一个task，如果已存在该task，则创建失败(创建成功后自动启动)
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param taskBeanName
	 * @param strategyName
	 * @param fetchDataNumber
	 * @param executeNumber
	 * @param taskItemList
	 * @param processType
	 * @param extraInfo
	 * @return
	 */
	public boolean saveTask(String scheduleName, String taskName, String taskBeanName, String strategyName, int fetchDataNumber, int executeNumber,
			List<String> taskItemList, int processType, String extraInfo) {
		return saveTask(scheduleName, taskName, taskBeanName, strategyName, fetchDataNumber, executeNumber, taskItemList, processType, null, null, extraInfo);
	}

	/**
	 * 创建一个task，如果已存在该task，则创建失败(创建成功后自动启动)
	 * 
	 * @param scheduleName
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
	public boolean saveTask(String scheduleName, String taskName, String taskBeanName, String strategyName, int fetchDataNumber, int executeNumber,
			List<String> taskItemList, int processType, String startTime, String endTime, String extraInfo) {
		ScheduleParameter config = TbscheduleUtil.creatScheduleParameter(scheduleName, taskName, taskBeanName, strategyName, fetchDataNumber, executeNumber,
				taskItemList, processType, startTime, endTime, extraInfo);
		config.setStatus(ScheduleParameter.STATUS_RESUME);
		dynamicSchedule.saveOrUpdateTask(scheduleName, config, null, DynamicSchedule.OPERATE_TYPE_SAVE);
		return true;
	}

	/**
	 * 更新一个task，如果不存在该task，则更新失败
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param taskBeanName
	 * @param strategyName
	 * @param extraInfo
	 * @return
	 */
	public boolean updateTask(String scheduleName, String taskName, String taskBeanName, String strategyName, String extraInfo) {
		return updateTask(scheduleName, taskName, taskBeanName, strategyName, 100, 100, null, ScheduleParameter.SLEEP_MODEL, extraInfo);
	}

	/**
	 * 更新一个task，如果不存在该task，则更新失败
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param taskBeanName
	 * @param strategyName
	 * @param fetchDataNumber
	 * @param executeNumber
	 * @param taskItemList
	 * @param processType
	 * @param extraInfo
	 * @return
	 */
	public boolean updateTask(String scheduleName, String taskName, String taskBeanName, String strategyName, int fetchDataNumber, int executeNumber,
			List<String> taskItemList, int processType, String extraInfo) {
		return updateTask(scheduleName, taskName, taskBeanName, strategyName, fetchDataNumber, executeNumber, taskItemList, processType, null, null, extraInfo);
	}

	/**
	 * 更新一个task，如果不存在该task，则更新失败
	 * 
	 * @param scheduleName
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
	public boolean updateTask(String scheduleName, String taskName, String taskBeanName, String strategyName, int fetchDataNumber, int executeNumber,
			List<String> taskItemList, int processType, String startTime, String endTime, String extraInfo) {
		ScheduleParameter config = TbscheduleUtil.creatScheduleParameter(scheduleName, taskName, taskBeanName, strategyName, fetchDataNumber, executeNumber,
				taskItemList, processType, startTime, endTime, extraInfo);
		dynamicSchedule.saveOrUpdateTask(scheduleName, config, null, DynamicSchedule.OPERATE_TYPE_UPDATE);
		return true;
	}

	/**
	 * 创建一个task，如果已存在该task，则更新该task
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param taskBeanName
	 * @param strategyName
	 * @param extraInfo
	 * @return
	 */
	public boolean saveOrUpdateTask(String scheduleName, String taskName, String taskBeanName, String strategyName, String extraInfo) {
		return saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, 100, 100, null, ScheduleParameter.SLEEP_MODEL, extraInfo);
	}

	/**
	 * 创建一个task，如果已存在该task，则更新该task
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param taskBeanName
	 * @param strategyName
	 * @param fetchDataNumber
	 * @param executeNumber
	 * @param taskItemList
	 * @param processType
	 * @param extraInfo
	 * @return
	 */
	public boolean saveOrUpdateTask(String scheduleName, String taskName, String taskBeanName, String strategyName, int fetchDataNumber, int executeNumber,
			List<String> taskItemList, int processType, String extraInfo) {
		return saveOrUpdateTask(scheduleName, taskName, taskBeanName, strategyName, fetchDataNumber, executeNumber, taskItemList, processType, null, null,
				extraInfo);
	}

	/**
	 * 创建一个task，如果已存在该task，则更新该task
	 * 
	 * @param scheduleName
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
	public boolean saveOrUpdateTask(String scheduleName, String taskName, String taskBeanName, String strategyName, int fetchDataNumber, int executeNumber,
			List<String> taskItemList, int processType, String startTime, String endTime, String extraInfo) {
		ScheduleParameter config = TbscheduleUtil.creatScheduleParameter(scheduleName, taskName, taskBeanName, strategyName, fetchDataNumber, executeNumber,
				taskItemList, processType, startTime, endTime, extraInfo);
		dynamicSchedule.saveOrUpdateTask(scheduleName, config, null, null);
		return true;
	}

	/**
	 * 暂停一个task
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param strategyName
	 * @return
	 */
	public boolean pauseTask(String scheduleName, String taskName, String strategyName) {
		dynamicSchedule.pauseTask(scheduleName, taskName, strategyName, null);
		return true;
	}

	/**
	 * 恢复一个task
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param strategyName
	 * @return
	 */
	public boolean resumeTask(String scheduleName, String taskName, String strategyName) {
		dynamicSchedule.resumeTask(scheduleName, taskName, strategyName, null);
		return true;
	}

	/**
	 * 删除一个task
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param strategyName
	 * @return
	 */
	public boolean deleteTask(String scheduleName, String taskName, String strategyName) {
		dynamicSchedule.deleteTask(scheduleName, taskName, strategyName, null);
		return true;
	}

	/**
	 * 获取所有的task
	 * 
	 * @return
	 */
	public List<ScheduleParameter> getAllTasks() {
		List<ScheduleParameter> list = null;
		List<String> schedulerList = dynamicSchedule.getAllScheduleNames(null);

		if (schedulerList != null) {
			list = new ArrayList<ScheduleParameter>();

			for (String scheduleName : schedulerList) {
				list.addAll(getSchedulerTasks(scheduleName));
			}
		}

		return list;
	}

	/**
	 * 获取某个scheduleName下面的所有task
	 * 
	 * @param schedName
	 * @return
	 */
	public List<ScheduleParameter> getSchedulerTasks(String scheduleName) {
		List<ScheduleParameter> list = new ArrayList<ScheduleParameter>();
		List<ScheduleTaskType> taskList = dynamicSchedule.getTaskList(scheduleName, null);
		List<ScheduleStrategy> strategyList = dynamicSchedule.getStrategyList(scheduleName, null);

		for (ScheduleTaskType scheduleTaskType : taskList) {
			String taskTypeName = scheduleTaskType.getBaseTaskType();

			for (ScheduleStrategy scheduleStrategy : strategyList) {
				if (taskTypeName.equals(scheduleStrategy.getTaskName())) {
					ScheduleParameter scheduleParameter = TbscheduleUtil.getScheduleParameter(scheduleName, scheduleTaskType, scheduleStrategy);
					list.add(scheduleParameter);
				}
			}
		}

		return list;
	}

	/**
	 * 获取某个task
	 * 
	 * @param schedName
	 * @param jobName
	 * @param jobGroup
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public ScheduleParameter getTask(String scheduleName, String taskName, String strategyName) {
		ScheduleTaskType scheduleTaskType = dynamicSchedule.getTask(scheduleName, taskName, null);
		ScheduleStrategy scheduleStrategy = dynamicSchedule.getStrategy(scheduleName, strategyName, null);
		return TbscheduleUtil.getScheduleParameter(scheduleName, scheduleTaskType, scheduleStrategy);
	}

	/**
	 * 
	 * @param scheduleName
	 * @param taskName
	 * @param strategyName
	 * @return
	 */
	public List<TaskRunningInfo> getTaskRunningInfoList(String scheduleName, String taskName, String strategyName) {
		ScheduleParameter config = TbscheduleUtil.creatScheduleParameter(scheduleName, taskName, null, strategyName, 100, 100, null,
				ScheduleParameter.SLEEP_MODEL, null, null, null);
		List<TaskRunningInfo> taskRunningInfoList = null;
		List<ScheduleTaskTypeRunningInfo> scheduleTaskTypeRunningInfoList = dynamicSchedule.getScheduleTaskRunningInfoList(scheduleName, taskName, null);

		if (scheduleTaskTypeRunningInfoList != null && !scheduleTaskTypeRunningInfoList.isEmpty()) {
			for (ScheduleTaskTypeRunningInfo scheduleTaskTypeRunningInfo : scheduleTaskTypeRunningInfoList) {
				config.setTaskName(scheduleTaskTypeRunningInfo.getTaskType());
				List<ScheduleServer> serverList = dynamicSchedule.getScheduleServerList(scheduleName, taskName, null);

				if (serverList != null && !serverList.isEmpty()) {
					taskRunningInfoList = new ArrayList<TaskRunningInfo>();

					for (ScheduleServer server : serverList) {
						long id = server.getId();
						String ip = server.getIp();
						String hostName = server.getHostName();
						int threadNum = server.getThreadNum();
						Timestamp registerTime = server.getRegisterTime();
						Timestamp heartBeatTime = server.getHeartBeatTime();
						Timestamp lastFetchDataTime = server.getLastFetchDataTime();
						long version = server.getVersion();
						String dealInfoDesc = server.getDealInfoDesc();
						TaskRunningInfo taskRunningInfo = new TaskRunningInfo();
						taskRunningInfo.setId(id);
						taskRunningInfo.setScheduleName(scheduleName);
						taskRunningInfo.setTaskName(taskName);
						taskRunningInfo.setIp(ip);
						taskRunningInfo.setHostName(hostName);
						taskRunningInfo.setThreadNum(threadNum);
						taskRunningInfo.setRegisterTime(registerTime);
						taskRunningInfo.setLastHeartBeatTime(heartBeatTime);
						taskRunningInfo.setLastFetchDataTime(lastFetchDataTime);

						if (dealInfoDesc != null) {
							String[] dealInfoDescArray = dealInfoDesc.split(",");

							for (int i = 0; i < dealInfoDescArray.length; i++) {
								if (dealInfoDescArray[i].contains(TaskRunningInfo.FETCH_DATA_NUM)) {
									String[] everyDeal = dealInfoDescArray[i].split("=");
									taskRunningInfo.setFetchDataNum(Long.valueOf(everyDeal[1]));
								} else if (dealInfoDescArray[i].contains(TaskRunningInfo.DEAL_DATA_SUCESS)) {
									String[] everyDeal = dealInfoDescArray[i].split("=");
									taskRunningInfo.setDealSucessNum(Long.valueOf(everyDeal[1]));
								} else if (dealInfoDescArray[i].contains(TaskRunningInfo.DEAL_DATA_FAIL)) {
									String[] everyDeal = dealInfoDescArray[i].split("=");
									taskRunningInfo.setDealFailNum(Long.valueOf(everyDeal[1]));
								} else if (dealInfoDescArray[i].contains(TaskRunningInfo.DEAL_SPEND_TIME)) {
									String[] everyDeal = dealInfoDescArray[i].split("=");
									taskRunningInfo.setDealSpendTime(Long.valueOf(everyDeal[1]));
								}
							}
						}

						taskRunningInfo.setVersion(version);
						taskRunningInfoList.add(taskRunningInfo);
					}
				}
			}
		}

		return taskRunningInfoList;
	}
}
