package com.tuhanbao.base.util.objutil;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;

public class ByteBuffer
{
    private static final int CAPACITY = 128;
    
    private static final int MAX_OFFSET = 256;

    private static final int MAX_DATA_LENGTH = 1024;

    private byte[] data;

    private int top;

    private int offset;

    public ByteBuffer()
    {
        this(CAPACITY);
    }

    public ByteBuffer(int capacity)
    {
        if (capacity < 0) throw new MyException(BaseErrorCode.ILLEGAL_ARGUMENT, "invalid capatity:" + capacity);
        this.data = new byte[capacity];
        this.top = 0;
        this.offset = 0;
    }

    public ByteBuffer(byte[] data)
    {
        if (data == null) data = new byte[0];
        this.data = data;
        this.top = data.length;
        this.offset = 0;
    }

    public ByteBuffer(byte[] data, int index, int length)
    {
        if (data == null) throw new MyException(BaseErrorCode.ILLEGAL_ARGUMENT, "null data");
        if ((index < 0) || (index > data.length)) throw new MyException(BaseErrorCode.ILLEGAL_ARGUMENT, "invalid index:" + index);
        if ((length < 0) || (data.length < index + length)) throw new MyException(BaseErrorCode.ILLEGAL_ARGUMENT, "invalid length:" + length);
        this.data = data;
        this.top = index + length;
        this.offset = index;
    }
    
    public ByteBuffer(String s)
    {
        this();
        this.putString(s);
    }

    public void put(byte[] b)
    {
        addCapacity(b.length);
        System.arraycopy(b, 0, this.data, top, b.length);
        this.top += b.length;
    }
    
    public void putString(String s)
    {
        if (s == null) return;
        byte[] b = ByteUtil.string2Bytes(s);
        
        putInt(b.length);
        put(b);
    }
    
    private void addCapacity()
    {
        addCapacity(CAPACITY);
    }
    
    /**
     * 增加容量
     * @param length
     */
    private void addCapacity(int length)
    {
        int oldLength = this.data.length;
        int newLength = oldLength;
        int dataLength = this.size();
        
        //如果有空间被浪费了，先进行清理
        if (this.offset >= MAX_OFFSET)
        {
            System.arraycopy(this.data, offset, this.data, 0, dataLength);
            this.offset = 0;
            this.top = dataLength;
        }
        
        //如果容量不够，需要扩展容量
        if (this.top + length > oldLength)
        {
            newLength = dataLength + (length << 1);
            byte[] temp = new byte[newLength];
            System.arraycopy(this.data, offset, temp, 0, dataLength);
            this.data = temp;
            this.offset = 0;
            this.top = dataLength;
        }
        //容量有剩余
        else if (newLength > MAX_DATA_LENGTH && newLength > (dataLength + length + 1) * 2)
        {
            newLength >>= 1;
            byte[] temp = new byte[newLength];
            System.arraycopy(this.data, offset, temp, 0, dataLength);
            this.data = temp;
            this.offset = 0;
            this.top = dataLength;
        }

    }
    
    public void putInt(int i)
    {
        addCapacity();
        this.data[top++] = ((byte)(i >> 24));
        this.data[top++] = ((byte)(i >> 16));
        this.data[top++] = ((byte)(i >> 8));
        this.data[top++] = ((byte)i);
    }
    
    public String readString()
    {
        if (size() == 0) return Constants.EMPTY;
        int length = this.readInt();
        if (length > this.size())
        {
            //还原
            this.offset -= 4;
            throw new MyException(BaseErrorCode.INDEX_OUTOF_BOUND, "invalid length : " + length);
        }
        
        return readString(length);
    }

    public String readString(int length)
    {
        if (length <= 0) return Constants.EMPTY;
        String s = ByteUtil.bytes2String(this.data, this.offset, length);
        if (length == this.size())
        {
            this.top = this.offset = 0;
        }
        else
        {
            this.offset += length;
        }
        return s;
    }
    
    public byte[] readBytes(int length)
    {
        if (length <= 0) return new byte[0];
        byte[] bytes = new byte[length];
        System.arraycopy(this.data, this.offset, bytes, 0, length);
        if (length == this.size())
        {
            this.top = this.offset = 0;
        }
        else
        {
            this.offset += length;
        }
        return bytes;
    }
    
    public int readInt()
    {
        byte b0 = this.data[offset++];
        byte b1 = this.data[offset++];
        byte b2 = this.data[offset++];
        byte b3 = this.data[offset++];
        return (((b3) << 24) | ((b2 & 0xff) << 16) | ((b1 & 0xff) <<  8) | ((b0 & 0xff)));
    }

    public int size()
    {
        return this.top - this.offset;
    }
    
    public byte[] getData()
    {
        byte[] bs = new byte[this.size()];
        System.arraycopy(this.data, offset, bs, 0, this.size());
        return bs;
    }
}
