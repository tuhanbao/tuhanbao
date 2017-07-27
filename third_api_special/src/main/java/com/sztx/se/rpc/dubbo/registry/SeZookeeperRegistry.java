package com.sztx.se.rpc.dubbo.registry;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.UrlUtils;
import com.alibaba.dubbo.registry.NotifyListener;
import com.alibaba.dubbo.registry.zookeeper.ZookeeperRegistry;
import com.alibaba.dubbo.remoting.zookeeper.ChildListener;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperClient;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.sztx.se.rpc.dubbo.config.DubboConstants;
import com.tuhanbao.base.util.objutil.ReflectUtil;

public class SeZookeeperRegistry extends ZookeeperRegistry {

	private final static Logger logger = LoggerFactory.getLogger(SeZookeeperRegistry.class);
	
	private static HashMap<String, String> methodProviderMap = new HashMap<String, String>();

	// URL地址分隔符，用于文件缓存中，服务提供者URL分隔
	private static final char URL_SEPARATOR = ' ';

	public SeZookeeperRegistry(URL url, ZookeeperTransporter zookeeperTransporter) {
		super(url, zookeeperTransporter);
	}

	public static String getProviderApplication(String interfaceName) {
		if (methodProviderMap != null) {
			return methodProviderMap.get(interfaceName);
		} else {
			return "";
		}
	}
	
