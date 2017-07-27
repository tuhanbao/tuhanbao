package com.sztx.se.core.quartz.job;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sztx.se.core.context.InitializeService;
import com.sztx.se.core.quartz.config.QuartzParameter;
import com.sztx.se.core.quartz.source.DynamicQuartz;
import com.sztx.se.core.quartz.util.QuartzUtil;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.objutil.TimeUtil;
import com.tuhanbao.base.util.objutil.UUIDUtil;
import com.tuhanbao.base.util.other.IPUtil;

/**
 * 自动任务调度指定的任务需要实现此接口
 * 
 * @author zhihongp
 * 
 */
public abstract class BaseJob implements Job {

	private static final int MAX_TEXT_LENGTH = 65535;

	@Autowired
	private DynamicQuartz dynamicQuartz;

	/**
	 * job具体的业务逻辑
	 * 
	 * @param context
	 * @throws JobExecutionException
	 */
	public abstract Object executeJob(JobExecutionContext context) throws JobExecutionException;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			Thread.sleep(50l);
		} catch (InterruptedException e) {
			LogManager.error("Quartz job execute thread sleep error", e);
		}

		long startTime = System.currentTimeMillis();
		long endTime = 0;
		QuartzParameter quartzParameter = null;
		Object obj = null;

		try {
			quartzParameter = getQuartzParameter(context.getScheduler(), context.getJobDetail(), context.getTrigger());

			String quartzBeginLog = getQuartzBeginLog(quartzParameter, startTime);

			LogManager.info(quartzBeginLog);

			obj = executeJob(context);
		} catch (Throwable t) {
			obj = t.getClass().getCanonicalName() + ":" + t.getMessage();
			throw t;
		} finally {
			String jobResult = "";

			if (obj != null) {
				jobResult = JSON.toJSONStringWithDateFormat(obj, TimeUtil.MAX_LONG_DATE_FORMAT_STR, SerializerFeature.DisableCircularReferenceDetect);
			}

			endTime = (endTime == 0 ? System.currentTimeMillis() : endTime);

			// 打印日志
			String quartzEndLog = getQuartzEndLog(jobResult, startTime, endTime);
			LogManager.info(quartzEndLog);

			updateJobRunningInfo(context, quartzParameter, startTime, endTime, jobResult);
			InitializeService.clearDynamicSources();
		}

	}

	private String getQuartzBeginLog(QuartzParameter quartzParameter, long startTime) {
		String quartzParameterStr = "";

		if (quartzParameter != null) {
			quartzParameterStr = JSON.toJSONStringWithDateFormat(quartzParameter, TimeUtil.MAX_LONG_DATE_FORMAT_STR,
					SerializerFeature.DisableCircularReferenceDetect);
		}

		String startTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, startTime);
		return String.format("[Quartz] Begin job, parameter:%s|start:%s", quartzParameterStr, startTimeStr);
	}

	private String getQuartzEndLog(String jobResult, long startTime, long endTime) {
		long cost = endTime - startTime;
		String endTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, endTime);
		return String.format("[Quartz] End job, result:%s|[end:%s, cost:%dms]", jobResult, endTimeStr, cost);
	}

	private QuartzParameter getQuartzParameter(Scheduler scheduler, JobDetail jobDetail, Trigger trigger) {
		QuartzParameter quartzParameter = null;

		try {
			quartzParameter = QuartzUtil.getQuartzParameter(scheduler, jobDetail, trigger);
		} catch (SchedulerException e) {
			throw MyException.getMyException(e);
		}

		return quartzParameter;
	}

	private void updateJobRunningInfo(JobExecutionContext context, QuartzParameter quartzParameter, long startTime, long endTime, String jobResult) {
		try {
			String schedName = quartzParameter.getSchedName();
			String jobName = quartzParameter.getJobName();
			String jobGroup = quartzParameter.getJobGroup();
			String jobClassName = (String) context.get("jobClassCanonicalName");
			String triggerName = quartzParameter.getTriggerName();
			String triggerGroup = quartzParameter.getTriggerGroup();
			String runningId = UUIDUtil.getJustNumUUID();
			Scheduler scheduler = context.getScheduler();
			String instanceName = scheduler.getSchedulerInstanceId();
			String ip = IPUtil.getLocalIp();
			String host = IPUtil.getLocalHost();
			String startTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, startTime);
			String endTimeStr = TimeUtil.getTimeStr(TimeUtil.MAX_LONG_DATE_FORMAT_STR, endTime);
			long cost = endTime - startTime;

			if (jobResult != null) {
				jobResult = StringUtil.trimString(jobResult, "\"");

				if (jobResult.length() > MAX_TEXT_LENGTH - 1) {
					jobResult = jobResult.substring(0, MAX_TEXT_LENGTH - 2);
				}
			}

			if (dynamicQuartz == null) {
				throw new MyException("UdateJobRunningInfo error, there is no dynamicQuartz");
			}

			boolean flag = dynamicQuartz.updateJobRunningInfo(null, schedName, jobName, jobGroup, jobClassName, triggerName, triggerGroup, runningId,
					instanceName, ip, host, startTimeStr, endTimeStr, cost, jobResult);

			if (!flag) {
				LogManager.error("Update job running info error, job=" + quartzParameter);
			}
		} catch (SchedulerException e) {
			throw MyException.getMyException(e);
		}
	}
}
