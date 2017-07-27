package com.sztx.se.core.quartz.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.quartz.CronExpression;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.sztx.se.core.quartz.config.QuartzParameter;
import com.sztx.se.core.quartz.source.DynamicQuartz;
import com.tuhanbao.base.util.exception.MyException;

public class QuartzUtil {

	/**
	 * 创建某个job，并将其转换为 QuartzParameter
	 * 
	 * @param schedName
	 * @param jobName
	 * @param jobGroup
	 * @param jobClassName
	 * @param description
	 * @param isRecovery
	 * @param triggerName
	 * @param triggerGroup
	 * @param isCronTrigger
	 * @param expression
	 * @param startAt
	 * @param endAt
	 * @param extraInfo
	 * @return
	 */
	public static QuartzParameter creatQuartzParameter(String schedName, String jobName, String jobGroup, String jobClassName, String description,
			boolean isRecovery, String triggerName, String triggerGroup, boolean isCronTrigger, String expression, Date startAt, Date endAt,
			Map<String, String> extraInfo) {
		if (isCronTrigger) {
			try {
				new CronExpression(expression);
			} catch (Exception e) {
				throw MyException.getMyException(e);
			}
		} else {
			try {
				Integer.valueOf(expression);
			} catch (Exception e) {
				throw MyException.getMyException(e);
			}
		}

		QuartzParameter quartzParameter = new QuartzParameter();
		quartzParameter.setSchedName(schedName);
		quartzParameter.setJobName(jobName);
		quartzParameter.setJobGroup(jobGroup);
		quartzParameter.setJobClassName(jobClassName);
		quartzParameter.setDescription(description);
		quartzParameter.setIsRecovery(isRecovery);
		quartzParameter.setTriggerName(triggerName);
		quartzParameter.setTriggerGroup(triggerGroup);
		quartzParameter.setIsCronTrigger(isCronTrigger);
		quartzParameter.setExpression(expression);
		quartzParameter.setStartAt(startAt);
		quartzParameter.setEndAt(endAt);
		quartzParameter.setExtraInfo(extraInfo);
		return quartzParameter;
	}

	/**
	 * 获取某个job，并将其转换为 QuartzParameter
	 * 
	 * @param scheduler
	 * @param jobDetail
	 * @param trigger
	 * @return
	 * @throws SchedulerException
	 */
	public static QuartzParameter getQuartzParameter(Scheduler scheduler, JobDetail jobDetail, Trigger trigger) throws SchedulerException {
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		String jobClassName = jobDataMap.getString(DynamicQuartz.JOB_CLASS_NAME);

		if (jobClassName == null) {
			throw new MyException("Quartz jobClassName is null");
		}

		Map<String, String> extraInfo = null;
		Set<Entry<String, Object>> set = jobDataMap.entrySet();

		if (!set.isEmpty()) {
			extraInfo = new HashMap<String, String>();

			for (Entry<String, Object> entry : set) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (!constantsMatch(key)) {
					String valueStr = (String) value;
					extraInfo.put(key, valueStr);
				}
			}
		}

		QuartzParameter quartzParameter = new QuartzParameter();
		quartzParameter.setSchedName(scheduler.getSchedulerName());
		quartzParameter.setJobName(jobDetail.getKey().getName());
		quartzParameter.setJobGroup(jobDetail.getKey().getGroup());
		quartzParameter.setJobClassName(jobClassName);
		quartzParameter.setDescription(jobDetail.getDescription());
		quartzParameter.setIsRecovery(jobDetail.requestsRecovery());
		quartzParameter.setTriggerName(trigger.getKey().getName());
		quartzParameter.setTriggerGroup(trigger.getKey().getGroup());
		quartzParameter.setIsCronTrigger(trigger.getJobDataMap().getBoolean(DynamicQuartz.IS_CRON_TRIGGER));
		quartzParameter.setExpression(trigger.getJobDataMap().getString(DynamicQuartz.TRIGGER_EXPRESSION));
		quartzParameter.setPrevFireTime(trigger.getPreviousFireTime());
		quartzParameter.setNextFireTime(trigger.getNextFireTime());
		quartzParameter.setStartAt(trigger.getStartTime());
		quartzParameter.setEndAt(trigger.getEndTime());
		quartzParameter.setStatus(scheduler.getTriggerState(trigger.getKey()).toString());
		quartzParameter.setExtraInfo(extraInfo);
		return quartzParameter;
	}

	public static boolean constantsMatch(String obj) {
		boolean flag = false;
		String[] scheduleConstants = DynamicQuartz.SCHEDULER_CONSTANTS;

		for (int i = 0; i < scheduleConstants.length; i++) {
			String scheduleConstant = scheduleConstants[i];

			if (scheduleConstant.equals(obj)) {
				flag = true;
				break;
			}
		}

		return flag;
	}
}
