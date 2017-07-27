package com.sztx.se.core.quartz.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sztx.se.core.context.SpringContextHolder;
import com.sztx.se.core.quartz.config.QuartzConfigServer;
import com.tuhanbao.base.util.log.LogManager;

/**
 * 
 * @author zhihongp
 *
 */
public class DynamicCreateQuartzManager {

	private List<QuartzConfigServer> quartzConfigServerList;

	private DynamicQuartz dynamicQuartz;

	public void setQuartzConfigServerList(List<QuartzConfigServer> quartzConfigServerList) {
		this.quartzConfigServerList = quartzConfigServerList;
	}

	public void setDynamicQuartz(DynamicQuartz dynamicQuartz) {
		this.dynamicQuartz = dynamicQuartz;
	}

	public void initCreateQuartz() {
		registerQuartScheduler();
	}

	private void registerQuartScheduler() {
		Map<String, QuartzConfigServer> targetQuartzConfigServerMap = new HashMap<String, QuartzConfigServer>();
		List<QuartzConfigServer> quartzConfigServerList = new ArrayList<QuartzConfigServer>();
		QuartzConfigServer defaultQuartzConfigServer = null;
		
		if (this.quartzConfigServerList == null || this.quartzConfigServerList.isEmpty()) {
			Map<String, QuartzConfigServer> quartzConfigServerMap = SpringContextHolder.applicationContext.getBeansOfType(QuartzConfigServer.class);

			if (quartzConfigServerMap != null && !quartzConfigServerMap.isEmpty()) {
				for (Entry<String, QuartzConfigServer> en : quartzConfigServerMap.entrySet()) {
					quartzConfigServerList.add(en.getValue());
				}
			}
		} else {
			quartzConfigServerList = this.quartzConfigServerList;
		}
		
		for (QuartzConfigServer quartzConfigServer : quartzConfigServerList) {
			String configServerKey = quartzConfigServer.getConfigServerKey();
			targetQuartzConfigServerMap.put(configServerKey, quartzConfigServer);
			boolean isDefault = quartzConfigServer.getIsDefault();

			if (isDefault) {
				defaultQuartzConfigServer = quartzConfigServer;
			}
		}
		
		dynamicQuartz.setTargetQuartzConfigServerMap(targetQuartzConfigServerMap);
		dynamicQuartz.setDefaultTargetQuartzSource(defaultQuartzConfigServer);
		
		try {
			dynamicQuartz.afterPropertiesSet();
		} catch (Exception e) {
		    LogManager.error("Initialize quartz schedule error", e);
		}
	}

}
