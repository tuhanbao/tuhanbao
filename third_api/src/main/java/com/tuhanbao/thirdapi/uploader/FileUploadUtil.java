package com.tuhanbao.thirdapi.uploader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.thirdapi.ProjectType;
import com.tuhanbao.thirdapi.ftp.FTPManager;
import com.tuhanbao.thirdapi.ftp.FtpConfig;

public class FileUploadUtil {
    private static Map<ProjectType, FileUploader> UPLOADERS = new HashMap<ProjectType, FileUploader>();
    
    private static FileUploader DEFAULT_FILE_UPLOADER = null;
    
    public static void registerFileUploader(ProjectType type, FileUploader uploader) {
        UPLOADERS.put(type, uploader);
    }

    public static String uploadFile(ProjectType type, String path, String fileName, InputStream is) {
        FileUploader fileUploader = getFileUploader(type);
        if (fileUploader == null) throw new MyException("no file uploader for this project");
        
        try {
            return fileUploader.uploadFile(path, fileName, is);
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
    }
    
    public static String uploadFile(ProjectType type, String path, File file) {
        FileUploader fileUploader = getFileUploader(type);
        if (fileUploader == null) throw new MyException("no file uploader for this project");
        try {
            return fileUploader.uploadFile(path, getFileName(file), new FileInputStream(file));
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
    }
    
    public static boolean removeFile(ProjectType type, String path, String fileName) {
        FileUploader fileUploader = getFileUploader(type);
        if (fileUploader == null) throw new MyException("no file uploader for this project");
        try {
            return fileUploader.removeFile(path, fileName);
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
    }

    public static boolean removeDic(ProjectType type, String path) {
        FileUploader fileUploader = getFileUploader(type);
        if (fileUploader == null) throw new MyException("no file uploader for this project");
        try {
            return fileUploader.removeDic(path);
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
    }

    private static FileUploader getFileUploader(ProjectType type) {
        if (UPLOADERS.containsKey(type)) return UPLOADERS.get(type);
        return getDefaultFileUploader();
    }
    
    private static FileUploader getDefaultFileUploader() {
        if (DEFAULT_FILE_UPLOADER == null) {
            if (ConfigManager.getConfig(FtpConfig.KEY) != null) {
                DEFAULT_FILE_UPLOADER = FTPManager.getDefaultInstance();
            }
            else {
                throw new MyException("no file uploader for this project!");
            }
        }
        return DEFAULT_FILE_UPLOADER;
    }

    public static String getFileName(File file) {
        String suffix = FileUtil.getSuffix(file.getAbsolutePath());
        String filename = UUID.randomUUID().toString() + suffix;
        return filename;
    }
}
