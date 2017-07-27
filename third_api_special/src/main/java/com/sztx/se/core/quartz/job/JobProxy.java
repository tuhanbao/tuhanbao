package com.sztx.se.core.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.core.quartz.source.DynamicQuartz;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;

public class JobProxy implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String jobClassName = context.getJobDetail().getJobDataMap().getString(DynamicQuartz.JOB_CLASS_NAME);

		if (jobClassName == null) {
			throw new JobExecutionException("jobClassName is null !");
		}

		Job job = getJob(jobClassName);

		if (job == null) {
			throw new MyException("Can not get a correct job, jobClassName=" + jobClassName);
		}

		String jobClassCanonicalName = job.getClass().getCanonicalName();
		context.put("jobClassCanonicalName", jobClassCanonicalName);
		job.execute(context);
	}

	private Job getJob(String jobClassName) {
		if (jobClassName == null) {
			throw new MyException("jobClassName is null !");
		}

		Job job = null;
		String errorLog = null;

		try {
			job = SpringContextHolder.applicationContext.getBean(jobClassName, Job.class);
		} catch (Exception e1) {
			errorLog = e1.getMessage();
			Class<?> jobClass = null;

			try {
				jobClass = Class.forName(jobClassName);

				if (jobClass != null) {
					try {
						job = (Job) SpringContextHolder.applicationContext.getBean(jobClass);
					} catch (Exception e3) {
						errorLog = errorLog + " | " + e3.getMessage();
					}

					if (job == null) {
						try {
							job = (Job) jobClass.newInstance();
						} catch (Exception e4) {
							errorLog = errorLog + " | " + e4.getMessage();
						}
					}
				}
			} catch (Exception e2) {
				errorLog = errorLog + " | " + e2.getMessage();
			}
		}

		if (job == null) {
			LogManager.error("Look for job class error, class name=" + jobClassName + errorLog);
		}

		return job;
	}
}
