package com.tuhanbao.thirdapi.ftp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.rm.IResource;
import com.tuhanbao.base.util.rm.IResourceManager;

public class FTPClientResource extends IResource {

    private FTPClient ftpClient;
    
    public FTPClientResource(IResourceManager manager, FTPClient ftpClient) {
        super(manager);
        this.ftpClient = ftpClient;
    }
    
    @Override
    public void destroy() {
        if (ftpClient != null) {
            try {
                ftpClient.logout();
            }
            catch (IOException e) {
            }
            
            try {
                ftpClient.disconnect();
            }
            catch (IOException e) {
            }
            
            ftpClient = null;
        }
    }

    @Override
    public void recreateResource() throws Exception {
        this.destroy();
        this.ftpClient = ((FTPManager)this.manager).getFTPClient();
    }

    public void uploadFile(String path, String fileName, InputStream is) throws Exception {
        FTPManager.changeWorkingDirectory(ftpClient, ((FTPManager)this.manager).getStringValue(FtpConfig.ROOT_PATH_NAME), path);
        boolean result = true;
        try {
            result = ftpClient.storeFile(fileName, is);
        }
        catch (IOException e) {
            result = false;
        }
        if (!result) throw new MyException("store file error : " + fileName);
    }
    
    public InputStream getFile(String path, String fileName) throws Exception {
        FTPManager.changeWorkingDirectory(ftpClient, ((FTPManager)this.manager).getStringValue(FtpConfig.ROOT_PATH_NAME), path);
        return ftpClient.retrieveFileStream(fileName);
    }

    public boolean removeFile(String path, String fileName) throws Exception {
        FTPManager.changeWorkingDirectory(ftpClient, ((FTPManager)this.manager).getStringValue(FtpConfig.ROOT_PATH_NAME), path);
        boolean result = true;
        try {
            result = ftpClient.deleteFile(fileName);
        }
        catch (IOException e) {
            result = false;
        }
        if (!result) throw new MyException("remove file error : " + path + "/" + fileName);
        return result;
    }
    
    public boolean removeDic(String path) throws Exception {
        FTPManager.changeWorkingDirectory(ftpClient, ((FTPManager)this.manager).getStringValue(FtpConfig.ROOT_PATH_NAME), path);
        boolean result = true;
        try {
            result = ftpClient.removeDirectory(path);
        }
        catch (IOException e) {
            result = false;
        }
        if (!result) throw new MyException("delete dic error : " + path);
        return result;
    }

}
