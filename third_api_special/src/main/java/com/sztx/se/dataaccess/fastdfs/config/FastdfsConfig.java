package com.sztx.se.dataaccess.fastdfs.config;

import java.util.ArrayList;
import java.util.List;

public class FastdfsConfig {
	
	/**
	 * 连接超时(单位：秒)
	 */
	private int connectTimeout;
	
	/**
	 * 网络超时(单位：秒)
	 */
	private int networkTimeout;

	private String charset;

	private int trackerHttpPort;

	private boolean antiStealToken;

	private String secretKey;

	private String trackerServers;

	private List<String> trackerServerList;

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getNetworkTimeout() {
		return networkTimeout;
	}

	public void setNetworkTimeout(int networkTimeout) {
		this.networkTimeout = networkTimeout;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getTrackerHttpPort() {
		return trackerHttpPort;
	}

	public void setTrackerHttpPort(int trackerHttpPort) {
		this.trackerHttpPort = trackerHttpPort;
	}

	public boolean getAntiStealToken() {
		return antiStealToken;
	}

	public void setAntiStealToken(boolean antiStealToken) {
		this.antiStealToken = antiStealToken;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getTrackerServers() {
		return trackerServers;
	}

	public void setTrackerServers(String trackerServers) {
		this.trackerServers = trackerServers;
		String[] trackerServerArray = this.trackerServers.split(",");
		
		if (trackerServerArray != null && trackerServerArray.length > 0) {
			if (this.trackerServerList == null) {
				this.trackerServerList = new ArrayList<String>();
			}
			
			for (String trackerServer : trackerServerArray) {
				this.trackerServerList.add(trackerServer);
			}
		}
	}

	public List<String> getTrackerServerList() {
		return trackerServerList;
	}

	@Override
	public String toString() {
		return "FastdfsConfig [connectTimeout=" + connectTimeout + ", networkTimeout=" + networkTimeout + ", charset=" + charset + ", trackerHttpPort="
				+ trackerHttpPort + ", antiStealToken=" + antiStealToken + ", secretKey=" + secretKey + ", trackerServers=" + trackerServers
				+ ", trackerServerList=" + trackerServerList + "]";
	}

}
