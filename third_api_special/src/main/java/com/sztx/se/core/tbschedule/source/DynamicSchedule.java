package com.sztx.se.core.tbschedule.source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.ZooKeeper;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.core.tbschedule.config.ScheduleConfigServer;
import com.sztx.se.core.tbschedule.config.ScheduleParameter;
import com.sztx.se.core.tbschedule.task.BaseTask;
import com.taobao.pamirs.schedule.strategy.ScheduleStrategy;
import com.taobao.pamirs.schedule.strategy.TBScheduleManagerFactory;
import com.taobao.pamirs.schedule.taskmanager.ScheduleServer;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskType;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskTypeRunningInfo;
import com.taobao.pamirs.schedule.zk.ScheduleDataManager4ZK;
import com.taobao.pamirs.schedule.zk.ScheduleStrategyDataManager4ZK;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.ReflectUtil;

public class DynamicSchedule {

	public static final String OPERATE_TYPE_SAVE = "save";

	public static final String OPERATE_TYPE_UPDATE = "update";

	private final static String SCHEDULE_ROOT = "/tbschedule";

	private final static String SCHEDULE_MANAGER_NAME = SCHEDULE_ROOT + "/" + "workerMonitorScheduler";

	private final static String SCHEDULE_SUFFIX = "Scheduler";

	private Map<String, ScheduleConfigServer> targetScheduleConfigServerMap;

	private Map<String, ScheduleConfigServer> targetGlobalScheduleConfigServerMap = new HashMap<String, ScheduleConfigServer>();

	private ScheduleConfigServer defaultTargetScheduleSource;

	private ScheduleConfigServer defaultTargetGlobalScheduleSource;

	private Map<String, TBScheduleManagerFactory> initedScheduleManagerFactoryMap = new HashMap<String, TBScheduleManagerFactory>();

	private Map<String, String> hostPathMap = new HashMap<String, String>();

	/**
	 * 日志开关，默认为false不打开
	 */
	private boolean openLog = false;

	private long interval = 1000l;

	public void setTargetScheduleConfigServerMap(Map<String, ScheduleConfigServer> targetScheduleConfigServerMap) {
		this.targetScheduleConfigServerMap = targetScheduleConfigServerMap;
	}

	public void setDefaultTargetScheduleSource(ScheduleConfigServer defaultTargetScheduleSource) {
		this.defaultTargetScheduleSource = defaultTargetScheduleSource;
	}

