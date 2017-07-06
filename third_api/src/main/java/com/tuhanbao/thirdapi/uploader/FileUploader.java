package com.tuhanbao.thirdapi.uploader;

import java.io.InputStream;

public interface FileUploader {
    //path为相对路径
    String uploadFile(String path, String fileName, InputStream is) throws Exception;
    
    /**
     * 获取文件
     * @param path
     * @param fileName
     * @return
     */
    InputStream getFile(String path, String fileName) throws Exception;
    
    boolean removeFile(String path, String fileName) throws Exception;

    boolean removeDic(String path) throws Exception;
}
