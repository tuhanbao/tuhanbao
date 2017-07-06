package com.tuhanbao.base.util.exception;

public class SocketException extends MyException
{
    private static final long serialVersionUID = 2955353884260501421L;
    
    private Throwable e;
    
    public SocketException(String msg)
    {
        this(new MsgException(msg));
    }
    
    public SocketException(Throwable e)
    {
        super(-1);
        this.e = e;
    }
    
    @Override
    public String toString()
    {
        return e.toString();
    }
}
