package com.sztx.se.dataaccess.fastdfs.impl;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sztx.se.dataaccess.fastdfs.BaseFastdfsDAO;
import com.sztx.se.dataaccess.fastdfs.source.DynamicFastdfsSource;
import com.tuhanbao.base.FileInfo;

public abstract class BaseFastdfsDAOImpl implements BaseFastdfsDAO {

	@Autowired(required = false)
	protected DynamicFastdfsSource fastdfsClient;

	@Override
	public String upload(String filePath, String author, Map<String, String> extraInfo) {
		String fileId = fastdfsClient.upload(filePath, author, extraInfo);
		return fileId;
	}

	@Override
	public String upload(String fileName, byte[] fileBytes, long size, InputStream inputStream, String author, Map<String, String> extraInfo) {
		String fileId = fastdfsClient.upload(fileName, fileBytes, size, inputStream, author, extraInfo);
		return fileId;
	}

	@Override
	public FileInfo download(String fileId, String localFilePath) {
		FileInfo fileInfo = fastdfsClient.download(fileId, localFilePath);
		return fileInfo;
	}

	@Override
	public boolean delete(String fileId, String localFilePath) {
		boolean flag = fastdfsClient.delete(fileId, localFilePath);
		return flag;
	}
}
