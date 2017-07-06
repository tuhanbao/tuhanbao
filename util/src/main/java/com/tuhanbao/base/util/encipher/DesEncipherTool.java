package com.tuhanbao.base.util.encipher;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.tuhanbao.base.util.exception.MyException;

public class DesEncipherTool implements IEncipherTool
{
    private static final String ALG = "DES";
    
    private static final String KEY = "DES";
    
    private Cipher decodeCipher, encodeCipher;
    
	private DesEncipherTool(byte[] password)
    {
		SecureRandom random = new SecureRandom();
		try {
			DESKeySpec desKey = new DESKeySpec(password);
			//创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY);
			SecretKey securekey = keyFactory.generateSecret(desKey);
			decodeCipher = Cipher.getInstance(ALG);
			decodeCipher.init(Cipher.DECRYPT_MODE, securekey, random);
			encodeCipher = Cipher.getInstance(ALG);
			encodeCipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		} catch (Exception e) {
			throw new MyException(-1, "init aes encipher fail");
		}
    }
    
    public static DesEncipherTool getDesEncipherTool(String password) {
    	return new DesEncipherTool(password.getBytes());
    }
    
    @Override
    public byte[] decrypt(byte[] bytes)
    {
        try {
			return decodeCipher.doFinal(bytes);
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
    }

    @Override
    public byte[] encrypt(byte[] bytes)
    {
    	try {
			return encodeCipher.doFinal(bytes);
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
    }
}
