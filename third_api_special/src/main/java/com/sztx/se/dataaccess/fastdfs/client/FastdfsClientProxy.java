package com.sztx.se.dataaccess.fastdfs.client;

public class FastdfsClientProxy {

	private String fastdfsSourceKey;

	private FastdfsClient fastdfsClient;

	private boolean isDefault;

	public String getFastdfsSourceKey() {
		return fastdfsSourceKey;
	}

	public void setFastdfsSourceKey(String fastdfsSourceKey) {
		this.fastdfsSourceKey = fastdfsSourceKey;
	}

	public FastdfsClient getFastdfsClient() {
		return fastdfsClient;
	}

	public void setFastdfsClient(FastdfsClient fastdfsClient) {
		this.fastdfsClient = fastdfsClient;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "FastdfsClientProxy [fastdfsSourceKey=" + fastdfsSourceKey + ", fastdfsClient=" + fastdfsClient + ", isDefault=" + isDefault + "]";
	}

}
