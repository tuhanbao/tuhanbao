package com.sztx.se.core.quartz.source;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.StringUtils;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.core.quartz.config.JobRunningInfo;
import com.sztx.se.core.quartz.config.QuartzConfigServer;
import com.sztx.se.core.quartz.config.QuartzParameter;
import com.sztx.se.core.quartz.job.BaseJob;
import com.sztx.se.core.quartz.job.JobProxy;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;

public class DynamicQuartz {

	public static final String IS_CRON_TRIGGER = "isCronTrigger";

	public static final String TRIGGER_EXPRESSION = "expression";

	public static final String JOB_CLASS_NAME = "jobClassName";

	public static final String SCHEDULER_DATASOURCE_KEY = "dataSourceKey";

	public static final String[] SCHEDULER_CONSTANTS = { IS_CRON_TRIGGER, TRIGGER_EXPRESSION, JOB_CLASS_NAME, SCHEDULER_DATASOURCE_KEY };

	private final static String QUARTZ_MANAGER_NAME = "workerMonitorScheduler";

	private Map<String, QuartzConfigServer> targetQuartzConfigServerMap;

	private QuartzConfigServer defaultTargetQuartzSource;

	private String hostQuartzName;

	public void setTargetQuartzConfigServerMap(Map<String, QuartzConfigServer> targetQuartzConfigServerMap) {
		this.targetQuartzConfigServerMap = targetQuartzConfigServerMap;
	}

	public void setDefaultTargetQuartzSource(QuartzConfigServer defaultTargetQuartzSource) {
		this.defaultTargetQuartzSource = defaultTargetQuartzSource;
	}

	public void afterPropertiesSet() throws Exception {
		Set<Entry<String, QuartzConfigServer>> set = targetQuartzConfigServerMap.entrySet();
		List<QuartzConfigServer> quartzConfigServerList = new ArrayList<QuartzConfigServer>();

		for (Map.Entry<String, QuartzConfigServer> entry : set) {
			String configServerKey = entry.getKey();
			QuartzConfigServer quartzConfigServer = entry.getValue();

			if (quartzConfigServer != null) {
				Properties quartzProperties = quartzConfigServer.getQuartzProperties();
				Properties p = (Properties) quartzProperties.clone();
				String dataSourceName = quartzProperties.getProperty("org.quartz.jobStore.dataSource");
				String applicationName = quartzConfigServer.getApplicationName();
				String dataSourceKey = quartzConfigServer.getDataSourceKey();
				String instanceName = p.getProperty("org.quartz.scheduler.instanceName");
				instanceName = applicationName + instanceName;
				hostQuartzName = configServerKey + ":" + instanceName;
				p.setProperty("org.quartz.scheduler.instanceName", instanceName);
				p.setProperty("org.quartz.jobStore.dataSource", dataSourceKey);
//				DynamicDataSource dynamicDataSource = (DynamicDataSource) SpringContextHolder.applicationContext.getBean(dataSourceName);
//				final DataSource dataSource = dynamicDataSource.getDataSource(dataSourceKey);
//
//				DBConnectionManager.getInstance().addConnectionProvider(dataSourceKey, new ConnectionProvider() {
//					@Override
//					public Connection getConnection() throws SQLException {
//						return DataSourceUtils.doGetConnection(dataSource);
//					}
//
//					@Override
//					public void shutdown() throws SQLException {
//					}
//
//					@Override
//					public void initialize() throws SQLException {
//					}
//				});

				StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory(p);
				quartzConfigServer.setStdSchedulerFactory(stdSchedulerFactory);
				quartzConfigServerList.add(quartzConfigServer);
			}
		}

		for (QuartzConfigServer quartzConfigServer : quartzConfigServerList) {
			if (quartzConfigServer.getAutoStartup()) {
				startQuartz(quartzConfigServer);
			}
		}
	}

