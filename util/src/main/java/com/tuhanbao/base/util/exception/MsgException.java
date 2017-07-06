package com.tuhanbao.base.util.exception;

public class MsgException extends RuntimeException
{
    private static final long serialVersionUID = -3357618182909466416L;

    private String msg;

    public MsgException(String msg)
    {
        this.msg = msg;
    }
    
    @Override
    public String toString()
    {
        return msg;
    }
    
    @Override
    public String getMessage()
    {
        return msg;
    }
}
