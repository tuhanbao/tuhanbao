package com.tuhanbao.base.util.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;


/**
 * 
 * @author zhihongp
 *
 */
public class ImageUtil {

    public static boolean isImage(String filePath) throws IOException {
        File file = new File(filePath);
        return isImage(file);
    }

    public static boolean isImage(File file) throws IOException {
        boolean isImage = false;
        InputStream is = null;
        BufferedImage src = null;

        try {
            is = new FileInputStream(file);
            src = ImageIO.read(is);

            if (src != null) {
                isImage = true;
            }
        } finally {
            is.close();
        }

        return isImage;
    }

    /**
     * 获得图片大小
     * 
     * @param imagePath 图片绝对路径
     * @return 图片大小(单位:字节)
     */
    public static long getSize(String imagePath) {
        File file = new File(imagePath);
        return file.length();
    }

    /**
     * 获取图片格式
     * 
     * @param file 图片路径
     * @return 图片格式
     * @throws IOException
     */
    public static String getImageFormatName(String filePath) throws IOException {
        String formatName = null;
        ImageInputStream iis = null;

        try {
            iis = ImageIO.createImageInputStream(new File(filePath));
            Iterator<ImageReader> imageReader = ImageIO.getImageReaders(iis);

            if (imageReader.hasNext()) {
                ImageReader reader = imageReader.next();
                formatName = reader.getFormatName();
            }
        } finally {
            if (iis != null) {
                iis.close();
            }
        }

        return formatName;
    }

    /**
     * 裁剪图片
     *
     * @param srcImagePath 源图片路径
     * @param newImagePath 处理后图片路径
     * @param x 起始X坐标
     * @param y 起始Y坐标
     * @param width 裁剪宽度
     * @param height 裁剪高度
     * @return 返回true说明裁剪成功,否则失败
     * @throws IM4JavaException
     * @throws InterruptedException
     * @throws IOException
     */
    public static boolean cutImage(String srcImagePath, String newImagePath, int x, int y, int width, int height) throws IOException, InterruptedException,
            IM4JavaException {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.crop(width, height, x, y);
        op.addImage(newImagePath);
        ConvertCmd convert = new ConvertCmd(true);
        convert.run(op);
        return true;
    }

    /**
     * 原图压缩质量(图片高度和宽度不变)
     * 
     * @param srcImagePath 源图片路径
     * @param newImagePath 处理后图片路径
     * @param quality 图片质量
     * @return 返回true说明缩放成功,否则失败
     * @throws IOException
     * @throws InterruptedException
     * @throws IM4JavaException
     */
    public static boolean zoomImage(String srcImagePath, String newImagePath, double quality) throws IOException, InterruptedException, IM4JavaException {
        return zoomImage(srcImagePath, newImagePath, null, null, quality);
    }

    /**
     * 根据尺寸缩放压缩图片[等比例缩放:参数height为null,按宽度缩放比例缩放;参数width为null,按高度缩放比例缩放]
     *
     * @param srcImagePath 源图片路径
     * @param newImagePath 处理后图片路径
     * @param width 缩放后的图片宽度
     * @param height 缩放后的图片高度
     * @param quality 图片质量
     * @return 返回true说明缩放成功,否则失败
     * @throws IM4JavaException
     * @throws InterruptedException
     * @throws IOException
     */
    public static boolean zoomImage(String srcImagePath, String newImagePath, Integer width, Integer height, double quality) throws IOException,
            InterruptedException, IM4JavaException {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);

        if (width == null && height != null) {
            op.resize(null, height);
        } else if (width != null && height == null) {
            op.resize(width);
        } else if (width != null && height != null) {
            op.resize(width, height);
        }

        op.quality(quality);
        op.addImage(newImagePath);
        ConvertCmd convert = new ConvertCmd(true);
        convert.run(op);
        return true;
    }

    /**
     * 图片旋转
     *
     * @param imagePath 源图片路径
     * @param newPath 处理后图片路径
     * @param degree 旋转角度
     * @return 返回true说明缩放成功,否则失败
     * @throws IM4JavaException
     * @throws InterruptedException
     * @throws IOException
     */
    public static boolean rotateImage(String srcImagePath, String newImagePath, double degree) throws IOException, InterruptedException, IM4JavaException {
        degree = degree % 360;

        if (degree <= 0) {
            degree = 360 + degree;
        }

        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.rotate(degree);
        op.addImage(newImagePath);
        ConvertCmd convert = new ConvertCmd(true);
        convert.run(op);
        return true;
    }

    /**
     * 给图片加文字
     * 
     * @param srcImagePath 源图片路径
     * @param newImagePath 处理后的图片路径
     * @param text 文字内容
     * @return 返回true说明缩放成功,否则失败
     * @throws IM4JavaException
     * @throws InterruptedException
     * @throws IOException
     */
    public static boolean addImgText(String srcImagePath, String newImagePath, String text) throws IOException, InterruptedException, IM4JavaException {
        IMOperation op = new IMOperation();
        op.font("Arial");
        op.gravity("southeast");
        op.pointsize(18).fill("#BCBFC8").draw("text 0,0 " + text);
        op.addImage();
        op.addImage();
        ConvertCmd convert = new ConvertCmd(true);
        convert.run(op, srcImagePath, srcImagePath);
        return true;
    }

    /**
     * 给图片加水印
     * 
     * @param srcImagePath 源图片路径
     * @param waterImagePath 水印路径
     * @param newImagePath 处理后的图片路径
     * @param gravity 图片位置
     * @param dissolve 水印透明度
     * @return 返回true说明缩放成功,否则失败
     * @throws IM4JavaException
     * @throws InterruptedException
     * @throws IOException
     */
    public static boolean waterMark(String srcImagePath, String waterImagePath, String newImagePath, String gravity, int dissolve) throws IOException,
            InterruptedException, IM4JavaException {
        IMOperation op = new IMOperation();
        op.addImage(waterImagePath);
        op.gravity(gravity);
        op.dissolve(dissolve);
        op.addImage(srcImagePath);
        op.addImage(newImagePath);
        CompositeCmd convert = new CompositeCmd(true);
        convert.run(op);
        return true;
    }

//    public static void main(String[] args) throws Exception {
//        String url = "D:/test.png";
//        String newUrl = "D:/test2.png";
//        ImageUtil.zoomImage(url, newUrl, 1.0d);
//    }
}
