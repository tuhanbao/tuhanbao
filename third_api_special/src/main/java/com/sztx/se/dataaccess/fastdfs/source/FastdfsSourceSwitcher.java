package com.sztx.se.dataaccess.fastdfs.source;

/**
 * Fastdfs动态数据源选择器
 * 
 * @author zhihongp
 * 
 */
public class FastdfsSourceSwitcher {

	private static final ThreadLocal<String> fastdfsContextHolder = new ThreadLocal<String>();

	public static void setFastdfsSourceTypeInContext(String fastdfsSourceType) {
		fastdfsContextHolder.set(fastdfsSourceType);
	}

	public static String getFastdfsSourceType() {
		String fastdfsSourceType = (String) fastdfsContextHolder.get();
		return fastdfsSourceType;
	}

	public static String getFastdfsSourceTypeFromContext() {
		String fastdfsSourceType = (String) fastdfsContextHolder.get();
		return fastdfsSourceType;
	}

	public static void clearFastdfsSourceType() {
		fastdfsContextHolder.remove();
	}
}