	public QuartzSchedulerFactory getQuartzSchedulerFactory(String configServerKey, String schedName) {
		vailPermission(configServerKey);

		if (configServerKey == null) {
			configServerKey = QuartzSwitcher.getQuartzType();
		}

		QuartzSchedulerFactory quartzSchedulerFactory = null;

		try {
			if (!StringUtils.isEmpty(configServerKey)) {
				QuartzConfigServer quartzConfigServer = targetQuartzConfigServerMap.get(configServerKey);

				if (quartzConfigServer != null) {
					StdSchedulerFactory stdSchedulerFactory = quartzConfigServer.getStdSchedulerFactory();

					if (!StringUtil.isEmpty(schedName)) {
						Properties quartzProperties = quartzConfigServer.getQuartzProperties();
						Properties p = (Properties) quartzProperties.clone();
						p.setProperty("org.quartz.scheduler.instanceName", schedName);
						p.setProperty("org.quartz.jobStore.dataSource", quartzConfigServer.getDataSourceKey());
						stdSchedulerFactory.initialize(p);
					}

					Scheduler scheduler = stdSchedulerFactory.getScheduler();
					scheduler.getContext().put("org.quartz.jobStore.dataSource", quartzConfigServer.getDataSourceKey());
					quartzSchedulerFactory = new QuartzSchedulerFactory(stdSchedulerFactory, scheduler);
				}
			}
		} catch (Exception e) {
			LogManager.error(e);
		}

		if (quartzSchedulerFactory == null) {
			quartzSchedulerFactory = getDefaultQuartzSchedulerFactory(schedName);
		}

		if (quartzSchedulerFactory == null) {
			throw new MyException("Can not get a QuartzSchedulerFactory!");
		}

		return quartzSchedulerFactory;
	}

	public List<String> getAllScheduleNames(String configServerKey) {
		if (configServerKey == null) {
			configServerKey = QuartzSwitcher.getQuartzType();
		}

		List<String> scheduleNames = null;
		Connection conn = null;

		try {
			if (configServerKey == null) {
				configServerKey = QuartzSwitcher.getQuartzType();
			}

			QuartzConfigServer quartzConfigServer = null;

			if (!StringUtil.isEmpty(configServerKey)) {
				quartzConfigServer = targetQuartzConfigServerMap.get(configServerKey);
			} else {
				quartzConfigServer = defaultTargetQuartzSource;
			}

			conn = DBConnectionManager.getInstance().getConnection(quartzConfigServer.getDataSourceKey());
			Properties quartzProperties = quartzConfigServer.getQuartzProperties();
			String tablePrefix = quartzProperties.getProperty("org.quartz.jobStore.tablePrefix");
			scheduleNames = selectAllScheduleNames(tablePrefix, conn);
		} catch (Exception e) {
			LogManager.error(e);
		} finally {
			closeConnection(conn);
		}

		if (scheduleNames == null) {
			scheduleNames = new ArrayList<String>();
		}

		return scheduleNames;
	}

	public List<Scheduler> getAllSchedulers(String configServerKey) {
		List<String> schedulerNames = getAllScheduleNames(configServerKey);
		List<Scheduler> schedulers = null;

		if (schedulerNames != null && !schedulerNames.isEmpty()) {
			schedulers = new ArrayList<Scheduler>();

			for (String schedName : schedulerNames) {
				QuartzSchedulerFactory quartzSchedulerFactory = getQuartzSchedulerFactory(configServerKey, schedName);
				Scheduler scheduler = quartzSchedulerFactory.getScheduler();
				schedulers.add(scheduler);
			}
		}

		if (schedulers == null) {
			schedulers = new ArrayList<Scheduler>();
		}

		return schedulers;
	}

