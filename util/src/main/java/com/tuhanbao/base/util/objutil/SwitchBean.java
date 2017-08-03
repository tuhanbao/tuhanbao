package com.tuhanbao.base.util.objutil;

/**
 * 用long的二进制表示开关
 * 
 * toString方法返回char数组 一个long由4个char表示，方便数据库存储
 * 
 * @author Administrator
 *
 */
public class SwitchBean {
    private long[] values;

    public SwitchBean(int length) {
        values = new long[length];
    }

    public SwitchBean(String str) {
        char[] chars = str.toCharArray();
        int length = chars.length / 4;
        values = new long[length];
        for (int i = 0; i < length; i++) {
            values[i] = (chars[i * 4]) | ((long)chars[i * 4 + 1] << 16) | ((long)chars[i * 4 + 2] << 32) | ((long)chars[i * 4 + 3] << 48);
        }
    }

    public boolean getValue(int index) {
        return ((values[index / 64] >> (index % 64)) & 1) == 1;
    }

    public void setValue(int index, boolean value) {
        if (value) {
            values[index / 64] = ((values[index / 64]) | (1L << (index % 64)));
        }
        else {
            values[index / 64] = ((values[index / 64]) & ~(1L << (index % 64)));
        }
    }

    public String toString() {
        int length = values.length;
        char[] chars = new char[length * 4];
        for (int i = 0; i < length; i++) {
            chars[i * 4] = (char)(values[i]);
            chars[i * 4 + 1] = (char)(values[i] >> 16);
            chars[i * 4 + 2] = (char)(values[i] >> 32);
            chars[i * 4 + 3] = (char)(values[i] >> 48);
        }
        return new String(chars);
    }
}
