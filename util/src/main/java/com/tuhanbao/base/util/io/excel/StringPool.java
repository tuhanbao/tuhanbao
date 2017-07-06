package com.tuhanbao.base.util.io.excel;

import com.tuhanbao.base.util.exception.MyException;

public class StringPool {
    
    private String[] data;
    
    private int position = 0;
    
    private int size;
    
    private static final int DEFAUL_SIZE = 1000;
    
    private Throwable e;
    
    private boolean isFinish = false;
    
    public StringPool() {
        this(DEFAUL_SIZE);
    }
    
    public StringPool(int size) {
        this.size = size;
        data = new String[this.size];
    }
    
//    public static void main(String args[]) {
//        final StringPool sp = new StringPool(10);
//        
//        new Thread(new Runnable() {
//            
//            @Override
//            public void run() {
//                int i = 0;
//                while(true) {
//                    try {
//                        sp.add(i++ + "");
//                        Thread.sleep(100);
//                    }
//                    catch (InterruptedException e) {
//                    }
//                }
//            }
//        }).start();
//        
//        new Thread(new Runnable() {
//            
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        for (String s : sp.get()) {
//                            System.out.println(s);
//                        }
//                    }
//                    catch (InterruptedException e) {
//                    }
//                }
//            }
//        }).start();
//    }

    public synchronized void add(String[] array, int startIndex, int length) throws InterruptedException {
        check();
        
        while (position == size) {
            this.wait();
        }
        
        int maxInsertLength = size - position;
        
        if (length < maxInsertLength) {
            System.arraycopy(array, startIndex, data, position, length);
            position += length;
        }
        else {
            System.arraycopy(array, startIndex, data, position, maxInsertLength);
            position += maxInsertLength;
            this.add(array, startIndex + maxInsertLength, length - maxInsertLength);
        }
        
        if (this.isReadFinish() || position > size / 2) {
            //避免频繁唤醒
            this.notify();
        }
    }

    public synchronized void add(String s) throws InterruptedException {
        check();
        while (position == size) {
            this.wait();
        }
        
        this.data[position++] = s;
        if (this.isReadFinish() || position > size / 2) {
            //避免频繁唤醒
            this.notify();
        }
    }
    
    /**
     * 可以自己判断readFinish，或者根据返回结果是否为null判断是否已经读完
     * 
     * @return
     * @throws InterruptedException
     */
    public synchronized String[] get() throws InterruptedException {
        check();
        if (isReadFinish()) return null;
        while (position == 0) {
            this.wait();
        }
        String[] result = new String[position];
        System.arraycopy(data, 0, result, 0, position);
        this.position = 0;
        this.notify();
        return result;
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
        return isFinish && position == 0;
    }

    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }
}