	public void setOpenLog(boolean openLog) {
		this.openLog = openLog;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public void afterPropertiesSet() throws Exception {
		Set<Entry<String, ScheduleConfigServer>> set = targetScheduleConfigServerMap.entrySet();

		for (Map.Entry<String, ScheduleConfigServer> entry : set) {
			String configServerKey = entry.getKey();
			ScheduleConfigServer scheduleConfigServer = entry.getValue();

			if (scheduleConfigServer != null) {
				TBScheduleManagerFactory scheduleManagerFactory = new TBScheduleManagerFactory();
				scheduleManagerFactory.setApplicationContext(SpringContextHolder.applicationContext);
				Map<String, String> zkConfig = new HashMap<String, String>();
				String rootPath = SCHEDULE_ROOT + "/" + scheduleConfigServer.getApplicationName() + SCHEDULE_SUFFIX;
				String hostPath = getHostPath(configServerKey, rootPath);
				hostPathMap.put(configServerKey, hostPath);
				zkConfig.put("zkConnectString", scheduleConfigServer.getServerAddress());
				zkConfig.put("rootPath", rootPath);
				zkConfig.put("userName", scheduleConfigServer.getUsername());
				zkConfig.put("password", scheduleConfigServer.getPassword());
				zkConfig.put("zkSessionTimeout", String.valueOf(scheduleConfigServer.getTimeout()));
				zkConfig.put("isCheckParentPath", String.valueOf(scheduleConfigServer.getIsCheckParentPath()));
				scheduleManagerFactory.setZkConfig(zkConfig);
				scheduleConfigServer.setScheduleManagerFactory(scheduleManagerFactory);
				String managerHostPath = getManagerHostPath(configServerKey);

				if (hostPath != null && hostPath.equals(managerHostPath)) {
					ScheduleConfigServer globalScheduleConfigServer = getGlobalScheduleConfigServer(configServerKey, scheduleConfigServer);
					targetGlobalScheduleConfigServerMap.put(configServerKey, globalScheduleConfigServer);

					if (globalScheduleConfigServer.getIsDefault()) {
						defaultTargetGlobalScheduleSource = globalScheduleConfigServer;
					}
				}

				if (scheduleConfigServer.getAutoStartup()) {
					startSchedule(hostPath, scheduleConfigServer);
				}
			}
		}
	}

	public void initTbScheduleLog() {
		BaseTask.setOpenLog(openLog);
	}

	/**
	 * 
	 * @param scheduleName
	 * @param config
	 * @param configServerKey
	 * @param operateType
	 */
	public void saveOrUpdateTask(String scheduleName, ScheduleParameter config, String configServerKey, String operateType) {
		try {
			TBScheduleManagerFactory scheduleManagerFactory = getScheduleManagerFactory(scheduleName, configServerKey);
			String baseTaskTypeName = config.getTaskName();
			// 创建任务的task信息
			ScheduleTaskType baseTaskType = getScheduleTaskType(config, baseTaskTypeName);
			// 创建任务的strategy信息
			ScheduleStrategy strategy = getScheduleStrategy(config, baseTaskTypeName);

			if (!scheduleManagerFactory.isZookeeperInitialSucess()) {
				String rootPath = scheduleManagerFactory.getZkConfig().get("rootPath");
				throw new MyException("ScheduleManagerFactory " + rootPath + " Zookeeper initial fail!");
			}

			ScheduleStrategyDataManager4ZK scheduleStrategyDataManager = scheduleManagerFactory.getScheduleStrategyManager();
			ScheduleDataManager4ZK scheduleDataManager = (ScheduleDataManager4ZK) scheduleManagerFactory.getScheduleDataManager();

			if (OPERATE_TYPE_SAVE.equals(operateType)) {
				saveTask(scheduleName, baseTaskType, strategy, scheduleDataManager, scheduleStrategyDataManager);
			} else if (OPERATE_TYPE_UPDATE.equals(operateType)) {
				updateTask(scheduleName, baseTaskType, strategy, scheduleDataManager, scheduleStrategyDataManager);
			} else {
				if (checkTaskExists(scheduleDataManager, baseTaskType)) {
					updateTask(scheduleName, baseTaskType, strategy, scheduleDataManager, scheduleStrategyDataManager);
				} else {
					saveTask(scheduleName, baseTaskType, strategy, scheduleDataManager, scheduleStrategyDataManager);
				}
			}
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	/**
	 * 
	 * @param scheduleName
	 * @param config
	 * @param configServerKey
	 */
	public void pauseTask(String scheduleName, String baseTaskTypeName, String strategyName, String configServerKey) {
		try {
			TBScheduleManagerFactory scheduleManagerFactory = getScheduleManagerFactory(scheduleName, configServerKey);

			if (!scheduleManagerFactory.isZookeeperInitialSucess()) {
				String rootPath = scheduleManagerFactory.getZkConfig().get("rootPath");
				throw new MyException("ScheduleManagerFactory " + rootPath + " Zookeeper initial fail!");
			}

			ScheduleDataManager4ZK scheduleDataManager = (ScheduleDataManager4ZK) scheduleManagerFactory.getScheduleDataManager();
			ScheduleStrategyDataManager4ZK scheduleStrategyDataManager = scheduleManagerFactory.getScheduleStrategyManager();
			scheduleStrategyDataManager.pause(strategyName);
			scheduleDataManager.pauseAllServer(baseTaskTypeName);
			int count = 0;

			while (true) {
				count++;
				List<ScheduleServer> validScheduleServers = scheduleDataManager.selectAllValidScheduleServer(baseTaskTypeName);

				if (validScheduleServers == null || validScheduleServers.isEmpty()) {
					break;
				}

				int interval = 100;
				Thread.sleep(interval);
				int maxCount = 100;

				if (count > maxCount) {
					LogManager.warn("Tbschedule pause task failure with time=" + interval * maxCount + "ms");
					break;
				}
			}
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	/**
	 * 
	 * @param scheduleName
	 * @param config
	 * @param configServerKey
	 */
	public void resumeTask(String scheduleName, String baseTaskTypeName, String strategyName, String configServerKey) {
		try {
			TBScheduleManagerFactory scheduleManagerFactory = getScheduleManagerFactory(scheduleName, configServerKey);

			if (!scheduleManagerFactory.isZookeeperInitialSucess()) {
				String rootPath = scheduleManagerFactory.getZkConfig().get("rootPath");
				throw new MyException("ScheduleManagerFactory " + rootPath + " Zookeeper initial fail!");
			}

			ScheduleStrategyDataManager4ZK scheduleStrategyDataManager = scheduleManagerFactory.getScheduleStrategyManager();
			ScheduleDataManager4ZK scheduleDataManager = (ScheduleDataManager4ZK) scheduleManagerFactory.getScheduleDataManager();
			scheduleStrategyDataManager.resume(strategyName);
			scheduleDataManager.resumeAllServer(baseTaskTypeName);
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	public void deleteTask(String scheduleName, String baseTaskTypeName, String strategyName, String configServerKey) {
		try {
			TBScheduleManagerFactory scheduleManagerFactory = getScheduleManagerFactory(scheduleName, configServerKey);

			if (!scheduleManagerFactory.isZookeeperInitialSucess()) {
				String rootPath = scheduleManagerFactory.getZkConfig().get("rootPath");
				throw new MyException("ScheduleManagerFactory " + rootPath + " Zookeeper initial fail!");
			}

			ScheduleStrategyDataManager4ZK scheduleStrategyDataManager = scheduleManagerFactory.getScheduleStrategyManager();
			ScheduleDataManager4ZK scheduleDataManager = (ScheduleDataManager4ZK) scheduleManagerFactory.getScheduleDataManager();

			if (checkTaskExists(scheduleDataManager, baseTaskTypeName)) {
				pauseTask(scheduleName, baseTaskTypeName, strategyName, null);
				scheduleDataManager.deleteTaskType(baseTaskTypeName);
				scheduleStrategyDataManager.deleteMachineStrategy(strategyName);
			}
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	public List<ScheduleTaskType> getTaskList(String scheduleName, String configServerKey) {
		try {
			TBScheduleManagerFactory scheduleManagerFactory = getScheduleManagerFactory(scheduleName, configServerKey);

			if (!scheduleManagerFactory.isZookeeperInitialSucess()) {
				String rootPath = scheduleManagerFactory.getZkConfig().get("rootPath");
				throw new MyException("ScheduleManagerFactory " + rootPath + " Zookeeper initial fail!");
			}

			ScheduleDataManager4ZK scheduleDataManager = (ScheduleDataManager4ZK) scheduleManagerFactory.getScheduleDataManager();
			List<ScheduleTaskType> taskList = scheduleDataManager.getAllTaskTypeBaseInfo();
			return taskList;
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	public ScheduleTaskType getTask(String scheduleName, String baseTaskTypeName, String configServerKey) {
		try {
			TBScheduleManagerFactory scheduleManagerFactory = getScheduleManagerFactory(scheduleName, configServerKey);

			if (!scheduleManagerFactory.isZookeeperInitialSucess()) {
				String rootPath = scheduleManagerFactory.getZkConfig().get("rootPath");
				throw new MyException("ScheduleManagerFactory " + rootPath + " Zookeeper initial fail!");
			}

			ScheduleDataManager4ZK scheduleDataManager = (ScheduleDataManager4ZK) scheduleManagerFactory.getScheduleDataManager();
			ScheduleTaskType scheduleTaskType = scheduleDataManager.loadTaskTypeBaseInfo(baseTaskTypeName);
			return scheduleTaskType;
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	public List<ScheduleStrategy> getStrategyList(String scheduleName, String configServerKey) {
		try {
			TBScheduleManagerFactory scheduleManagerFactory = getScheduleManagerFactory(scheduleName, configServerKey);

			if (!scheduleManagerFactory.isZookeeperInitialSucess()) {
				String rootPath = scheduleManagerFactory.getZkConfig().get("rootPath");
				throw new MyException("ScheduleManagerFactory " + rootPath + " Zookeeper initial fail!");
			}

			ScheduleStrategyDataManager4ZK scheduleStrategyDataManager = scheduleManagerFactory.getScheduleStrategyManager();
			List<ScheduleStrategy> strategyList = scheduleStrategyDataManager.loadAllScheduleStrategy();
			return strategyList;
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	public ScheduleStrategy getStrategy(String scheduleName, String strategyName, String configServerKey) {
		try {
			TBScheduleManagerFactory scheduleManagerFactory = getScheduleManagerFactory(scheduleName, configServerKey);

			if (!scheduleManagerFactory.isZookeeperInitialSucess()) {
				String rootPath = scheduleManagerFactory.getZkConfig().get("rootPath");
				throw new MyException("ScheduleManagerFactory " + rootPath + " Zookeeper initial fail!");
			}

			ScheduleStrategyDataManager4ZK scheduleStrategyDataManager = scheduleManagerFactory.getScheduleStrategyManager();
			ScheduleStrategy scheduleStrategy = scheduleStrategyDataManager.loadStrategy(strategyName);
			return scheduleStrategy;
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	public List<String> getAllScheduleNames(String configServerKey) {
		try {
			TBScheduleManagerFactory globalScheduleManagerFactory = getScheduleManagerFactory(SCHEDULE_ROOT, configServerKey);
			Map<String, String> zkConfig = globalScheduleManagerFactory.getZkConfig();
			String globalScheduleName = zkConfig.get("rootPath");
			ZooKeeper zk = globalScheduleManagerFactory.getZkManager().getZooKeeper();
			List<String> scheduleNameList = zk.getChildren(globalScheduleName, false);
			return scheduleNameList;
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	public List<ScheduleServer> getScheduleServerList(String scheduleName, String baseTaskTypeName, String configServerKey) {
		try {
			TBScheduleManagerFactory scheduleManagerFactory = getScheduleManagerFactory(scheduleName, configServerKey);

			if (!scheduleManagerFactory.isZookeeperInitialSucess()) {
				String rootPath = scheduleManagerFactory.getZkConfig().get("rootPath");
				throw new MyException("ScheduleManagerFactory " + rootPath + " Zookeeper initial fail!");
			}

			ScheduleDataManager4ZK scheduleDataManager = (ScheduleDataManager4ZK) scheduleManagerFactory.getScheduleDataManager();
			List<ScheduleServer> serverList = scheduleDataManager.selectAllValidScheduleServer(baseTaskTypeName);
			return serverList;
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	public List<ScheduleTaskTypeRunningInfo> getScheduleTaskRunningInfoList(String scheduleName, String baseTaskTypeName, String configServerKey) {
		try {
			TBScheduleManagerFactory scheduleManagerFactory = getScheduleManagerFactory(scheduleName, configServerKey);

			if (!scheduleManagerFactory.isZookeeperInitialSucess()) {
				String rootPath = scheduleManagerFactory.getZkConfig().get("rootPath");
				throw new MyException("ScheduleManagerFactory " + rootPath + " Zookeeper initial fail!");
			}

			ScheduleDataManager4ZK scheduleDataManager = (ScheduleDataManager4ZK) scheduleManagerFactory.getScheduleDataManager();
			List<ScheduleTaskTypeRunningInfo> scheduleTaskTypeRunningInfoList = scheduleDataManager.getAllTaskTypeRunningInfo(baseTaskTypeName);
			return scheduleTaskTypeRunningInfoList;
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	/**
	 * 
	 * @param rootPath
	 * @param scheduleConfigServer
	 * @throws Exception
	 */
	private void startSchedule(String hostPath, ScheduleConfigServer scheduleConfigServer) throws Exception {
		TBScheduleManagerFactory scheduleManagerFactory = scheduleConfigServer.getScheduleManagerFactory();
		boolean flag = false;
		int num = 0;
		scheduleManagerFactory.init();

		while (true) {
			if (scheduleManagerFactory.isZookeeperInitialSucess()) {
				flag = true;
				break;
			}

			if (num >= 30) {
				flag = false;
				LogManager.error("Zookeeper initial fail ...");
				break;
			}

			num++;
			Thread.sleep(1000);
		}

		Thread.sleep(1000);
		initedScheduleManagerFactoryMap.put(hostPath, scheduleManagerFactory);

		if (flag) {
			LogManager.info("TBSchedule [" + scheduleConfigServer.getConfigServerKey() + " " + hostPath + "] 自动启动");
		} else {
			String exceptionMessage = "TBSchedule [" + scheduleConfigServer.getConfigServerKey() + " " + hostPath + "] 启动失败";
			throw new MyException(exceptionMessage);
		}
	}

	/**
	 * 
	 * @param scheduleName
	 * @param configServerKey
	 * @return
	 * @throws Exception
	 */
	private TBScheduleManagerFactory getScheduleManagerFactory(String scheduleName, String configServerKey) throws Exception {
		if (configServerKey == null) {
			configServerKey = ScheduleSwitcher.getScheduleType();
		}

		vailPermission(configServerKey);
		String newRootPath = null;
		ScheduleConfigServer scheduleConfigServer = null;

		if (SCHEDULE_ROOT.equals(scheduleName)) {
			newRootPath = SCHEDULE_ROOT;

			if (StringUtils.isNotBlank(configServerKey)) {
				scheduleConfigServer = targetGlobalScheduleConfigServerMap.get(configServerKey);
			}

			if (scheduleConfigServer == null) {
				scheduleConfigServer = defaultTargetGlobalScheduleSource;
			}
		} else {
			newRootPath = SCHEDULE_ROOT + "/" + scheduleName;

			if (StringUtils.isNotBlank(configServerKey)) {
				scheduleConfigServer = targetScheduleConfigServerMap.get(configServerKey);
			}

			if (scheduleConfigServer == null) {
				scheduleConfigServer = defaultTargetScheduleSource;
			}
		}

		if (configServerKey == null) {
			configServerKey = scheduleConfigServer.getConfigServerKey();
		}

		String hostPath = getHostPath(configServerKey, newRootPath);
		TBScheduleManagerFactory scheduleManagerFactory = initedScheduleManagerFactoryMap.get(hostPath);
		boolean flag = false;

		if (scheduleManagerFactory == null) {
			scheduleManagerFactory = new TBScheduleManagerFactory();
			scheduleManagerFactory.setApplicationContext(SpringContextHolder.applicationContext);
			Map<String, String> zkConfig = new HashMap<String, String>();
			zkConfig.put("rootPath", newRootPath);
			zkConfig.put("zkConnectString", scheduleConfigServer.getServerAddress());
			zkConfig.put("userName", scheduleConfigServer.getUsername());
			zkConfig.put("password", scheduleConfigServer.getPassword());
			zkConfig.put("zkSessionTimeout", String.valueOf(scheduleConfigServer.getTimeout()));
			zkConfig.put("isCheckParentPath", String.valueOf(scheduleConfigServer.getIsCheckParentPath()));
			scheduleManagerFactory.setZkConfig(zkConfig);
			scheduleManagerFactory.setStart(false);
			int num = 0;
			scheduleManagerFactory.initWithoutTimer();

			while (true) {
				if (scheduleManagerFactory.isZookeeperInitialSucess()) {
					flag = true;
					break;
				}

				if (num >= 20) {
					flag = false;
					LogManager.error("Zookeeper initial fail ...");
					break;
				}

				num++;
				Thread.sleep(50);
			}

			Thread.sleep(interval);
			initedScheduleManagerFactoryMap.put(hostPath, scheduleManagerFactory);
		} else {
			flag = true;
		}

		if (!flag) {
			throw new MyException("Can not get a scheduleManagerFactory, scheduleManagerFactory " + hostPath + " initial fail!");
		}

		return scheduleManagerFactory;
	}

	private void saveTask(String scheduleName, ScheduleTaskType baseTaskType, ScheduleStrategy strategy, ScheduleDataManager4ZK scheduleDataManager,
			ScheduleStrategyDataManager4ZK scheduleStrategyDataManager) {
		try {
			if (checkTaskExists(scheduleDataManager, baseTaskType)) {
				throw new MyException("Save task error, the task[scheduleName=" + scheduleName + ",taskName=" + baseTaskType.getBaseTaskType()
						+ "] is already exist");
			}

			scheduleDataManager.updateBaseTaskType(baseTaskType);
			scheduleStrategyDataManager.updateScheduleStrategy(strategy);
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	private void updateTask(String scheduleName, ScheduleTaskType baseTaskType, ScheduleStrategy strategy, ScheduleDataManager4ZK scheduleDataManager,
			ScheduleStrategyDataManager4ZK scheduleStrategyDataManager) {
		try {
			if (!checkTaskExists(scheduleDataManager, baseTaskType)) {
				throw new MyException("Update task error, the task[scheduleName=" + scheduleName + ",taskName=" + baseTaskType.getBaseTaskType()
						+ "] is not exist");
			}

			ScheduleTaskType scheduleTaskType = scheduleDataManager.loadTaskTypeBaseInfo(baseTaskType.getBaseTaskType());
			ScheduleStrategy scheduleStrategy = scheduleStrategyDataManager.loadStrategy(strategy.getStrategyName());
			String taskSts = scheduleTaskType.getSts();
			baseTaskType.setSts(taskSts);
			String strategySts = scheduleStrategy.getSts();
			strategy.setSts(strategySts);
			pauseTask(scheduleName, baseTaskType.getBaseTaskType(), strategy.getStrategyName(), null);
			scheduleDataManager.updateBaseTaskType(baseTaskType);
			scheduleStrategyDataManager.updateScheduleStrategy(strategy);
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}

	private void vailPermission(String configServerKey) {
		if (configServerKey == null) {
			configServerKey = defaultTargetScheduleSource.getConfigServerKey();
		}

		if (configServerKey == null) {
			throw new MyException("ConfigServerKey must not be null!");
		}

		String hostPath = hostPathMap.get(configServerKey);
		String managerHostPath = getManagerHostPath(configServerKey);

		if (hostPath == null || !hostPath.equals(managerHostPath)) {
			throw new MyException("Have no permission to operate!");
		}
	}

	private String getHostPath(String configServerKey, String rootPath) {
		if (configServerKey == null) {
			throw new MyException("ConfigServerKey must not be null");
		}

		String hostPath = configServerKey + ":" + rootPath;
		return hostPath;
	}

	private String getManagerHostPath(String configServerKey) {
		if (configServerKey == null) {
			throw new MyException("ConfigServerKey must not be null");
		}

		String managerHostPath = configServerKey + ":" + SCHEDULE_MANAGER_NAME;
		return managerHostPath;
	}

	private boolean checkTaskExists(ScheduleDataManager4ZK scheduleDataManage, ScheduleTaskType baseTaskType) throws Exception {
		return checkTaskExists(scheduleDataManage, baseTaskType.getBaseTaskType());
	}

	private boolean checkTaskExists(ScheduleDataManager4ZK scheduleDataManage, String baseTaskTypeName) throws Exception {
		String pathBaseTaskType = (String) ReflectUtil.getFieldValue(scheduleDataManage, "PATH_BaseTaskType");
		String zkPath = pathBaseTaskType + "/" + baseTaskTypeName;
		ZooKeeper zooKeeper = scheduleDataManage.getZooKeeper();

		if (zooKeeper.exists(zkPath, false) == null) {
			return false;
		} else {
			return true;
		}
	}

	private ScheduleTaskType getScheduleTaskType(ScheduleParameter config, String baseTaskTypeName) {
		ScheduleTaskType baseTaskType = new ScheduleTaskType();
		baseTaskType.setBaseTaskType(baseTaskTypeName);
		baseTaskType.setDealBeanName(config.getTaskBeanName());
		baseTaskType.setExecuteNumber(config.getExecuteNumber());
		baseTaskType.setExpireOwnSignInterval(config.getExpireOwnSignInterval());
		baseTaskType.setFetchDataNumber(config.getFetchDataNumber());
		baseTaskType.setHeartBeatRate(config.getHeartBeatRate());
		baseTaskType.setJudgeDeadInterval(config.getJudgeDeadInterval());
		baseTaskType.setMaxTaskItemsOfOneThreadGroup(config.getMaxTaskItemsOfOneThreadGroup());
		baseTaskType.setPermitRunStartTime(config.getStartTime());
		baseTaskType.setPermitRunEndTime(config.getEndTime());
		baseTaskType.setSleepTimeInterval(config.getSleepTimeInterval());
		baseTaskType.setSleepTimeNoData(config.getSleepTimeNoData());
		baseTaskType.setTaskParameter(config.getExtraInfo());
		baseTaskType.setThreadNumber(config.getThreadNumber());

		if (config.getProcessType() == 0) {
			baseTaskType.setProcessorType("SLEEP");
		} else {
			baseTaskType.setProcessorType("NOTSLEEP");
		}

		if (config.getStatus() != null) {
			baseTaskType.setSts(config.getStatus());
		}

		String[] taskItems = config.getTaskItemList().toArray(new String[0]);
		baseTaskType.setTaskItems(taskItems);
		return baseTaskType;
	}

	private ScheduleStrategy getScheduleStrategy(ScheduleParameter config, String baseTaskTypeName) {
		String strategyName = config.getStrategyName();
		ScheduleStrategy strategy = new ScheduleStrategy();
		strategy.setStrategyName(strategyName);

		if (config.getStrategyType() == 1) {
			strategy.setKind(ScheduleStrategy.Kind.Java);
		} else if (config.getStrategyType() == 2) {
			strategy.setKind(ScheduleStrategy.Kind.Bean);
		} else {
			strategy.setKind(ScheduleStrategy.Kind.Schedule);
		}

		strategy.setTaskName(baseTaskTypeName);
		strategy.setTaskParameter(config.getExtraInfo());
		strategy.setNumOfSingleServer(config.getNumOfSingleServer());
		strategy.setAssignNum(config.getAssignNum());
		strategy.setSts(config.getStatus());
		String[] ips = config.getIpList().toArray(new String[0]);
		strategy.setIPList(ips);
		return strategy;
	}

	private ScheduleConfigServer getGlobalScheduleConfigServer(String configServerKey, ScheduleConfigServer scheduleConfigServer) {
		if (configServerKey == null) {
			configServerKey = ScheduleSwitcher.getScheduleType();
		}

		vailPermission(configServerKey);
		ScheduleConfigServer globalScheduleConfigServer = new ScheduleConfigServer();
		globalScheduleConfigServer.setApplicationName(scheduleConfigServer.getApplicationName());
		globalScheduleConfigServer.setAutoStartup(false);
		globalScheduleConfigServer.setIsCheckParentPath(scheduleConfigServer.getIsCheckParentPath());
		globalScheduleConfigServer.setIsDefault(scheduleConfigServer.getIsDefault());
		globalScheduleConfigServer.setPassword(scheduleConfigServer.getPassword());
		globalScheduleConfigServer.setServerAddress(scheduleConfigServer.getServerAddress());
		globalScheduleConfigServer.setTimeout(scheduleConfigServer.getTimeout());
		globalScheduleConfigServer.setUsername(scheduleConfigServer.getUsername());
		TBScheduleManagerFactory globalScheduleManagerFactory = new TBScheduleManagerFactory();
		globalScheduleManagerFactory.setApplicationContext(SpringContextHolder.applicationContext);
		Map<String, String> zkConfig = new HashMap<String, String>();
		String rootPath = SCHEDULE_ROOT;
		zkConfig.put("zkConnectString", globalScheduleConfigServer.getServerAddress());
		zkConfig.put("rootPath", rootPath);
		zkConfig.put("userName", globalScheduleConfigServer.getUsername());
		zkConfig.put("password", globalScheduleConfigServer.getPassword());
		zkConfig.put("zkSessionTimeout", String.valueOf(globalScheduleConfigServer.getTimeout()));
		zkConfig.put("isCheckParentPath", String.valueOf(globalScheduleConfigServer.getIsCheckParentPath()));
		globalScheduleManagerFactory.setZkConfig(zkConfig);
		globalScheduleConfigServer.setScheduleManagerFactory(globalScheduleManagerFactory);
		return globalScheduleConfigServer;
	}

}
