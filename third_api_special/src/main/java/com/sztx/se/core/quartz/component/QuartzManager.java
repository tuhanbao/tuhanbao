package com.sztx.se.core.quartz.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.SystemException;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import com.sztx.se.core.quartz.config.JobRunningInfo;
import com.sztx.se.core.quartz.config.QuartzParameter;
import com.sztx.se.core.quartz.source.DynamicQuartz;
import com.sztx.se.core.quartz.source.DynamicQuartz.QuartzSchedulerFactory;
import com.sztx.se.core.quartz.util.QuartzUtil;
import com.tuhanbao.base.util.exception.MyException;

/**
 * 
 * @author zhihongp
 * 
 */
public class QuartzManager {

	private DynamicQuartz dynamicQuartz;

	public void setDynamicQuartz(DynamicQuartz dynamicQuartz) {
		this.dynamicQuartz = dynamicQuartz;
	}

	/**
	 * 创建一个job，如果已存在该job，则创建失败
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
	public boolean saveJob(String schedName, String jobName, String jobGroup, String jobClassName, String description, boolean isRecovery, String triggerName,
			String triggerGroup, boolean isCronTrigger, String expression, Date startAt, Date endAt, Map<String, String> extraInfo) {
		QuartzParameter config = QuartzUtil.creatQuartzParameter(schedName, jobName, jobGroup, jobClassName, description, isRecovery, triggerName,
				triggerGroup, isCronTrigger, expression, startAt, endAt, extraInfo);
		dynamicQuartz.saveOrUpdateJob(config, null, true);
		return true;
	}

	/**
	 * 更新一个job，如果该job不存在，则更新失败
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
	public boolean updateJob(String schedName, String jobName, String jobGroup, String jobClassName, String description, boolean isRecovery,
			String triggerName, String triggerGroup, boolean isCronTrigger, String expression, Date startAt, Date endAt, Map<String, String> extraInfo) {
		QuartzParameter config = QuartzUtil.creatQuartzParameter(schedName, jobName, jobGroup, jobClassName, description, isRecovery, triggerName,
				triggerGroup, isCronTrigger, expression, startAt, endAt, extraInfo);
		dynamicQuartz.updateJob(config, null);
		return true;
	}

	/**
	 * 创建一个job，如果已存在该job，则更新该job
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
	public boolean saveOrUpdateJob(String schedName, String jobName, String jobGroup, String jobClassName, String description, boolean isRecovery,
			String triggerName, String triggerGroup, boolean isCronTrigger, String expression, Date startAt, Date endAt, Map<String, String> extraInfo) {
		QuartzParameter config = QuartzUtil.creatQuartzParameter(schedName, jobName, jobGroup, jobClassName, description, isRecovery, triggerName,
				triggerGroup, isCronTrigger, expression, startAt, endAt, extraInfo);
		dynamicQuartz.saveOrUpdateJob(config, null, false);
		return true;
	}

	/**
	 * 暂停一个job
	 * 
	 * @param schedName
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public boolean pauseTrigger(String schedName, String triggerName, String triggerGroup) {
		QuartzParameter config = new QuartzParameter();
		config.setSchedName(schedName);
		config.setTriggerName(triggerName);
		config.setTriggerGroup(triggerGroup);
		dynamicQuartz.pauseTrigger(config, null);
		return true;
	}

	/**
	 * 恢复一个job
	 * 
	 * @param schedName
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public boolean resumeTrigger(String schedName, String triggerName, String triggerGroup) {
		QuartzParameter config = new QuartzParameter();
		config.setSchedName(schedName);
		config.setTriggerName(triggerName);
		config.setTriggerGroup(triggerGroup);
		dynamicQuartz.resumeTrigger(config, null);
		return true;
	}

	/**
	 * 删除一个job，一个job可能有多个trigger,如果本次删除的trigger是该job唯一的触发器，则该job也会被删除
	 * 
	 * @param schedName
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public boolean deleteTrigger(String schedName, String triggerName, String triggerGroup) {
		QuartzParameter config = new QuartzParameter();
		config.setSchedName(schedName);
		config.setTriggerName(triggerName);
		config.setTriggerGroup(triggerGroup);
		dynamicQuartz.deleteTrigger(config, null);
		return true;
	}

	/**
	 * 获取所有的job
	 * 
	 * @return
	 */
	public List<QuartzParameter> getAllJobs() {
		List<QuartzParameter> list = null;
		List<String> scheduleNames = dynamicQuartz.getAllScheduleNames(null);

		if (scheduleNames != null) {
			list = new ArrayList<QuartzParameter>();

			for (String scheduleName : scheduleNames) {
				list.addAll(getSchedulerJobs(scheduleName));
			}
		}

		return list;
	}

	/**
	 * 获取某个sched下面的所有job
	 * 
	 * @param schedName
	 * @return
	 */
	public List<QuartzParameter> getSchedulerJobs(String schedName) {
		List<QuartzParameter> list = new ArrayList<QuartzParameter>();

		try {
			Scheduler scheduler = getScheduler(schedName);
			Set<JobKey> keys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());

			if (keys == null) {
				return list;
			}

			for (JobKey key : keys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(key);

				if (triggers != null) {
					JobDetail jobDetail = scheduler.getJobDetail(key);

					for (Trigger trigger : triggers) {
						list.add(getJob(scheduler, jobDetail, trigger));
					}
				}
			}
		} catch (SchedulerException e) {
			throw MyException.getMyException(e);
		}

		return list;
	}

	/**
	 * 获取某个job
	 * 
	 * @param schedName
	 * @param jobName
	 * @param jobGroup
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public QuartzParameter getJob(String schedName, String jobName, String jobGroup, String triggerName, String triggerGroup) {
		Scheduler scheduler = getScheduler(schedName);
		JobKey jobKey = new JobKey(jobName, jobGroup);
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);

		try {
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			Trigger trigger = scheduler.getTrigger(triggerKey);
			return getJob(scheduler, jobDetail, trigger);
		} catch (SchedulerException e) {
			throw MyException.getMyException(e);
		}
	}

	public JobRunningInfo getJobRunningInfo(String schedName, String jobName, String jobGroup) {
		JobRunningInfo jobRunningInfo = dynamicQuartz.getJobRunningInfo(null, schedName, jobName, jobGroup);
		return jobRunningInfo;
	}

	private QuartzParameter getJob(Scheduler scheduler, JobDetail jobDetail, Trigger trigger) throws SchedulerException {
		if (scheduler == null || jobDetail == null || trigger == null) {
			return null;
		}

		return QuartzUtil.getQuartzParameter(scheduler, jobDetail, trigger);
	}

	private Scheduler getScheduler(String schedName) {
		QuartzSchedulerFactory quartzSchedulerFactory = dynamicQuartz.getQuartzSchedulerFactory(null, schedName);
		return quartzSchedulerFactory.getScheduler();
	}

}
