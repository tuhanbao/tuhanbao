package com.tuhanbao.thirdapi.uploader.local;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tuhanbao.base.util.log.LogManager;

/**  
 *  缩略图实现，将图片(jpg、bmp、png、gif等等)真实的变成想要的大小  
 *  整个类待整改
 *  @deprecated
 */
public class CompressPic {

	private File inputFile; // 文件对象
	private File outputFile; // 输出图路径
	private int outputWidth; // 默认输出图片宽
	private int outputHeight; // 默认输出图片高
	private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)

	public CompressPic() {

	}

	private boolean compressPic() {
		try {
			// 获得源文件
			if (!inputFile.exists()) {
				return false;
			}
			Image img = ImageIO.read(inputFile);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				return false;
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (this.proportion == true) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) img.getWidth(null)) / (double) outputWidth + 0.1;
					double rate2 = ((double) img.getHeight(null)) / (double) outputHeight + 0.1;
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (img.getWidth(null) / rate);
					newHeight = (int) (img.getHeight(null) / rate);
				} else {
					newWidth = outputWidth; // 输出的图片宽度
					newHeight = outputHeight; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
				tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
				FileOutputStream out = new FileOutputStream(outputFile);
				// JPEGImageEncoder可适用于其他图片类型的转换
				ImageIO.write(tag, "PNG", out);
				// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				// encoder.encode(tag);
				out.close();
			}
		} catch (IOException ex) {
			LogManager.error(ex);
			return false;
		}
		return true;
	}

	public boolean compressPic(File inputFile, File outputFile, int width, int height, boolean gp) {
		// 输入图路径
		this.inputFile = inputFile;
		// 输出图路径
		this.outputFile = outputFile;
		// 设置图片长宽
		this.outputWidth = width;
		this.outputHeight = height;
		// 是否是等比缩放 标记
		this.proportion = gp;
		return compressPic();
	}
}