	@Override
	protected void doRegister(URL url) {
		try {
			ZookeeperClient zkClient = (ZookeeperClient) ReflectUtil.getFieldValue(this, "zkClient");
			handleUrl(url);
			zkClient.create(toUrlPath(url), url.getParameter(Constants.DYNAMIC_KEY, true));
		} catch (Throwable e) {
			throw new RpcException("Failed to register " + url + " to zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSubscribe(final URL url, final NotifyListener listener) {
		try {
			ConcurrentMap<URL, ConcurrentMap<NotifyListener, ChildListener>> zkListeners = (ConcurrentMap<URL, ConcurrentMap<NotifyListener, ChildListener>>) ReflectUtil
					.getFieldValue(this, "zkListeners");
			final Set<String> anyServices = (Set<String>) ReflectUtil.getFieldValue(this, "anyServices");
			ZookeeperClient zkClient = (ZookeeperClient) ReflectUtil.getFieldValue(this, "zkClient");

			if (Constants.ANY_VALUE.equals(url.getServiceInterface())) {
				String root = toRootPath();
				ConcurrentMap<NotifyListener, ChildListener> listeners = zkListeners.get(url);

				if (listeners == null) {
					zkListeners.putIfAbsent(url, new ConcurrentHashMap<NotifyListener, ChildListener>());
					listeners = zkListeners.get(url);
				}

				ChildListener zkListener = listeners.get(listener);

				if (zkListener == null) {
					listeners.putIfAbsent(listener, new ChildListener() {
						public void childChanged(String parentPath, List<String> currentChilds) {
							for (String child : currentChilds) {
								if (!anyServices.contains(child)) {
									anyServices.add(child);
									subscribe(url.setPath(child).addParameters(Constants.INTERFACE_KEY, child, Constants.CHECK_KEY, String.valueOf(false)),
											listener);
								}
							}
						}
					});
					zkListener = listeners.get(listener);
				}

				zkClient.create(root, false);
				List<String> services = zkClient.addChildListener(root, zkListener);

				if (services != null && services.size() > 0) {
					anyServices.addAll(services);

					for (String service : services) {
						subscribe(url.setPath(service).addParameters(Constants.INTERFACE_KEY, service, Constants.CHECK_KEY, String.valueOf(false)), listener);
					}
				}
			} else {
				List<URL> urls = new ArrayList<URL>();

				for (String path : toCategoriesPath(url)) {
					ConcurrentMap<NotifyListener, ChildListener> listeners = zkListeners.get(url);

					if (listeners == null) {
						zkListeners.putIfAbsent(url, new ConcurrentHashMap<NotifyListener, ChildListener>());
						listeners = zkListeners.get(url);
					}

					ChildListener zkListener = listeners.get(listener);

					if (zkListener == null) {
						listeners.putIfAbsent(listener, new ChildListener() {
							public void childChanged(String parentPath, List<String> currentChilds) {
								SeZookeeperRegistry.this.notify(url, listener, toUrlsWithEmpty(url, parentPath, currentChilds));
							}
						});

						zkListener = listeners.get(listener);
					}

					zkClient.create(path, false);
					List<String> children = zkClient.addChildListener(path, zkListener);

					if (children != null) {
						urls.addAll(toUrlsWithEmpty(url, path, children));
					}
				}

				for (URL tmpUrl : urls) {
					handleUrl(tmpUrl);
					if (tmpUrl != null && tmpUrl.toString().startsWith("dubbo:")) {
						String providerApplication = tmpUrl.getParameter(Constants.APPLICATION_KEY);
						RpcContext.getContext().set(DubboConstants.PROVIDER_APPLICATION_KEY, providerApplication);
					}
				}

				notify(url, listener, urls);
			}
		} catch (Throwable e) {
			throw new RpcException("Failed to subscribe " + url + " to zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void notify(URL url, NotifyListener listener, List<URL> urls) {
		if (url == null) {
			throw new IllegalArgumentException("notify url == null");
		}

		if (listener == null) {
			throw new IllegalArgumentException("notify listener == null");
		}

		if ((urls == null || urls.size() == 0) && !Constants.ANY_VALUE.equals(url.getServiceInterface())) {
			logger.warn("Ignore empty notify urls for subscribe url " + url);
			return;
		}

		if (logger.isInfoEnabled()) {
			logger.info("Notify urls for subscribe url " + url + ", urls: " + urls);
		}

		ConcurrentMap<URL, Map<String, List<URL>>> notified = (ConcurrentMap<URL, Map<String, List<URL>>>) ReflectUtil.getFieldValue(this, "notified");
		Map<String, List<URL>> result = new HashMap<String, List<URL>>();

		for (URL u : urls) {
			if (UrlUtils.isMatch(url, u)) {
				String category = u.getParameter(Constants.CATEGORY_KEY, Constants.DEFAULT_CATEGORY);
				List<URL> categoryList = result.get(category);

				if (categoryList == null) {
					categoryList = new ArrayList<URL>();
					result.put(category, categoryList);
				}

				categoryList.add(u);
			}
		}

		if (result.size() == 0) {
			return;
		}

		Map<String, List<URL>> categoryNotified = notified.get(url);

		if (categoryNotified == null) {
			notified.putIfAbsent(url, new ConcurrentHashMap<String, List<URL>>());
			categoryNotified = notified.get(url);
		}

		for (Map.Entry<String, List<URL>> entry : result.entrySet()) {
			String category = entry.getKey();
			List<URL> categoryList = entry.getValue();
			categoryNotified.put(category, categoryList);
			saveProperties(url);

			for (URL u : categoryList) {
				handleUrl(u);
			}

			listener.notify(categoryList);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void retry() {
		Set<URL> failedRegistered = (Set<URL>) ReflectUtil.getFieldValue(this, "failedRegistered");
		Set<URL> failedUnregistered = (Set<URL>) ReflectUtil.getFieldValue(this, "failedUnregistered");
		ConcurrentMap<URL, Set<NotifyListener>> failedSubscribed = (ConcurrentMap<URL, Set<NotifyListener>>) ReflectUtil
				.getFieldValue(this, "failedSubscribed");
		ConcurrentMap<URL, Set<NotifyListener>> failedUnsubscribed = (ConcurrentMap<URL, Set<NotifyListener>>) ReflectUtil.getFieldValue(this,
				"failedUnsubscribed");
		ConcurrentMap<URL, Map<NotifyListener, List<URL>>> failedNotified = (ConcurrentMap<URL, Map<NotifyListener, List<URL>>>) ReflectUtil.getFieldValue(
				this, "failedNotified");

		if (!failedRegistered.isEmpty()) {
			Set<URL> failed = new HashSet<URL>(failedRegistered);

			if (failed.size() > 0) {
				if (logger.isInfoEnabled()) {
					logger.info("Retry register " + failed);
				}

				try {
					for (URL url : failed) {
						try {
							doRegister(url);
							failedRegistered.remove(url);
						} catch (Throwable t) { // 忽略所有异常，等待下次重试
							logger.warn("Failed to retry register " + failed + ", waiting for again, cause: " + t.getMessage(), t);
						}
					}
				} catch (Throwable t) { // 忽略所有异常，等待下次重试
					logger.warn("Failed to retry register " + failed + ", waiting for again, cause: " + t.getMessage(), t);
				}
			}
		}

		if (!failedUnregistered.isEmpty()) {
			Set<URL> failed = new HashSet<URL>(failedUnregistered);

			if (failed.size() > 0) {
				if (logger.isInfoEnabled()) {
					logger.info("Retry unregister " + failed);
				}

				try {
					for (URL url : failed) {
						try {
							doUnregister(url);
							failedUnregistered.remove(url);
						} catch (Throwable t) { // 忽略所有异常，等待下次重试
							logger.warn("Failed to retry unregister  " + failed + ", waiting for again, cause: " + t.getMessage(), t);
						}
					}
				} catch (Throwable t) { // 忽略所有异常，等待下次重试
					logger.warn("Failed to retry unregister  " + failed + ", waiting for again, cause: " + t.getMessage(), t);
				}
			}
		}

		if (!failedSubscribed.isEmpty()) {
			Map<URL, Set<NotifyListener>> failed = new HashMap<URL, Set<NotifyListener>>(failedSubscribed);

			for (Map.Entry<URL, Set<NotifyListener>> entry : new HashMap<URL, Set<NotifyListener>>(failed).entrySet()) {
				if (entry.getValue() == null || entry.getValue().size() == 0) {
					failed.remove(entry.getKey());
				}
			}

			if (failed.size() > 0) {
				if (logger.isInfoEnabled()) {
					logger.info("Retry subscribe " + failed);
				}

				try {
					for (Map.Entry<URL, Set<NotifyListener>> entry : failed.entrySet()) {
						URL url = entry.getKey();
						Set<NotifyListener> listeners = entry.getValue();

						for (NotifyListener listener : listeners) {
							try {
								doSubscribe(url, listener);
								listeners.remove(listener);
							} catch (Throwable t) { // 忽略所有异常，等待下次重试
								logger.warn("Failed to retry subscribe " + failed + ", waiting for again, cause: " + t.getMessage(), t);
							}
						}
					}
				} catch (Throwable t) { // 忽略所有异常，等待下次重试
					logger.warn("Failed to retry subscribe " + failed + ", waiting for again, cause: " + t.getMessage(), t);
				}
			}
		}

		if (!failedUnsubscribed.isEmpty()) {
			Map<URL, Set<NotifyListener>> failed = new HashMap<URL, Set<NotifyListener>>(failedUnsubscribed);

			for (Map.Entry<URL, Set<NotifyListener>> entry : new HashMap<URL, Set<NotifyListener>>(failed).entrySet()) {
				if (entry.getValue() == null || entry.getValue().size() == 0) {
					failed.remove(entry.getKey());
				}
			}

			if (failed.size() > 0) {
				if (logger.isInfoEnabled()) {
					logger.info("Retry unsubscribe " + failed);
				}

				try {
					for (Map.Entry<URL, Set<NotifyListener>> entry : failed.entrySet()) {
						URL url = entry.getKey();
						Set<NotifyListener> listeners = entry.getValue();
						handleUrl(url);

						for (NotifyListener listener : listeners) {
							try {
								doUnsubscribe(url, listener);
								listeners.remove(listener);
							} catch (Throwable t) { // 忽略所有异常，等待下次重试
								logger.warn("Failed to retry unsubscribe " + failed + ", waiting for again, cause: " + t.getMessage(), t);
							}
						}
					}
				} catch (Throwable t) { // 忽略所有异常，等待下次重试
					logger.warn("Failed to retry unsubscribe " + failed + ", waiting for again, cause: " + t.getMessage(), t);
				}
			}
		}

		if (!failedNotified.isEmpty()) {
			Map<URL, Map<NotifyListener, List<URL>>> failed = new HashMap<URL, Map<NotifyListener, List<URL>>>(failedNotified);

			for (Map.Entry<URL, Map<NotifyListener, List<URL>>> entry : new HashMap<URL, Map<NotifyListener, List<URL>>>(failed).entrySet()) {
				if (entry.getValue() == null || entry.getValue().size() == 0) {
					failed.remove(entry.getKey());
				}
			}

			if (failed.size() > 0) {
				if (logger.isInfoEnabled()) {
					logger.info("Retry notify " + failed);
				}

				try {
					for (Map<NotifyListener, List<URL>> values : failed.values()) {
						for (Map.Entry<NotifyListener, List<URL>> entry : values.entrySet()) {
							try {
								NotifyListener listener = entry.getKey();
								List<URL> urls = entry.getValue();

								for (URL u : urls) {
									handleUrl(u);
								}

								listener.notify(urls);
								values.remove(listener);
							} catch (Throwable t) { // 忽略所有异常，等待下次重试
								logger.warn("Failed to retry notify " + failed + ", waiting for again, cause: " + t.getMessage(), t);
							}
						}
					}
				} catch (Throwable t) { // 忽略所有异常，等待下次重试
					logger.warn("Failed to retry notify " + failed + ", waiting for again, cause: " + t.getMessage(), t);
				}
			}
		}
	}

	private String toRootPath() {
		String root = (String) ReflectUtil.getFieldValue(this, "root");
		return root;
	}

	private String[] toCategoriesPath(URL url) {
		String[] categroies;

		if (Constants.ANY_VALUE.equals(url.getParameter(Constants.CATEGORY_KEY))) {
			categroies = new String[] { Constants.PROVIDERS_CATEGORY, Constants.CONSUMERS_CATEGORY, Constants.ROUTERS_CATEGORY,
					Constants.CONFIGURATORS_CATEGORY };
		} else {
			categroies = url.getParameter(Constants.CATEGORY_KEY, new String[] { Constants.DEFAULT_CATEGORY });
		}

		String[] paths = new String[categroies.length];

		for (int i = 0; i < categroies.length; i++) {
			paths[i] = toServicePath(url) + Constants.PATH_SEPARATOR + categroies[i];
		}

		return paths;
	}

	private String toServicePath(URL url) {
		String name = url.getServiceInterface();

		if (Constants.ANY_VALUE.equals(name)) {
			return toRootPath();
		}

		return toRootDir() + URL.encode(name);
	}

	private String toRootDir() {
		String root = (String) ReflectUtil.getFieldValue(this, "root");

		if (root.equals(Constants.PATH_SEPARATOR)) {
			return root;
		}

		return root + Constants.PATH_SEPARATOR;
	}

	private List<URL> toUrlsWithEmpty(URL consumer, String path, List<String> providers) {
		List<URL> urls = toUrlsWithoutEmpty(consumer, providers);

		if (urls == null || urls.isEmpty()) {
			int i = path.lastIndexOf('/');
			String category = i < 0 ? path : path.substring(i + 1);
			URL empty = consumer.setProtocol(Constants.EMPTY_PROTOCOL).addParameter(Constants.CATEGORY_KEY, category);
			urls.add(empty);
		}

		return urls;
	}

	private List<URL> toUrlsWithoutEmpty(URL consumer, List<String> providers) {
		List<URL> urls = new ArrayList<URL>();

		if (providers != null && providers.size() > 0) {
			for (String provider : providers) {
				provider = URL.decode(provider);

				if (provider.contains("://")) {
					URL url = URL.valueOf(provider);

					if (UrlUtils.isMatch(consumer, url)) {
						urls.add(url);
					}
				}
			}
		}

		return urls;
	}

	@SuppressWarnings("unchecked")
	private void saveProperties(URL url) {
		File file = (File) ReflectUtil.getFieldValue(this, "file");
		ConcurrentMap<URL, Map<String, List<URL>>> notified = (ConcurrentMap<URL, Map<String, List<URL>>>) ReflectUtil.getFieldValue(this, "notified");

		if (file == null) {
			return;
		}

		try {
			StringBuilder buf = new StringBuilder();
			Map<String, List<URL>> categoryNotified = notified.get(url);

			if (categoryNotified != null) {
				for (List<URL> us : categoryNotified.values()) {
					for (URL u : us) {
						if (buf.length() > 0) {
							buf.append(URL_SEPARATOR);
						}

						buf.append(u.toFullString());
					}
				}
			}

			Properties properties = (Properties) ReflectUtil.getFieldValue(this, "properties");
			AtomicLong lastCacheChanged = (AtomicLong) ReflectUtil.getFieldValue(this, "lastCacheChanged");
			Boolean syncSaveFile = (Boolean) ReflectUtil.getFieldValue(this, "syncSaveFile");
			ExecutorService registryCacheExecutor = (ExecutorService) ReflectUtil.getFieldValue(this, "registryCacheExecutor");
			properties.setProperty(url.getServiceKey(), buf.toString());
			long version = lastCacheChanged.incrementAndGet();

			if (syncSaveFile) {
				doSaveProperties(version);
			} else {
				registryCacheExecutor.execute(new SaveProperties(version));
			}
		} catch (Throwable t) {
			logger.warn(t.getMessage(), t);
		}
	}

	private class SaveProperties implements Runnable {
		private long version;

		private SaveProperties(long version) {
			this.version = version;
		}

		public void run() {
			doSaveProperties(version);
		}
	}

	private String toUrlPath(URL url) {
		return toCategoryPath(url) + Constants.PATH_SEPARATOR + URL.encode(url.toFullString());
	}

	private String toCategoryPath(URL url) {
		return toServicePath(url) + Constants.PATH_SEPARATOR + url.getParameter(Constants.CATEGORY_KEY, Constants.DEFAULT_CATEGORY);
	}

	private void handleUrl(URL url) {
		if (url != null && url.toString().startsWith("dubbo:")) {
			String providerApplication = url.getParameter(Constants.APPLICATION_KEY);
			String interfaceName = url.getParameter(Constants.INTERFACE_KEY);
			methodProviderMap.put(interfaceName, providerApplication);
		}
	}
}
