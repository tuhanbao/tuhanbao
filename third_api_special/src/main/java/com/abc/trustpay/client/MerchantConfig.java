package com.abc.trustpay.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.tuhanbao.base.util.io.IOUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.thirdapi.pay.abc.ABCConfig;
import com.tuhanbao.thirdapi.pay.abc.ABCConstants;
import com.tuhanbao.thirdapi.pay.abc.MerchantInfo;

/**
 * 农行源码移植
 * 
 * 农行版本升级后需要更新
 * 
 * 此类不参与check style，格式检查等各项代码审查
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("restriction")
public class MerchantConfig {
    public static final String KEY_STORE_TYPE_FILE = "0";
    public static final String KEY_STORE_TYPE_SIGN_SERVER = "1";
    public static final String KEY_STORE_TYPE_OTHERS = "3";
    // private static String iKeyStoreType = "0";
    private static boolean iIsInitialed = false;
    // private static ResourceBundle iResourceBundle = null;
    private static List<String> iMerchantIDList = new ArrayList<String>();
    // private static List<String> iMerchantCertificateList = new
    // ArrayList<String>();
    // private static List<Object> iMerchantKeyList = new ArrayList<Object>();

    private static Map<Integer, MerchantInfo> MERCHANT_MAP = new HashMap<Integer, MerchantInfo>();

    // private static String iTrustPayConnectMethod = "http";
    // private static String iTrustPayServerName = "";
    // private static int iTrustPayServerPort = 0;
    // private static String iTrustPayTrxURL = "";
    // private static String iTrustPayIETrxURL = "";
    // private static String iMerchantErrorURL = "";
    // private static String iNewLine = "1";
    private static SSLSocketFactory iSSLSocketFactory = null;
    private static Certificate iTrustpayCertificate = null;
    private static String iLogPath = "";
    private static MerchantConfig uniqueInstanceOf_MerchantConfig = null;

    static {
        try {
            bundle();
        }
        catch (TrxException e) {
            LogManager.error(e);
        }
    }

    private MerchantConfig() throws TrxException {
    }

    /**
     * 单例
     * 
     * @throws TrxException
     */
    private static synchronized void syncInit() throws TrxException {
        if (uniqueInstanceOf_MerchantConfig == null) uniqueInstanceOf_MerchantConfig = new MerchantConfig();
    }

    public static MerchantConfig getUniqueInstance() throws TrxException {
        if (uniqueInstanceOf_MerchantConfig == null) {
            syncInit();
        }
        return uniqueInstanceOf_MerchantConfig;
    }

    /**
     * 读取配置文件
     * 
     * @throws TrxException
     */
    private static void bundle() throws TrxException {
        if (!iIsInitialed) {
            String tTrustPayCertFile = ABCConfig.getValue(ABCConstants.TRUSTPAY_CERTFILE);
            iTrustpayCertificate = getCertificate(tTrustPayCertFile);
            initSSL();
            String iMerchantIDS = ABCConfig.getValue(ABCConstants.MERCHANT_ID);
            if (iMerchantIDS.length() == 0) {
                throw new TrxException("1001", "商户端配置文件中参数设置错误 - 商户号[MerchantID]配置错误！");
            }
            StringTokenizer st = new StringTokenizer(iMerchantIDS, ",");
            while (st.hasMoreTokens()) {
                iMerchantIDList.add(st.nextToken());
            }
            String iKeyStoreType = ABCConfig.getValue(ABCConstants.MERCHANT_KEY_STORE_TYPE);
            if (iKeyStoreType.equals("0")) {
                bindMerchantCertificateByFile();
            }
            else if (!iKeyStoreType.equals("1")) {
                throw new TrxException("1001", "商户端配置文件中参数设置错误 - 证书储存媒体配置错误！");
            }
            iLogPath = ABCConfig.getValue(ABCConstants.LOG_PATH);
            iIsInitialed = true;
        }
    }

    private static void initSSL() throws TrxException {
        try {
            java.security.Provider tProvider = new com.sun.net.ssl.internal.ssl.Provider();
            SSLContext tSSLContext = SSLContext.getInstance("TLS", tProvider);
            TrustManagerFactory tTrustManagerFactory = TrustManagerFactory.getInstance("SunX509", tProvider);
            KeyStore tKeyStore = KeyStore.getInstance("JKS");
            String trustStoreFile = ABCConfig.getValue(ABCConstants.TRUST_STORE_FILE);
            String password = ABCConfig.getValue(ABCConstants.TRUST_STORE_PASSWORD);
            tKeyStore.load(new FileInputStream(trustStoreFile), password.toCharArray());
            tTrustManagerFactory.init(tKeyStore);
            TrustManager[] tTrustManager = tTrustManagerFactory.getTrustManagers();
            tSSLContext.init(null, tTrustManager, null);
            iSSLSocketFactory = tSSLContext.getSocketFactory();
        }
        catch (Exception e) {
            LogManager.debug("[Trustpay商户端API] - 初始 - 系统发生无法预期的错误" + e.getMessage());
            throw new TrxException("1999", "系统发生无法预期的错误", e.getMessage());
        }
        catch (Error e) {
            LogManager.debug("[Trustpay商户端API] - 初始 - 系统发生无法预期的错误" + e.getMessage());
            throw new TrxException("1999", "系统发生无法预期的错误", e.getMessage());
        }
        LogManager.debug("[Trustpay商户端API] - 初始 - SSLSocketFactory完成");
    }

    private static Certificate getCertificate(String tCertFile) throws TrxException {
        Certificate tCertificate = null;
        byte[] tCertBytes = new byte[4096];
        int tCertBytesLen = 0;
        FileInputStream tIn = null;
        try {
            tIn = new FileInputStream(tCertFile);
            tCertBytesLen = tIn.read(tCertBytes);
        }
        catch (Exception e) {
            throw new TrxException("1002", "无法读取证书文档");
        }
        finally {
            try {
                IOUtil.close(tIn);
            }
            catch (Exception localException1) {
            }
        }
        byte[] tFinalCertBytes = new byte[tCertBytesLen];
        for (int i = 0; i < tCertBytesLen; i++) {
            tFinalCertBytes[i] = tCertBytes[i];
        }
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        try {
            CertificateFactory tCertificateFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream bais = new ByteArrayInputStream(tFinalCertBytes);
            if (bais.available() > 0) {
                tCertificate = tCertificateFactory.generateCertificate(bais);
            }
        }
        catch (Exception e) {
            throw new TrxException("1006", "证书格式错误 - 无法由[" + tCertFile + "]生成X.509证书对象！" + e.getMessage());
        }
        return tCertificate;
    }

    public static String getParameterByName(String aParamName) throws TrxException {
        String tValue = ABCConfig.getValue(aParamName);
        if (StringUtil.isEmpty(tValue)) {
            throw new TrxException("1001", "商户端配置文件中参数设置错误", " - 未设定[" + aParamName + "]参数值!");
        }
        return tValue;
    }

    public static BufferedWriter getTrxLogFile() throws TrxException {
        return getTrxLogFile("TrxLog");
    }

    public static BufferedWriter getTrxLogFile(String aFileName) throws TrxException {
        bundle();
        BufferedWriter tLogFile = null;
        String tFileName = "";
        try {
            HiCalendar tHiCalendar = new HiCalendar();
            tFileName = iLogPath + System.getProperty("file.separator") + aFileName + tHiCalendar.toString(".%Y%m%d.log");
            tLogFile = new BufferedWriter(new FileWriter(tFileName, true));
        }
        catch (IOException e) {
            throw new TrxException("1004", "无法写入交易日志文档", " - 系统无法写入交易日志至[" + tFileName + "]中!");
        }
        return tLogFile;
    }

    private static void bindMerchantCertificateByFile() throws TrxException {
        String tMerchantCertFiles = ABCConfig.getValue(ABCConstants.MERCHANT_CERTFILE);
        if (tMerchantCertFiles.length() == 0) {
            throw new TrxException("1001", "商户端配置文件中参数设置错误", "商户证书储存目录档名[MerchantCertFile]配置错误！");
        }
        String tMerchantCertPasswords = ABCConfig.getValue(ABCConstants.MERCHANT_CERT_PASSWORD);
        if (tMerchantCertPasswords.length() == 0) {
            throw new TrxException("1001", "商户端配置文件中参数设置错误", "商户私钥加密密码[MerchantCertPassword]配置错误！");
        }
        StringTokenizer stCertFile = new StringTokenizer(tMerchantCertFiles, ",");
        StringTokenizer stCertPassword = new StringTokenizer(tMerchantCertPasswords, ",");
        if (stCertFile.countTokens() != stCertPassword.countTokens()) {
            throw new TrxException("1001", "商户端配置文件中参数设置错误", "商户证书储存目录档名[MerchantCertFile]或商户私钥加密密码[MerchantCertPassword]配置错误！");
        }
        ArrayList<String> tCertFileList = new ArrayList<String>();
        ArrayList<String> tCertPasswordList = new ArrayList<String>();
        while (stCertFile.hasMoreTokens()) {
            tCertFileList.add(stCertFile.nextToken());
        }
        while (stCertPassword.hasMoreTokens()) {
            tCertPasswordList.add(stCertPassword.nextToken());
        }
        int size = tCertFileList.size();
        String errMessage = "";
        for (int i = 0; i < size; i++) {
            String iMerchantID = (String)iMerchantIDList.get(i);
            String tMerchantCertFile = (String)tCertFileList.get(i);
            String tMerchantCertPassword = (String)tCertPasswordList.get(i);

            FileInputStream tin = null;
            try {
                tin = new FileInputStream(tMerchantCertFile);
                errMessage += initMerchantInfo(i + 1, iMerchantID, tin, tMerchantCertPassword);
            }
            catch (FileNotFoundException e) {
                String err = iMerchantID + " 无法读取证书文档" + "\r\n";
                LogManager.debug(err);
            }
            finally {
                if (tin != null) {
                    try {
                        tin.close();
                    }
                    catch (Exception localException2) {
                    }
                }
                tin = null;
            }
        }
        if (size == 1) {
            if (!errMessage.equals(""))/* 556 */ throw new TrxException("1999", "系统发生无法预期的错误", errMessage);
        }
    }

    public static String initMerchantInfo(int id, String iMerchantID, InputStream tIn, String tMerchantCertPassword) {
        boolean flag = false;
        KeyStore tKeyStore = null;
        String errMessage = "";
        try {
            tKeyStore = KeyStore.getInstance("PKCS12", new com.sun.net.ssl.internal.ssl.Provider().getName());
            tKeyStore.load(tIn, tMerchantCertPassword.toCharArray());
        }
        catch (Exception e) {
            flag = true;
            String err = iMerchantID + " 无法读取证书文档" + "\r\n";
            LogManager.debug(err);
            errMessage = err;
        }
        finally {
            if (tIn != null) {
                try {
                    tIn.close();
                }
                catch (Exception localException2) {
                }
            }
        }
        Certificate tCert = null;
        String tAliases = "";
        String iMerchantCertificate = "";

        try {
            Enumeration<?> e = tKeyStore.aliases();
            if (e.hasMoreElements()) {
                tAliases = (String)e.nextElement();
            }
            tCert = tKeyStore.getCertificate(tAliases);
            Base64 tBase64 = new Base64();
            iMerchantCertificate = tBase64.encode(tCert.getEncoded());
        }
        catch (Exception e) {
            if (!flag) {
                String err = iMerchantID + " 证书格式错误" + "\r\n";
                LogManager.debug(err);
                errMessage = errMessage + err;
                flag = true;
            }
        }

        try {
            X509Certificate tX509Cert = (X509Certificate)tCert;
            tX509Cert.checkValidity();
        }
        catch (Exception e) {
            if (!flag) {
                String err = iMerchantID + " 证书过期" + "\r\n";
                LogManager.debug(err);
                errMessage = errMessage + err;
                flag = true;
            }
        }
        PrivateKey iMerchantKey = null;
        try {
            iMerchantKey = (PrivateKey)tKeyStore.getKey(tAliases, tMerchantCertPassword.toCharArray());
        }
        catch (Exception e) {
            if (!flag) {
                String err = iMerchantID + " 无法读取商户私钥" + "\r\n";
                LogManager.debug(err);
                errMessage = errMessage + err;
            }
            return errMessage;
        }

        MERCHANT_MAP.put(id, new MerchantInfo(iMerchantID, iMerchantKey, iMerchantCertificate));
        return errMessage;
    }

    public String getMerchantID(int id) throws TrxException {
        return MERCHANT_MAP.get(id).getMerchantId();
    }

    public String getMerchantCertificate(int id) throws TrxException {
        MerchantInfo merchantInfo = MERCHANT_MAP.get(id);
        if (merchantInfo == null) return null;
        return merchantInfo.getMerchantCertificate();
    }

    public PrivateKey getMerchantKey(int id) throws TrxException {
        MerchantInfo merchantInfo = MERCHANT_MAP.get(id);
        if (merchantInfo == null) return null;
        return merchantInfo.getKey();
    }

    public String getTrustPayConnectMethod() throws TrxException {
        return ABCConfig.getValue(ABCConstants.TRUSTPAY_CONNECT_METHOD);
    }

    public String getKeyStoreType() throws TrxException {
        return ABCConfig.getValue(ABCConstants.MERCHANT_KEY_STORE_TYPE);
    }

    public String getTrustPayServerName() throws TrxException {
        return ABCConfig.getValue(ABCConstants.TRUSTPAY_SERVER_NAME);
    }

    public int getTrustPayServerPort() throws TrxException {
        return ABCConfig.getIntValue(ABCConstants.TRUSTPAY_SERVER_PORT);
    }

    public String getTrustPayNewLine() throws TrxException {
        String tNewLine = ABCConfig.getValue(ABCConstants.TRUSTPAY_NEWLINE);
        if (tNewLine.equals("1")) return "\n";
        else return "\r\n";
    }

    public String getTrustPayTrxURL() throws TrxException {
        return ABCConfig.getValue(ABCConstants.TRUSTPAY_TRXURL);
    }

    public String getTrustPayIETrxURL() throws TrxException {
        return ABCConfig.getValue(ABCConstants.TRUSTPAY_IETRXURL);
    }

    public String getMerchantErrorURL() throws TrxException {
        return ABCConfig.getValue(ABCConstants.MERCHANT_ERRORURL);
    }

    public SSLSocketFactory getSSLSocketFactory() throws TrxException {
        return iSSLSocketFactory;
    }

    public static Certificate getTrustpayCertificate() throws TrxException {
        bundle();
        return iTrustpayCertificate;
    }

    /**
     * 外部调用
     * 
     * @param i
     * @param aMessage
     * @return
     * @throws TrxException
     */
    public String signMessage(int i, String aMessage) throws TrxException {
        String tMessage = null;
        try {
            String tKeyStoreType = getKeyStoreType();
            if (tKeyStoreType.equalsIgnoreCase("0")) {
                tMessage = fileSignMessage(i, aMessage);
            }
            else if (tKeyStoreType.equalsIgnoreCase("1")) {
                tMessage = signServerSignMessage(aMessage);
            }
        }
        catch (TrxException e) {
            throw e;
        }
        catch (Exception e) {
            throw new TrxException("1102", "签名交易报文时发生错误 - " + e.getMessage());
        }
        return tMessage.toString();
    }

    private String fileSignMessage(int i, String aMessage) throws Exception {
        Signature tSignature = null;
        String tMessage = "";
        try {
            tSignature = Signature.getInstance("SHA1withRSA");
            tSignature.initSign(getMerchantKey(i));
            tSignature.update(aMessage.toString().getBytes("UTF-8"));
            byte[] tSigned = tSignature.sign();
            Base64 tBase64 = new Base64();
            String tSignedBase64 = tBase64.encode(tSigned);
            tMessage = "{\"Message\":" + aMessage + "," + "\"Signature-Algorithm\":" + "\"" + "SHA1withRSA" + "\"" + "," + "\"Signature\":" + "\""
                    + tSignedBase64 + "\"}";
        }
        catch (Exception e) {
            throw e;
        }
        return tMessage.toString();
    }

    private String signServerSignMessage(String aMessage) throws Exception {
        String tMessage = "";
        Socket tSocket = null;
        OutputStream tOut = null;
        BufferedReader tIn = null;
        String tSignServerIP = ABCConfig.getValue("SignServerIP");
        int tSignServerPort = ABCConfig.getIntValue("SignServerPort");
        String tSignServerPass = ABCConfig.getValue("SignServerPassword");
        try {
            tSocket = new Socket(InetAddress.getByName(tSignServerIP), tSignServerPort);
            Base64 tBase64 = new Base64();
            String tData = tBase64.encode(aMessage.toString().getBytes("UTF-8"));
            StringBuffer tSignRequest = new StringBuffer("")/* 751 */ .append("<SignReq>")/* 752 */ .append("<Password>").append(tSignServerPass)
                    .append("</Password>").append("<Signature-Algorithm>").append("SHA1withRSA").append("</Signature-Algorithm>").append("<Data>")
                    .append(tData).append("</Data>")/* 755 */ .append("</SignReq>\n");
            tOut = tSocket.getOutputStream();
            tOut.write(tSignRequest.toString().getBytes("iso8859-1"));
            tOut.flush();
            tIn = new BufferedReader(new InputStreamReader(tSocket.getInputStream()));
            String tLine = tIn.readLine();
            if (tLine == null)/* 764 */ throw new TrxException("1104", "签名服务器返回签名错误", "无响应");
            XMLDocument tSignedResponse = new XMLDocument(tLine);
            String tSignBase64 = "";
            if (tSignedResponse.getValueNoNull("RC").equals("0"))/* 768 */ tSignBase64 = tSignedResponse.getValueNoNull("Signature");
            else {
                throw new TrxException("1104", "签名服务器返回签名错误", "错误代码[" + tSignedResponse.getValueNoNull("RC") + "]");
            }
            tMessage = "{\"Message\":" + aMessage + "," + "\"Signature-Algorithm\":" + "\"" + "SHA1withRSA" + "\"" + "," + "\"Signature\":" + "\""
                    + tSignBase64 + "\"}";
        }
        catch (TrxException e) {
            throw e;
        }
        catch (Exception e) {
            throw new TrxException("1103", "无法连线签名服务器", e.getMessage());
        }
        finally {
            if (tIn != null) try {
                tIn.close();
            }
            catch (Exception localException1) {
            }
            if (tOut != null) try {
                tOut.close();
            }
            catch (Exception localException2) {
            }
            if (tSocket != null) try {
                tSocket.close();
            }
            catch (Exception localException3) {
            }
        }
        return tMessage.toString();
    }

    /**
     * 外部调用
     * 
     * @param json
     * @param aMessage
     * @throws TrxException
     */
    public static void verifySign(JSON json, String aMessage) throws TrxException {
        bundle();
        String tTrxResponse = json.GetKeyValue("Message");
        if (tTrxResponse == null) {
            throw new TrxException("1301", "网上支付平台的响应报文不完整", "无[Message]段！");
        }
        String tAlgorithm = json.GetKeyValue("Signature-Algorithm");
        if (tAlgorithm == null) {
            throw new TrxException("1301", "网上支付平台的响应报文不完整", "无[Signature-Algorithm]段！");
        }
        String tSignBase64 = json.GetKeyValue("Signature");
        if (tSignBase64 == null) {
            throw new TrxException("1301", "网上支付平台的响应报文不完整", "无[Signature]段！");
        }
        Base64 tBase64 = new Base64();
        byte[] tSign = tBase64.decode(tSignBase64.toString());
        try {
            Signature tSignature = Signature.getInstance(tAlgorithm.toString());
            tSignature.initVerify(getTrustpayCertificate());
            tSignature.update(tTrxResponse.toString().getBytes("gb2312"));
            if (!tSignature.verify(tSign))/* 826 */ throw new TrxException("1302", "网上支付平台的响应报文签名验证失败");
        }
        catch (TrxException e) {
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            throw new TrxException("1302", "网上支付平台的响应报文签名验证失败 - " + e.getMessage());
        }
    }

    /**
     * 外部调用
     * 
     * @param aMessage
     * @return
     * @throws TrxException
     */
    public XMLDocument verifySignXML(XMLDocument aMessage) throws TrxException {
        XMLDocument tTrxResponse = aMessage.getValue("Message");
        if (tTrxResponse == null) {
            throw new TrxException("1301", "网上支付平台的响应报文不完整", "无[Message]段！");
        }
        XMLDocument tAlgorithm = aMessage.getValue("Signature-Algorithm");
        if (tAlgorithm == null) {
            throw new TrxException("1301", "网上支付平台的响应报文不完整", "无[Signature-Algorithm]段！");
        }
        XMLDocument tSignBase64 = aMessage.getValue("Signature");
        if (tSignBase64 == null) {
            throw new TrxException("1301", "网上支付平台的响应报文不完整", "无[Signature]段！");
        }
        Base64 tBase64 = new Base64();
        byte[] tSign = tBase64.decode(tSignBase64.toString());
        try {
            Signature tSignature = Signature.getInstance(tAlgorithm.toString());
            tSignature.initVerify(getTrustpayCertificate());
            tSignature.update(tTrxResponse.toString().getBytes("gb2312"));
            boolean result = tSignature.verify(tSign);
            if (!result)/* 865 */ throw new TrxException("1302", "网上支付平台的响应报文签名验证失败");
        }
        catch (TrxException e) {
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            throw new TrxException("1302", "网上支付平台的响应报文签名验证失败 - " + e.getMessage());
        }
        return tTrxResponse;
    }

    public static void main(String[] argc) {
    }
}