	public void saveOrUpdateJob(QuartzParameter config, String configServerKey, boolean alreadyWarning) {
		try {
			QuartzSchedulerFactory quartzSchedulerFactory = getQuartzSchedulerFactory(configServerKey, config.getSchedName());
			Scheduler scheduler = quartzSchedulerFactory.getScheduler();
			String dataSourceKey = scheduler.getContext().getString("org.quartz.jobStore.dataSource");
			JobKey jobKey = JobKey.jobKey(config.getJobName(), config.getJobGroup());
			TriggerKey triggerKey = TriggerKey.triggerKey(config.getTriggerName(), config.getTriggerGroup());

			if (checkJobExists(scheduler, jobKey)) {
				if (alreadyWarning) {
					throw new MyException("Save job error, the job[schedName=" + scheduler.getSchedulerName() + ",jobName=" + config.getJobName()
							+ ",jobGroup=" + config.getJobGroup() + "] is already exist");
				}
			}

			scheduler.unscheduleJob(triggerKey);
			scheduler.deleteJob(jobKey);
			JobDetail jobDetail = newJobDetail(config, JobProxy.class, dataSourceKey);
			Trigger trigger = newTrigger(config);
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw MyException.getMyException(e);
		}
	}

	public void updateJob(QuartzParameter config, String configServerKey) {
		try {
			QuartzSchedulerFactory quartzSchedulerFactory = getQuartzSchedulerFactory(configServerKey, config.getSchedName());
			Scheduler scheduler = quartzSchedulerFactory.getScheduler();
			String dataSourceKey = scheduler.getContext().getString("org.quartz.jobStore.dataSource");
			JobKey jobKey = JobKey.jobKey(config.getJobName(), config.getJobGroup());
			TriggerKey triggerKey = TriggerKey.triggerKey(config.getTriggerName(), config.getTriggerGroup());

			if (checkJobExists(scheduler, jobKey)) {
				scheduler.unscheduleJob(triggerKey);
				scheduler.deleteJob(jobKey);
				JobDetail jobDetail = newJobDetail(config, JobProxy.class, dataSourceKey);
				Trigger trigger = newTrigger(config);
				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				throw new MyException("Update job error, the job[schedName=" + scheduler.getSchedulerName() + ",jobName=" + config.getJobName()
						+ ",jobGroup=" + config.getJobGroup() + "] is not exist");
			}
		} catch (SchedulerException e) {
			throw MyException.getMyException(e);
		}
	}

	/**
	 * 暂停一个job
	 * 
	 * @throws SchedulerException
	 */
	public void pauseTrigger(QuartzParameter config, String configServerKey) {
		String schedName = config.getSchedName();
		String triggerName = config.getTriggerName();
		String triggerGroup = config.getTriggerGroup();
		QuartzSchedulerFactory quartzSchedulerFactory = getQuartzSchedulerFactory(configServerKey, schedName);
		Scheduler scheduler = quartzSchedulerFactory.getScheduler();

		try {
			scheduler.pauseTrigger(new TriggerKey(triggerName, triggerGroup));
		} catch (SchedulerException e) {
			throw MyException.getMyException(e);
		}
	}

	/**
	 * 恢复job
	 * 
	 * @throws SchedulerException
	 */
	public void resumeTrigger(QuartzParameter config, String configServerKey) {
		String schedName = config.getSchedName();
		String triggerName = config.getTriggerName();
		String triggerGroup = config.getTriggerGroup();
		QuartzSchedulerFactory quartzSchedulerFactory = getQuartzSchedulerFactory(configServerKey, schedName);
		Scheduler scheduler = quartzSchedulerFactory.getScheduler();

		try {
			scheduler.resumeTrigger(new TriggerKey(triggerName, triggerGroup));
		} catch (SchedulerException e) {
			throw MyException.getMyException(e);
		}
	}

	/**
	 * 删除该config指定的job的trigger
	 * 一个job可能有多个trigger,如果本次删除的trigger是该job唯一的触发器，则该job也会被删除
	 * 
	 */
	public void deleteTrigger(QuartzParameter config, String configServerKey) {
		String schedName = config.getSchedName();
		String triggerName = config.getTriggerName();
		String triggerGroup = config.getTriggerGroup();
		QuartzSchedulerFactory quartzSchedulerFactory = getQuartzSchedulerFactory(configServerKey, schedName);
		Scheduler scheduler = quartzSchedulerFactory.getScheduler();

		try {
			scheduler.unscheduleJob(new TriggerKey(triggerName, triggerGroup));
		} catch (SchedulerException e) {
			throw MyException.getMyException(e);
		}
	}

