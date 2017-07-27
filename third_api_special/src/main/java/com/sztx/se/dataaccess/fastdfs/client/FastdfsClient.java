package com.sztx.se.dataaccess.fastdfs.client;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;

import com.sztx.se.dataaccess.fastdfs.config.FastdfsConfig;
import com.tuhanbao.base.FileInfo;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.FileUtil;

public class FastdfsClient {

	private FastdfsConfig fastdfsConfig;

	private TrackerClient trackerClient;

	public void setFastdfsConfig(FastdfsConfig fastdfsConfig) {
		this.fastdfsConfig = fastdfsConfig;
	}

	/**
	 * 
	 * @param filePath
	 * @param fileExt
	 * @param extraInfo
	 * @return
	 */
	public String upload(String filePath, String fileExt, Map<String, String> extraInfo) {
		TrackerServer trackerServer = null;

		try {
			trackerServer = trackerClient.getConnection();
			StorageClient1 storageClient = getStorageClient(trackerServer, null);
			int size = extraInfo.size();
			NameValuePair[] metaList = new NameValuePair[size];
			int i = 0;

			for (Entry<String, String> en : extraInfo.entrySet()) {
				metaList[i] = new NameValuePair(en.getKey(), en.getValue());
				i++;
			}

			String fileId = storageClient.upload_file1(filePath, fileExt, metaList);
			return fileId;
		} catch (Exception e) {
			throw MyException.getMyException(e);
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (IOException e) {
					throw MyException.getMyException(e);
				}
			}
		}
	}

	/**
	 * 
	 * @param filePath
	 * @param fileExt
	 * @param extraInfo
	 * @return
	 */
	public String upload(byte[] fileBytes, String fileExt, Map<String, String> extraInfo) {
		TrackerServer trackerServer = null;

		try {
			trackerServer = trackerClient.getConnection();
			StorageClient1 storageClient = getStorageClient(trackerServer, null);
			int size = extraInfo.size();
			NameValuePair[] metaList = new NameValuePair[size];
			int i = 0;

			for (Entry<String, String> en : extraInfo.entrySet()) {
				metaList[i] = new NameValuePair(en.getKey(), en.getValue());
				i++;
			}

			String fileId = storageClient.upload_file1(fileBytes, fileExt, metaList);
			return fileId;
		} catch (Exception e) {
			throw MyException.getMyException(e);
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (IOException e) {
					throw MyException.getMyException(e);
				}
			}
		}
	}

	/**
	 * 
	 * @param fileId
	 * @return
	 */
	public FileInfo download(String fileId) {
		TrackerServer trackerServer = null;

		try {
			trackerServer = trackerClient.getConnection();
			StorageClient1 storageClient = getStorageClient(trackerServer, null);
			byte[] content = storageClient.download_file1(fileId);

			if (content == null || content.length == 0) {
				return null;
			}

			NameValuePair[] metaList = storageClient.get_metadata1(fileId);
			Map<String, String> extraInfo = null;
			long size = 0l;
			String author = null;
			String fileName = null;

			if (metaList != null && metaList.length > 0) {
				for (int i = 0; i < metaList.length; i++) {
					NameValuePair meta = metaList[i];
					String name = meta.getName();

					if (name.equals(FileInfo.KEY_SIZE)) {
						size = Long.valueOf(meta.getValue());
					} else if (name.equals(FileInfo.KEY_AUTHOR)) {
						author = meta.getValue();
					} else if (name.equals(FileInfo.KEY_NAME)) {
						fileName = meta.getValue();
					} else {
						if (extraInfo == null) {
							extraInfo = new HashMap<String, String>();
						}

						extraInfo.put(meta.getName(), meta.getValue());
					}
				}
			}

			return new FileInfo(fileId, fileName, content, size, author, extraInfo);
		} catch (Exception e) {
			throw MyException.getMyException(e);
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (IOException e) {
					throw MyException.getMyException(e);
				}
			}
		}
	}

	/**
	 * 
	 * @param fileId
	 * @param localFile
	 * @return
	 */
	public FileInfo download(String fileId, String localFilePath) {
		FileInfo fileInfo = download(fileId);

		if (fileInfo != null) {
			try {
				File file = FileUtil.createFile(fileInfo.getContent(), localFilePath);

				if (!file.exists() || !file.isFile()) {
					throw new MyException("Fastdfs download file save to " + localFilePath + " fail");
				}
			} catch (IOException e) {
				throw MyException.getMyException(e);
			}
		}

		return fileInfo;
	}

	/**
	 * 
	 * @param fileId
	 * @return
	 */
	public boolean deleteFile(String fileId) {
		TrackerServer trackerServer = null;

		try {
			trackerServer = trackerClient.getConnection();
			StorageClient1 storageClient = getStorageClient(trackerServer, null);
			int r = storageClient.delete_file1(fileId);
			return (r == 0);
		} catch (Exception e) {
			throw MyException.getMyException(e);
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (IOException e) {
					throw MyException.getMyException(e);
				}
			}
		}
	}

	/**
	 * 
	 * @param fileId
	 * @param localFilePath
	 * @return
	 */
	public boolean deleteFile(String fileId, String localFilePath) {
		boolean flag = deleteFile(fileId);

		if (flag) {
			flag = FileUtil.delete(localFilePath);
		}

		return flag;
	}

	/*
	 * 
	 */
	public void afterPropertiesSet() {
		trackerClient = new TrackerClient();
		ClientGlobal.setG_connect_timeout(fastdfsConfig.getConnectTimeout() * 1000);
		ClientGlobal.setG_network_timeout(fastdfsConfig.getNetworkTimeout() * 1000);
		ClientGlobal.setG_charset(fastdfsConfig.getCharset());
		ClientGlobal.setG_anti_steal_token(fastdfsConfig.getAntiStealToken());
		ClientGlobal.setG_secret_key(fastdfsConfig.getSecretKey());
		ClientGlobal.setG_tracker_http_port(fastdfsConfig.getTrackerHttpPort());
		List<String> trackerServerList = fastdfsConfig.getTrackerServerList();
		InetSocketAddress[] trackerServers = new InetSocketAddress[trackerServerList.size()];

		for (int i = 0; i < trackerServerList.size(); ++i) {
			String trackerServer = trackerServerList.get(i);
			int idx = trackerServer.lastIndexOf(':');
			String ip = trackerServer.substring(0, idx);
			int port = Integer.valueOf(trackerServer.substring(idx + 1));
			trackerServers[i] = new InetSocketAddress(ip, port);
		}

		TrackerGroup trackerGroup = new TrackerGroup(trackerServers);
		ClientGlobal.setG_tracker_group(trackerGroup);
		trackerClient = new TrackerClient(trackerGroup);
	}

	private StorageClient1 getStorageClient(TrackerServer trackerServer, StorageServer storageServer) {
		return new StorageClient1(trackerServer, storageServer);
	}
}
