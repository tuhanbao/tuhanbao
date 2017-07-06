package com.tuhanbao.base.util.io.txt;

import java.nio.ByteBuffer;

import com.tuhanbao.base.util.exception.MyException;

public class FileInputStreamPool {
    
    private ByteBuffer bb;
    
    private byte[] bytes;
    
    private int position = 0;
    
    private int size;
    
    private Throwable e;
    
    private boolean isFinish = false;
    
    //因为比较常用，额外开了一个utf-8文本读取模式
    private final int model;
            
    public static final int BYTE = 0, UTF_8_TXT = 1;
    
    //大概能缓存的批次
    private static final int DEFAULT_CACHE_BATCH = 10;
    
    public FileInputStreamPool(int size) {
        this(size, UTF_8_TXT);
    }

    public FileInputStreamPool(int size, int model) {
        this.size = size * DEFAULT_CACHE_BATCH;
        if (this.size < 0) this.size = Integer.MAX_VALUE;
        bytes = new byte[this.size];
        bb = ByteBuffer.allocate(size);
        this.model = model;
    }
    
//    public static void main(String args[]) throws IOException, InterruptedException {
////        TxtUtil.readSync(new File("D:\\ip_global_20170606.list"), Constants.UTF_8, 1000);
//        FileInputStreamPool fisp = TxtUtil.readSync(new File("D:\\test1.txt"), Constants.UTF_8, 100);
//        while (!fisp.isReadFinish()) {
//            System.out.println(new String(fisp.get()));
//        }
//        System.exit(0);
//    }

    public ByteBuffer getByteBuffer() {
        return this.bb;
    }

    public synchronized boolean isReady() throws InterruptedException {
        check();
        int length = bb.position();
        while (position + length >= this.size) {
            this.wait();
        }
        if (length > 0) {
            System.arraycopy(bb.array(), 0, bytes, position, length);
            this.position = position + length;
            bb.clear();
            this.notify();
        }
        return true;
    }
    
    /**
     * 可以自己判断readFinish，或者根据返回结果是否为null判断是否已经读完
     * 
     * @return
     * @throws InterruptedException
     */
    public synchronized byte[] get() throws InterruptedException {
        check();
        if (isReadFinish()) return null;
        int readPosition = getReadPosition();
        while (readPosition == 0) {
            wait();
            readPosition = getReadPosition();
        }
        byte[] result = new byte[readPosition];
        System.arraycopy(bytes, 0, result, 0, readPosition);
        System.arraycopy(bytes, readPosition, bytes, 0, position - readPosition);
        this.position = position - readPosition;
        notify();
        return result;
    }

    private int getReadPosition() {
        if (this.model == UTF_8_TXT) {
            //找到最后一个字符所在位置
            return getLastUtf8CharIndex();
        }
        return this.position;
    }
    
    public int getLastUtf8CharIndex() {
        for (int i = 5; i >= 3; i--) {
            int index = position - 6 + i;
            if (index < 0) break;
            byte lastByte = bytes[index];
            if (lastByte >> i == -2 || lastByte >> i == -1) {
                return index;
            }
        }
        return position;
    }

    public void setError(Throwable e) {
        this.e = e;
    }
    
    private void check() {
        if (e != null) {
            throw MyException.getMyException(e);
        }
    }

    public boolean isReadFinish() {
        return isFinish && position == 0 && bb.position() == 0;
    }

    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }
}