	public class QuartzSchedulerFactory {

		private StdSchedulerFactory stdSchedulerFactory;

		private Scheduler scheduler;

		public QuartzSchedulerFactory(StdSchedulerFactory stdSchedulerFactory, Scheduler scheduler) {
			this.stdSchedulerFactory = stdSchedulerFactory;
			this.scheduler = scheduler;
		}

		public StdSchedulerFactory getStdSchedulerFactory() {
			return stdSchedulerFactory;
		}

		public void setStdSchedulerFactory(StdSchedulerFactory stdSchedulerFactory) {
			this.stdSchedulerFactory = stdSchedulerFactory;
		}

		public Scheduler getScheduler() {
			return scheduler;
		}

		public void setScheduler(Scheduler scheduler) {
			this.scheduler = scheduler;
		}
	}

	public boolean updateJobRunningInfo(String configServerKey, String schedName, String jobName, String jobGroup, String jobClassName, String triggerName,
			String triggerGroup, String runningId, String instanceName, String ip, String host, String startTimeStr, String endTimeStr, long cost,
			String jobResult) {
		if (configServerKey == null) {
			configServerKey = QuartzSwitcher.getQuartzType();
		}

		boolean flag = false;
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			if (configServerKey == null) {
				configServerKey = QuartzSwitcher.getQuartzType();
			}

			QuartzConfigServer quartzConfigServer = null;

			if (!StringUtil.isEmpty(configServerKey)) {
				quartzConfigServer = targetQuartzConfigServerMap.get(configServerKey);
			} else {
				quartzConfigServer = defaultTargetQuartzSource;
			}

			conn = DBConnectionManager.getInstance().getConnection(quartzConfigServer.getDataSourceKey());
			Properties quartzProperties = quartzConfigServer.getQuartzProperties();
			String tablePrefix = quartzProperties.getProperty("org.quartz.jobStore.tablePrefix");
			String sql = "INSERT INTO "
					+ tablePrefix
					+ "JOB_RUNNING_INFOS (SCHED_NAME,JOB_NAME,JOB_GROUP,JOB_CLASS_NAME,TRIGGER_NAME,TRIGGER_GROUP,RUNNING_ID,INSTANCE_NAME,IP,HOST_NAME,START_TIME,END_TIME,COST,JOB_RESULT) VALUES ('"
					+ schedName + "','" + jobName + "','" + jobGroup + "','" + jobClassName + "','" + triggerName + "','" + triggerGroup + "','" + runningId
					+ "','" + instanceName + "','" + ip + "','" + host + "','" + startTimeStr + "','" + endTimeStr + "'," + cost + ",'" + jobResult
					+ "') ON DUPLICATE KEY UPDATE JOB_CLASS_NAME='" + jobClassName + "',TRIGGER_NAME='" + triggerName + "',TRIGGER_GROUP='" + triggerGroup
					+ "',RUNNING_ID='" + runningId + "',INSTANCE_NAME='" + instanceName + "',IP='" + ip + "',HOST_NAME='" + host + "',START_TIME='"
					+ startTimeStr + "',END_TIME='" + endTimeStr + "',COST=" + cost + ",JOB_RESULT='" + jobResult + "';";
			ps = conn.prepareStatement(sql);
			int rs = ps.executeUpdate();

			if (rs > 0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			throw MyException.getMyException(e);
		} finally {
			closeStatement(ps);
			closeConnection(conn);
		}

		return flag;
	}

