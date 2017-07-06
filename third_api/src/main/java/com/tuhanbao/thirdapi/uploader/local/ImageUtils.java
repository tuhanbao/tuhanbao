package com.tuhanbao.thirdapi.uploader.local;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.RandomUtil;
import com.tuhanbao.base.util.objutil.TimeUtil;
import com.tuhanbao.thirdapi.ProjectType;

/**
 * 整个类带整改
 * 
 * @author Administrator
 * @deprecated
 */
public class ImageUtils {
    
    private static final String REAL_PATH = "/root/share_file/pic_path";
    
    private static final String HTTP_URL = "http://b2b.hahanongzhuang.com:10839/pic_path";

    /** 
     * 保存图片到本地
     *
     * @param filePath 保存路径
     * @param fileName 文件名称
     * @param file 文件
     * @param isNeedCompress 是否需要压缩
     * @return 保存文件全名
     */
    public static String savePicToLocal(ProjectType projectType, InputStream file, String suffix, Boolean isNeedCompress, Integer width, Integer height) {
        int bytesWritten = 0;
        int byteCount = 0;
        OutputStream outputStream = null;
        String filePath = getImgUrl(projectType, suffix);
        try {
            File dir = new File(filePath);
            FileUtil.createFile(dir);
            byte[] bytes = new byte[file.available()];
            outputStream = new FileOutputStream(filePath);
            while ((byteCount = file.read(bytes)) != -1) {
                outputStream.write(bytes, bytesWritten, byteCount);
                bytesWritten += byteCount;
            }
            LogManager.info("保存文件：" + filePath);
            if (isNeedCompress) {
                // 需要压缩
                filePath = compress(filePath, width, height);
            }
        } catch (FileNotFoundException e) {
            LogManager.error(e);
            return null;
        } catch (IOException e) {
            LogManager.error(e);
            return null;
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LogManager.error(e);
            }
        }
        return filePath.replace(REAL_PATH, HTTP_URL);
    }

    /** 
     * 保存图片到本地
     *
     * @param filePath 保存路径
     * @param fileName 文件名称
     * @param file 文件
     * @param isNeedCompress 是否需要压缩
     * @return  保存文件全名
     */
    public static String savePicToLocal(ProjectType projectType, byte[] file, String suffix, Boolean isNeedCompress, Integer width, Integer height) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File f = null;
        String filePath = getImgUrl(projectType, suffix);
        try {
            File dir = new File(filePath);
            FileUtil.createDir(dir);
            fos = new FileOutputStream(f);
            bos = new BufferedOutputStream(fos);
            bos.write(file);
            LogManager.info("保存文件：" + filePath);
            if (isNeedCompress) {
                // 需要压缩
                filePath = compress(filePath, width, height);
            }
        } catch (FileNotFoundException e) {
            LogManager.error(e);
        } catch (IOException e) {
            LogManager.error(e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    LogManager.error(e1);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    LogManager.error(e1);
                }
            }
        }
        return filePath.replace(REAL_PATH, HTTP_URL);
    }

    /** 
     * 压缩图片
     *
     * @param filePath 路径
     * @param fileName 名称
     * @param width 压缩后宽
     * @param height 压缩后高
     * @return 压缩后文件名称
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private static String compress(String fileName, Integer width, Integer height) throws FileNotFoundException, IOException {
        CompressPic cp = new CompressPic();
        if (null == width || null == height) {
            return fileName;
        }
        File inputFile = new File(fileName);
        BufferedImage sourceImg = ImageIO.read(new FileInputStream(inputFile));
        LogManager.info("上传图片规格：" + sourceImg.getWidth() + "x" + sourceImg.getHeight());
        String fileNewName = FileUtil.addSuffix2File(fileName, "_" + width + "_" + height);
        File outputFile = new File(fileNewName);
        LogManager.info("压缩图片地址：" + fileNewName);
        if (cp.compressPic(inputFile, outputFile, width, height, true)) {
            return fileNewName;
        }
        return fileName;
    }
    
    public static String getImgUrl(ProjectType projectType, String suffix) {
        return getImgUrl(projectType, null, suffix);
    }
    
    public static String getImgUrl(ProjectType projectType, String path, String suffix) {
        StringBuilder sb = new StringBuilder();
        sb.append(REAL_PATH).append(Constants.FILE_SEP).append(projectType.name().toLowerCase());
        if (path == null) {
            int[] ymd = TimeUtil.getTodayYearMonthDayHour();
            path = TimeUtil.getYmdStr(new int[] { ymd[0], ymd[1], ymd[2] }, Constants.FILE_SEP);
        }
        sb.append(Constants.FILE_SEP).append(path).append(Constants.FILE_SEP).append(RandomUtil.randomLetterAndNumberString(32)).append(suffix);
        return sb.toString();
    }
}
