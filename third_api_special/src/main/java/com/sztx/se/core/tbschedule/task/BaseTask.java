package com.sztx.se.core.tbschedule.task;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sztx.se.core.context.InitializeService;
import com.taobao.pamirs.schedule.IScheduleTaskDealMulti;
import com.taobao.pamirs.schedule.TaskItemDefine;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.TimeUtil;

public abstract class BaseTask<T> implements IScheduleTaskDealMulti<T> {

	private static boolean openLog;

	public static void setOpenLog(boolean openLog) {
		BaseTask.openLog = openLog;
	}

	/**
	 * 执行任务
	 * 
	 * @param tasks
	 * @param ownSign
	 * @return
	 * @throws Exception
	 */
	public abstract Object executeTask(T[] tasks, String ownSign) throws Exception;

	@Override
	public boolean execute(T[] tasks, String ownSign) throws Exception {
		boolean flag = true;

		if (openLog) {
			long startTime = System.currentTimeMillis();
			long endTime = 0;
			Object obj = null;

			try {
				String tbScheduleBeginLog = getTbScheduleBeginLog(tasks, ownSign, startTime);
				LogManager.info(tbScheduleBeginLog);
				obj = executeTask(tasks, ownSign);
			} catch (Throwable t) {
				flag = false;
				obj = t.getClass().getCanonicalName() + ":" + t.getMessage();
				throw t;
			} finally {
				String taskResult = "";

				if (obj != null) {
					taskResult = JSON.toJSONStringWithDateFormat(obj, TimeUtil.MAX_LONG_DATE_FORMAT_STR, SerializerFeature.DisableCircularReferenceDetect);
				}

				endTime = (endTime == 0 ? System.currentTimeMillis() : endTime);
				// 打印日志
				String tbScheduleEndLog = getTbScheduleEndLog(taskResult, startTime, endTime);
				LogManager.info(tbScheduleEndLog);
				InitializeService.clearDynamicSources();
			}
		} else {
			try {
				executeTask(tasks, ownSign);
				InitializeService.clearDynamicSources();
			} catch (Throwable t) {
				flag = false;
				throw t;
			}
		}

		return flag;
	}

	public List<String> getTaskItems(List<TaskItemDefine> taskItemList) {
		List<String> taskItems = new ArrayList<>();

		for (int i = 0; i < taskItemList.size(); i++) {
			TaskItemDefine taskItem = taskItemList.get(i);
			taskItems.add(taskItem.getTaskItemId());
		}

		return taskItems;
	}

	private String getTbScheduleBeginLog(T[] tasks, String ownSign, long startTime) {
		String tasksStr = "";
		int length = tasks.length;
		tasksStr = JSON.toJSONStringWithDateFormat(tasks, TimeUtil.MAX_LONG_DATE_FORMAT_STR, SerializerFeature.DisableCircularReferenceDetect);
		String startTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, startTime);
		return String.format("[Tbschedule] Begin task, number:%s|task:%s|ownSign:%s|start:%s", length, tasksStr, ownSign, startTimeStr);
	}

	private String getTbScheduleEndLog(String taskResult, long startTime, long endTime) {
		long cost = endTime - startTime;
		String endTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, endTime);
		return String.format("[Tbschedule] End task, result:%s|[end:%s, cost:%dms]", taskResult, endTimeStr, cost);
	}

	// private String getTbScheduleLog(T[] tasks, String ownSign, String
	// taskResult, long startTime, long endTime) {
	// String tasksStr = "";
	// int length = tasks.length;
	//
	// if (length > 0) {
	// T task = tasks[0];
	// tasksStr = JSON.toJSONStringWithDateFormat(task,
	// DateUtil.MAX_LONG_DATE_FORMAT_STR,
	// SerializerFeature.DisableCircularReferenceDetect);
	// }
	//
	// long cost = endTime - startTime;
	// String startTimeStr = DateUtil.formatDate(startTime,
	// DateUtil.MAX_LONG_DATE_FORMAT_STR);
	// String endTimeStr = DateUtil.formatDate(endTime,
	// DateUtil.MAX_LONG_DATE_FORMAT_STR);
	// return
	// String.format("[Tbschedule] Excute task, number:%s|task:%s|ownSign:%s|result:%s|[start:%s, end:%s, cost:%dms]",
	// length, tasksStr, ownSign,
	// taskResult, startTimeStr, endTimeStr, cost);
	// }
}
