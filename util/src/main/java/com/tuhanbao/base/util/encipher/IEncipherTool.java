package com.tuhanbao.base.util.encipher;

public interface IEncipherTool
{
    public byte[] encrypt(byte[] bytes);
    
    public byte[] decrypt(byte[] bytes);
}
