package com.tuhanbao.thirdapi.aliyun.oss;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.TimeUtil;
import com.tuhanbao.base.util.thread.TimerEvent;
import com.tuhanbao.base.util.thread.TimerThreadFactory;

public class OSSManager extends TimerEvent {
    private Map<String, OSSObject> OSS_MAP = new HashMap<String, OSSObject>();

    public static OSSManager INSTANCE = new OSSManager();

    private OSSManager() {
    }

    protected void run() throws Exception {
        refresh(0);
    }

    private void refresh(final int num) {
        if (num > 3) LogManager.error(new MyException(BaseErrorCode.ERROR, "connect oss error!!!!"));

        try {
            // 不准清空OSS_MAP
            OSSClient client = new OSSClient(OSSConfig.ENDPOINT, OSSConfig.ACCESS_KEY, OSSConfig.ACCESS_KEY_SECRET);
            Date expiration = new Date(System.currentTimeMillis() + OSSConfig.EXPIRATION_TIME * TimeUtil.MIN2MILL);

            ObjectListing list = client.listObjects(OSSConfig.BUCKET_NAME);
            for (OSSObjectSummary obj : list.getObjectSummaries()) {
                String key = obj.getKey();

                if (isFile(key)) {
                    GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(OSSConfig.BUCKET_NAME, key, HttpMethod.GET);
                    // 设置过期时间
                    request.setExpiration(expiration);
                    // 生成URL签名(HTTP GET请求)
                    URL signedUrl = client.generatePresignedUrl(request);
                    String fileKey = getKey(key);
                    OSS_MAP.put(fileKey, new OSSObject(key, signedUrl, obj.getSize()));
                }
            }
            client.shutdown();
        }
        catch (Exception e) {
            TimerThreadFactory.addTimerEvent(new TimerEvent(1) {
                @Override
                protected void run() throws Exception {
                    final int nextNum = num + 1;
                    refresh(nextNum);
                }
            }, 10, TimeUnit.MINUTES);
        }
    }

    private static boolean isFile(String key) {
        return key.startsWith(OSSConfig.ROOT_DIC) && !key.endsWith("/");
    }

    private static String getKey(String key) {
        if (!key.startsWith(OSSConfig.ROOT_DIC)) return Constants.EMPTY;
        return key.substring(OSSConfig.ROOT_DIC.length());
    }

    public static OSSObject getOssObject(String ossKey) {
        OSSObject result = INSTANCE.OSS_MAP.get(ossKey);

        // 可能是临时更新的，直接去获取一次
        if (result == null) {
            OSSClient client = new OSSClient(OSSConfig.ENDPOINT, OSSConfig.ACCESS_KEY, OSSConfig.ACCESS_KEY_SECRET);
            Date expiration = new Date(System.currentTimeMillis() + OSSConfig.EXPIRATION_TIME * TimeUtil.MIN2MILL);

            ObjectListing list = client.listObjects(OSSConfig.BUCKET_NAME);
            for (OSSObjectSummary obj : list.getObjectSummaries()) {
                String key = obj.getKey();
                String fileKey = getKey(key);

                if (isFile(key) && fileKey.equals(ossKey)) {
                    GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(OSSConfig.BUCKET_NAME, key, HttpMethod.GET);
                    // 设置过期时间
                    request.setExpiration(expiration);
                    // 生成URL签名(HTTP GET请求)
                    URL signedUrl = client.generatePresignedUrl(request);
                    OSSObject value = new OSSObject(key, signedUrl, obj.getSize());
                    INSTANCE.OSS_MAP.put(fileKey, value);
                    return value;
                }
            }
            client.shutdown();
        }

        return result;
    }

    public static URL getUrl(String key) {
        if (INSTANCE.OSS_MAP.containsKey(key)) {
            return INSTANCE.OSS_MAP.get(key).getUrl();
        }
        throw new MyException(BaseErrorCode.ERROR, "oss object is not exits : " + key);
    }
}
