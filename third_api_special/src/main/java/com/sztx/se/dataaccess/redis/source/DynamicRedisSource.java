package com.sztx.se.dataaccess.redis.source;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import com.sztx.se.dataaccess.redis.callback.RedisCallback;
import com.sztx.se.dataaccess.redis.callback.SessionCallback;
import com.sztx.se.dataaccess.redis.template.RedisTemplateProxy;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.other.ConsistenHashUtil;

/**
 * 动态redis
 * 
 * @author zhihongp
 * 
 */
public class DynamicRedisSource {

	public static Map<String, ConsistenHashUtil<RedisTemplate<Serializable, Serializable>>> consistenHashUtilMap;

	private RedisTemplateProxy defaultTargetRedisSource;

	private Map<String, RedisTemplateProxy> targetRedisSources;

	public RedisTemplateProxy getDefaultTargetRedisSource() {
		return defaultTargetRedisSource;
	}

	public void setDefaultTargetRedisSource(RedisTemplateProxy defaultTargetRedisSource) {
		this.defaultTargetRedisSource = defaultTargetRedisSource;
	}

	public Map<String, RedisTemplateProxy> getTargetRedisSources() {
		return targetRedisSources;
	}

	public void setTargetRedisSources(Map<String, RedisTemplateProxy> targetRedisSources) {
		this.targetRedisSources = targetRedisSources;
	}

	public <T> T execute(RedisCallback<T> action) {
		String key = action.getKey();
		RedisTemplate<Serializable, Serializable> redisTemplate = getRedisTemplate(key);

		if (redisTemplate == null) {
			redisTemplate = getRedisTemplate(key, defaultTargetRedisSource);
		}

		if (redisTemplate == null) {
			throw new MyException("Can not get a redisTemplate!");
		}

		return redisTemplate.execute(action);
	}

	public <T> T execute(RedisCallback<T> action, boolean exposeConnection) {
		String key = action.getKey();
		RedisTemplate<Serializable, Serializable> redisTemplate = getRedisTemplate(key);

		if (redisTemplate == null) {
			redisTemplate = getRedisTemplate(key, defaultTargetRedisSource);
		}

		if (redisTemplate == null) {
			throw new MyException("Can not get a redisTemplate!");
		}

		return redisTemplate.execute(action, exposeConnection);
	}

	public <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
		String key = action.getKey();
		RedisTemplate<Serializable, Serializable> redisTemplate = getRedisTemplate(key);

		if (redisTemplate == null) {
			redisTemplate = getRedisTemplate(key, defaultTargetRedisSource);
		}

		if (redisTemplate == null) {
			throw new MyException("Can not get a redisTemplate!");
		}

		return redisTemplate.execute(action, exposeConnection, pipeline);
	}

	public <T> T execute(SessionCallback<T> action) {
		String key = action.getKey();
		RedisTemplate<Serializable, Serializable> redisTemplate = getRedisTemplate(key);

		if (redisTemplate == null) {
			redisTemplate = getRedisTemplate(key, defaultTargetRedisSource);
		}

		if (redisTemplate == null) {
			throw new MyException("Can not get a redisTemplate!");
		}

		return redisTemplate.execute(action);
	}

	public void afterPropertiesSet() {
		Set<Entry<String, RedisTemplateProxy>> set = targetRedisSources.entrySet();

		for (Map.Entry<String, RedisTemplateProxy> entry : set) {
			String key = entry.getKey();
			RedisTemplateProxy redisTemplateProxy = entry.getValue();
			List<RedisTemplate<Serializable, Serializable>> redisTemplateList = redisTemplateProxy.getRedisTemplateList();

			if (redisTemplateList != null && !redisTemplateList.isEmpty()) {
				if (DynamicRedisSource.consistenHashUtilMap == null) {
					DynamicRedisSource.consistenHashUtilMap = new HashMap<String, ConsistenHashUtil<RedisTemplate<Serializable, Serializable>>>();
				}

				DynamicRedisSource.consistenHashUtilMap.put(key, new ConsistenHashUtil<RedisTemplate<Serializable, Serializable>>(redisTemplateList));

				for (RedisTemplate<Serializable, Serializable> redisTemplate : redisTemplateList) {
					redisTemplate.afterPropertiesSet();
				}
			}
		}
	}

	private RedisTemplate<Serializable, Serializable> getRedisTemplate(String key) {
		String redisSourceType = RedisSourceSwitcher.getRedisSourceType();

		if (StringUtils.isNotBlank(redisSourceType)) {
			RedisTemplateProxy redisTemplateProxy = targetRedisSources.get(redisSourceType);
			return getRedisTemplate(key, redisTemplateProxy);
		} else {
			return null;
		}
	}

	private RedisTemplate<Serializable, Serializable> getRedisTemplate(String key, RedisTemplateProxy redisTemplateProxy) {
		if (redisTemplateProxy == null) {
			return null;
		}

		List<RedisTemplate<Serializable, Serializable>> redisTemplateList = redisTemplateProxy.getRedisTemplateList();

		if (redisTemplateList == null || redisTemplateList.isEmpty()) {
			return null;
		}

		String redisSourceType = redisTemplateProxy.getRedisSourceKey();
		ConsistenHashUtil<RedisTemplate<Serializable, Serializable>> consistenHashUtil = DynamicRedisSource.consistenHashUtilMap.get(redisSourceType);

		if (consistenHashUtil != null) {
			RedisTemplate<Serializable, Serializable> redisTemplate = consistenHashUtil.get(key);
			return redisTemplate;
		} else {
			return null;
		}
	}

}
