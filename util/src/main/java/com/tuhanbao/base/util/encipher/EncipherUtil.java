package com.tuhanbao.base.util.encipher;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.util.config.BaseConfigConstants;
import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.objutil.Base64Util;
import com.tuhanbao.base.util.objutil.ByteUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 用来进行通用性的加解密
 * 需要个性化的请使用加解密类本身
 * 
 * @author tuhanbao
 *
 */
public class EncipherUtil 
{
    public static IEncipherTool TCP_TOOL = null;
    public static IEncipherTool HTTP_TOOL = null;
    
    
    private static Map<EncipherType, IEncipherTool> TOOLS = new HashMap<EncipherType, IEncipherTool>();
    
    public static final String DEFAULT_PASSWORD = "tuhanbao";
    
    static
    {
        Config baseConfig = ConfigManager.getBaseConfig();
        String password = null;
        String httpEnclipherType = null;
        String tcpEnclipherType = null;
        if (baseConfig != null) {
            password = baseConfig.getString(BaseConfigConstants.PASSWORD);
            //初始化加解密
            httpEnclipherType = baseConfig.getString(BaseConfigConstants.HTTP_ENCLIPHER);
            tcpEnclipherType = baseConfig.getString(BaseConfigConstants.TCP_ENCLIPHER);
        }
        
        if (!StringUtil.isEmpty(password)) {
            resetPassword(password);
        }
        
        if (!StringUtil.isEmpty(httpEnclipherType)) {
            HTTP_TOOL = getToolByType(EncipherType.valueOf(httpEnclipherType));
        }
        if (!StringUtil.isEmpty(tcpEnclipherType)) {
            TCP_TOOL = getToolByType(EncipherType.valueOf(tcpEnclipherType));
        }
    }
    
    /**
     * 此方法只允许在工程启动时预先调用
     * @param password
     */
    public static void resetPassword(String password) {
        if (StringUtil.isEmpty(password)) {
            password = DEFAULT_PASSWORD;
        }
        //密码之所以用byte，因为很可能密码本来就是加密了的，无法用String表示
        register(EncipherType.SELF, SelfEncipherTool.getInstance(password));
        register(EncipherType.DES, DesEncipherTool.getDesEncipherTool(password));
        register(EncipherType.AES, AESEncipherTool.getAESEncipherTool(password));
    }
    
    public static byte[] encrypt(EncipherType type, byte[] bytes) {
        IEncipherTool tool = getToolByType(type);
        if (tool == null) return bytes;
        else return tool.encrypt(bytes);
    }
    
    public static byte[] decrypt(EncipherType type, byte[] bytes) {
        IEncipherTool tool = getToolByType(type);
        if (tool == null) return bytes;
        else return tool.decrypt(bytes);
    }
    
    public static String encrypt(EncipherType type, String str) {
        return Base64Util.encode(encrypt(type, ByteUtil.string2Bytes(str)));
    }
    
    public static String decrypt(EncipherType type, String str) {
        return ByteUtil.bytes2String(decrypt(type, Base64Util.decode(str)));
    }
    
    public static byte[] encryptTcp(byte[] bytes)
    {
        if (TCP_TOOL == null) return bytes;
        else return TCP_TOOL.encrypt(bytes);
    }
    
    public static byte[] decryptTcp(byte[] bytes)
    {
        if (TCP_TOOL == null) return bytes;
        else return TCP_TOOL.decrypt(bytes);
    }
    
    public static byte[] encryptHttp(String str)
    {
        if (HTTP_TOOL == null) return ByteUtil.string2Bytes(str);
        else return HTTP_TOOL.encrypt(ByteUtil.string2Bytes(str));
    }
    
    public static String decryptHttp(String str)
    {
        if (HTTP_TOOL == null) return str;
        else return ByteUtil.bytes2String(HTTP_TOOL.decrypt(ByteUtil.string2Bytes(str)));
    }
    
    private static IEncipherTool getToolByType(EncipherType type)
    {
        return TOOLS.get(type);
    }
    
    public static void register(EncipherType type, IEncipherTool tool)
    {
        TOOLS.put(type, tool);
    }
}
