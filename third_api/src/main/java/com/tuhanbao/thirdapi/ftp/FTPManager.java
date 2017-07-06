package com.tuhanbao.thirdapi.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.rm.IResourceManager;
import com.tuhanbao.thirdapi.uploader.FileUploader;

public class FTPManager extends IResourceManager implements FileUploader {
    private Config config = null;
    
    private static FTPManager INSTANCE = null;
    
    public FTPManager() {
        this(null);
    }

    public FTPManager(Config config) {
        super(getIntValue(config, FtpConfig.MIN_FTP_SIZE_NAME), getIntValue(config, FtpConfig.MAX_FTP_SIZE_NAME), 24 * 60, false);
        this.config = config;
    }

    @Override
    protected FTPClientResource createResource() throws Exception {
        FTPClient ftpClient = getFTPClient();
        return new FTPClientResource(this, ftpClient);
    }

    public FTPClient getFTPClient() throws SocketException, IOException {
        String hostname = getStringValue(FtpConfig.HOSTNAME_NAME);
        int port = getIntValue(config, FtpConfig.FTPPORT_NAME);
        String username = getStringValue(FtpConfig.USERNAME_NAME);
        String password = getStringValue(FtpConfig.PASSWORD_NAME);
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(Constants.UTF_8);
        // 连接FTP服务器
        ftpClient.connect(hostname, port);
        // 登录FTP服务器
        ftpClient.login(username, password);
        // 是否成功登录FTP服务器
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            throw new MyException("ftp connect error !!");
        }
        
        String rootPath = getStringValue(FtpConfig.ROOT_PATH_NAME);
        if (!StringUtil.isEmpty(rootPath) && !ftpClient.changeWorkingDirectory(rootPath)) {
            changeWorkingDirectory(ftpClient, Constants.EMPTY, rootPath);
        }

        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        
        return ftpClient;
    }

    /**
     * 创建目录，而且:
     * ftpclient会切换到新建的目录中
     * 
     * @param ftpClient
     * @param rootPath
     * @param filePath
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void changeWorkingDirectory(FTPClient ftpClient, String rootPath, String filePath) throws UnsupportedEncodingException, IOException {
        if (null == ftpClient || StringUtil.isEmpty(filePath)) {
            throw new MyException("ftp create path error : " + filePath);
        }
        
        String fullPath = FileUtil.appendPath(rootPath, filePath);
        if (ftpClient.changeWorkingDirectory(fullPath)) return;
        
        String[] moreDir = FileUtil.getMenus(filePath);
        String existDir = rootPath;
        boolean flag = false;
        for (String dir : moreDir) {
            if (!StringUtil.isEmpty(dir)) {
                existDir = FileUtil.appendPath(existDir, dir);
                if (!ftpClient.changeWorkingDirectory(existDir.toString())) {
                    flag = ftpClient.makeDirectory(existDir.toString());
                    if (!flag) {
                        throw new MyException("ftp create path error : " + filePath);
                    }
                }
            }
        }
        LogManager.debug("create dir success: " + filePath);
        if (ftpClient.changeWorkingDirectory(fullPath)) return;
        
        throw new MyException("ftp change path error : " + filePath);
    }

    @Override
    public String uploadFile(String path, String fileName, InputStream is) throws Exception {
        FTPClientResource resource = (FTPClientResource)this.getResource();
        resource.uploadFile(path, fileName, is);
        resource.release();
        return FileUtil.appendPath(getStringValue(FtpConfig.SHOW_PATH_NAME), path, fileName);
    }
    
    @Override
    public InputStream getFile(String path, String fileName) throws Exception {
        FTPClientResource resource = (FTPClientResource)this.getResource();
        InputStream is = resource.getFile(path, fileName);
        resource.release();
        return is;
    }

    @Override
    public boolean removeFile(String path, String fileName) throws Exception {
        FTPClientResource resource = (FTPClientResource)this.getResource();
        boolean result = resource.removeFile(path, fileName);
        resource.release();
        return result;
    }

    @Override
    public boolean removeDic(String path) throws Exception {
        FTPClientResource resource = (FTPClientResource)this.getResource();
        boolean result = resource.removeDic(path);
        resource.release();
        return result;
    }
    
    public String getStringValue(String key) {
        if (config != null) {
            return config.getString(key);
        }
        return FtpConfig.getStringValue(key);
    }

    public static int getIntValue(Config config, String key) {
        if (config != null) {
            String value = config.getString(key);
            if (StringUtil.isEmpty(value)) return 0;
            else return Integer.valueOf(value);
        }
        return FtpConfig.getIntValue(key);
    }

    public synchronized static FTPManager getDefaultInstance() {
        if (INSTANCE == null) INSTANCE = new FTPManager();
        return INSTANCE;
    }

}
