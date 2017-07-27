package com.sztx.se.dataaccess.fastdfs;

import java.io.InputStream;
import java.util.Map;

import com.tuhanbao.base.FileInfo;

/**
 * 
 * @author zhihongp
 *
 */
public interface BaseFastdfsDAO {

	/**
	 * 上传文件到fastdfs中(file必须是本地文件)
	 * 
	 * @param filePath
	 * @param author
	 * @param extraInfo
	 * @param isImage
	 * @return
	 */
	String upload(String filePath, String author, Map<String, String> extraInfo);

	/**
	 * 上传文件到fastdfs中
	 * 
	 * @param fileName
	 * @param fileBytes
	 * @param size
	 * @param inputStream
	 * @param author
	 * @param extraInfo
	 * @return
	 */
	String upload(String fileName, byte[] fileBytes, long size, InputStream inputStream, String author, Map<String, String> extraInfo);

	/**
	 * 从fastdfs中下载文件
	 * 
	 * @param fileId
	 * @param localFilePath
	 * @return
	 */
	FileInfo download(String fileId, String localFilePath);

	/**
	 * 从fastdfs中删除文件
	 * 
	 * @param fileId
	 * @param localFilePath
	 * @param isImage
	 * @return
	 */
	boolean delete(String fileId, String localFilePath);
}
