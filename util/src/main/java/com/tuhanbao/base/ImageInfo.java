package com.tuhanbao.base;

import java.util.Map;

public class ImageInfo extends FileInfo {

    /**
     * 
     */
    private static final long serialVersionUID = -1825849155575262967L;

    private int height;

    private int width;

    public ImageInfo() {
        super();
    }

    public ImageInfo(String fileId, String fileName, byte[] content, int height, int width, long size, String author, Map<String, String> extraInfo) {
        super(fileId, fileName, content, size, author, extraInfo);
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "ImageInfo [height=" + height + ", width=" + width + "]";
    }

}