	public JobRunningInfo getJobRunningInfo(String configServerKey, String schedName, String jobName, String jobGroup) {
		if (configServerKey == null) {
			configServerKey = QuartzSwitcher.getQuartzType();
		}

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			if (configServerKey == null) {
				configServerKey = QuartzSwitcher.getQuartzType();
			}

			QuartzConfigServer quartzConfigServer = null;

			if (!StringUtil.isEmpty(configServerKey)) {
				quartzConfigServer = targetQuartzConfigServerMap.get(configServerKey);
			} else {
				quartzConfigServer = defaultTargetQuartzSource;
			}

			conn = DBConnectionManager.getInstance().getConnection(quartzConfigServer.getDataSourceKey());
			Properties quartzProperties = quartzConfigServer.getQuartzProperties();
			String tablePrefix = quartzProperties.getProperty("org.quartz.jobStore.tablePrefix");
			String sql = "SELECT * FROM " + tablePrefix + "JOB_RUNNING_INFOS WHERE SCHED_NAME='" + schedName + "' AND JOB_NAME='" + jobName
					+ "' AND JOB_GROUP='" + jobGroup + "' LIMIT 1;";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			JobRunningInfo jri = null;

			while (rs.next()) {
				jri = new JobRunningInfo();
				jri.setId(rs.getString("RUNNING_ID"));
				jri.setSchedName(rs.getString("SCHED_NAME"));
				jri.setJobName(rs.getString("JOB_NAME"));
				jri.setJobGroup(rs.getString("JOB_GROUP"));
				jri.setJobClassName(rs.getString("JOB_CLASS_NAME"));
				jri.setTriggerName(rs.getString("TRIGGER_NAME"));
				jri.setTriggerGroup(rs.getString("TRIGGER_GROUP"));
				jri.setInstanceName(rs.getString("INSTANCE_NAME"));
				jri.setIp(rs.getString("IP"));
				jri.setHostName(rs.getString("HOST_NAME"));
				jri.setStartTime(rs.getString("START_TIME"));
				jri.setEndTime(rs.getString("END_TIME"));
				jri.setCost(rs.getLong("COST"));
				jri.setResult(rs.getString("JOB_RESULT"));
			}

			return jri;
		} catch (Exception e) {
			throw MyException.getMyException(e);
		} finally {
			closeStatement(ps);
			closeConnection(conn);
		}
	}

	private QuartzSchedulerFactory getDefaultQuartzSchedulerFactory(String schedName) {
		QuartzSchedulerFactory defaultQuartzSchedulerFactory = null;

		try {
			StdSchedulerFactory defaultStdSchedulerFactory = defaultTargetQuartzSource.getStdSchedulerFactory();

			if (!StringUtil.isEmpty(schedName)) {
				Properties quartzProperties = defaultTargetQuartzSource.getQuartzProperties();
				Properties p = (Properties) quartzProperties.clone();
				p.setProperty("org.quartz.scheduler.instanceName", schedName);
				p.setProperty("org.quartz.jobStore.dataSource", defaultTargetQuartzSource.getDataSourceKey());
				defaultStdSchedulerFactory.initialize(p);
			}

			Scheduler defaultScheduler = defaultStdSchedulerFactory.getScheduler();
			defaultScheduler.getContext().put("org.quartz.jobStore.dataSource", defaultTargetQuartzSource.getDataSourceKey());
			defaultQuartzSchedulerFactory = new QuartzSchedulerFactory(defaultStdSchedulerFactory, defaultScheduler);
		} catch (SchedulerException e) {
			LogManager.error(e);
		}

		return defaultQuartzSchedulerFactory;
	}

	private void startQuartz(QuartzConfigServer quartzConfigServer) throws SchedulerException {
		StdSchedulerFactory stdSchedulerFactory = quartzConfigServer.getStdSchedulerFactory();
		Scheduler scheduler = stdSchedulerFactory.getScheduler();
		scheduler.start();
		LogManager.info("Quartz [" + quartzConfigServer.getConfigServerKey() + "] 自动启动");
	}

	private boolean checkJobExists(Scheduler scheduler, JobKey jobKey) {
		try {
			return scheduler.checkExists(jobKey);
		} catch (SchedulerException e) {
			throw MyException.getMyException(e);
		}
	}

	private JobDetail newJobDetail(QuartzParameter config, Class<? extends Job> jobClass, String dataSource) {
		JobDetail jobDetail = JobBuilder.newJob(jobClass).requestRecovery(config.getIsRecovery()).withIdentity(config.getJobName(), config.getJobGroup())
				.withDescription(config.getDescription()).usingJobData(JOB_CLASS_NAME, config.getJobClassName())
				.usingJobData(SCHEDULER_DATASOURCE_KEY, dataSource).build();

		Map<String, String> extraInfoMap = config.getExtraInfo();
		JobBuilder jobBuilder = jobDetail.getJobBuilder();

		if (extraInfoMap != null) {
			for (Entry<String, String> en : extraInfoMap.entrySet()) {
				jobBuilder.usingJobData(en.getKey(), en.getValue());
			}
		}

		jobDetail = jobBuilder.build();
		return jobDetail;
	}

	private Trigger newTrigger(QuartzParameter config) {
		Trigger trigger = null;

		if (config.getIsCronTrigger()) {
			CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(config.getExpression()).withMisfireHandlingInstructionDoNothing();
			TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger()
					.withIdentity(TriggerKey.triggerKey(config.getTriggerName(), config.getTriggerGroup())).withSchedule(builder)
					.usingJobData(IS_CRON_TRIGGER, config.getIsCronTrigger()).usingJobData(TRIGGER_EXPRESSION, config.getExpression());

			if (config.getStartAt() == null || config.getStartAt().getTime() < new Date().getTime()) {
				triggerBuilder.startNow();
			} else {
				triggerBuilder.startAt(config.getStartAt());
			}

			if (config.getEndAt() != null) {
				triggerBuilder.endAt(config.getEndAt());
			}

			trigger = triggerBuilder.build();
		} else {
			SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule().repeatForever()
					.withIntervalInSeconds(Integer.valueOf(config.getExpression()));
			TriggerBuilder<SimpleTrigger> triggerBuilder = TriggerBuilder.newTrigger()
					.withIdentity(TriggerKey.triggerKey(config.getTriggerName(), config.getTriggerGroup())).withSchedule(builder)
					.usingJobData(IS_CRON_TRIGGER, config.getIsCronTrigger()).usingJobData(TRIGGER_EXPRESSION, config.getExpression());

			if (config.getStartAt() == null || config.getStartAt().getTime() < new Date().getTime()) {
				triggerBuilder.startNow();
			} else {
				triggerBuilder.startAt(config.getStartAt());
			}

			if (config.getEndAt() != null) {
				triggerBuilder.endAt(config.getEndAt());
			}

			trigger = triggerBuilder.build();
		}

		return trigger;
	}

	private List<String> selectAllScheduleNames(String tablePrefix, Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> schedulerNames = new ArrayList<String>();

		try {
			String sql = "SELECT SCHED_NAME FROM " + tablePrefix + "JOB_DETAILS GROUP BY SCHED_NAME";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				String schedulerName = rs.getString("SCHED_NAME");
				schedulerNames.add(schedulerName);
			}

			return schedulerNames;
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
		}
	}

	private static void closeResultSet(ResultSet rs) {
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException ignore) {
			}
		}
	}

	private static void closeStatement(Statement statement) {
		if (null != statement) {
			try {
				statement.close();
			} catch (SQLException ignore) {
			}
		}
	}

	private static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ignore) {
			}
		}
	}

	private void vailPermission(String configServerKey) {
		if (configServerKey == null) {
			configServerKey = QuartzSwitcher.getQuartzType();
		}

		if (configServerKey == null) {
			configServerKey = defaultTargetQuartzSource.getConfigServerKey();
		}

		String managerQuartzName = configServerKey + ":" + QUARTZ_MANAGER_NAME;

		if (hostQuartzName == null || !hostQuartzName.equals(managerQuartzName)) {
			throw new MyException("Have no permission to operate!");
		}
	}
}
