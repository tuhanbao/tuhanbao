package com.tuhanbao.base.util.encipher;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.tuhanbao.base.util.exception.MyException;

public class AESEncipherTool implements IEncipherTool
{
    private static final String ALG = "AES/ECB/PKCS5Padding";
    
    private static final String KEY = "AES";
    
    private SecretKeySpec key;
    
    private Cipher decodeCipher, encodeCipher;
    
    public static final int PASSWORD_LENGTH = 16;
    
	private AESEncipherTool(byte[] password)
    {
		key = new SecretKeySpec(password, KEY);
		try {
			decodeCipher = Cipher.getInstance(ALG);
			decodeCipher.init(Cipher.DECRYPT_MODE, key);
			encodeCipher = Cipher.getInstance(ALG);
			encodeCipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (Exception e) {
			throw new MyException(-1, "init aes encipher fail");
		}
    }
    
	/**
	 * 长度必须16位
	 * @param password
	 * @return
	 */
    public static AESEncipherTool getAESEncipherTool(String password) {

        //由于AES的特殊性，密码长度有要求
        if (password.length() < AESEncipherTool.PASSWORD_LENGTH) {
            password += Encipher.DEFAULT_PASSWORD;
        }
        byte[] aesBytes = new byte[AESEncipherTool.PASSWORD_LENGTH];
        System.arraycopy(password.getBytes(), 0, aesBytes, 0, AESEncipherTool.PASSWORD_LENGTH);
    	return new AESEncipherTool(aesBytes);
    }
    
//    public static void main(String args[]) {
//        String s = "1231289391";
//        getAESEncipherTool(Base64.decode("0ooVTNEDnswZH3tNw/4yEw==".getBytes())).encrypt(s.getBytes());
////        String newStr = new String(En);
////        System.out.println(newStr);
//    }
    
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
