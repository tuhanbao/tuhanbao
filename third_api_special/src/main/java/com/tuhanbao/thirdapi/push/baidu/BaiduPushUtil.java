/**
 * 
 */
package com.tuhanbao.thirdapi.push.baidu;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;

/**
 * 2016年10月25日
 * 
 * @author liuhanhui
 */
public class BaiduPushUtil {

    private static BaiduPushClient pushClient;
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "description";
    // private static final String
    private static final String APIKEY = "ot76bpSUuhdv7KwLMGKnj8lq";
    private static final String SECRETKEY = "YB8YNdCA05hrSZXxuzyKok6BjSHAdxA9";

    static {
        init();
    }

    private static void init() {
        // PushKeyPair pushKeyPair = new PushKeyPair(BaiduPushConfig.APIKEY,
        // BaiduPushConfig.SECRETKEY);
        PushKeyPair pushKeyPair = new PushKeyPair(APIKEY, SECRETKEY);
        pushClient = new BaiduPushClient(pushKeyPair, BaiduPushConstants.CHANNEL_REST_URL);
        pushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });
    }

    public static void singlePush4Android(String channelId, String msgTitle, String msgContent) {
        String message = buildPushMessage(msgTitle, msgContent);
        PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest();
        request.addChannelId(channelId);
        request.addDeviceType(3);// 设置设备类型，deviceType => 1 for web, 2 for pc, 3
                                 // for android, 4 for ios, 5 for wp.
        request.addMessageType(1);// 设置消息类型,0表示透传消息,1表示通知,默认为0.
        request.addMessage(message);
        try {
            PushMsgToSingleDeviceResponse response = pushClient.pushMsgToSingleDevice(request);
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime());
        }
        catch (PushClientException e) {
            e.printStackTrace();
        }
        catch (PushServerException e) {
            e.printStackTrace();
        }
    }

    public static void listPush4Android(List<String> channelIds, String msgTitle, String msgContent) {
        for (String channelId : channelIds) {
            singlePush4Android(channelId, msgTitle, msgContent);
        }
    }

    public static void singlePush4Ios(String channelId, String msgTitle, String msgContent) {
        String message = buildPushMessage(msgTitle, msgContent);

        PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest();
        request.addChannelId(channelId);
        request.addDeviceType(4);// 设置设备类型，deviceType => 1 for web, 2 for pc, 3
                                 // for android, 4 for ios, 5 for wp.
        request.addMessageType(1);// 设置消息类型,0表示透传消息,1表示通知,默认为0.
        request.addMessage(message);
        try {
            PushMsgToSingleDeviceResponse response = pushClient.pushMsgToSingleDevice(request);
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime());
        }
        catch (PushClientException e) {
            e.printStackTrace();
        }
        catch (PushServerException e) {
            e.printStackTrace();
        }
    }

    public static void lishPush4Ios(List<String> channelIds, String msgTitle, String msgContent) {
        for (String channelId : channelIds) {
            singlePush4Ios(channelId, msgTitle, msgContent);
        }
    }

    public static void singlePush4All(String channelId, String msgTitle, String msgContent) {
        singlePush4Android(channelId, msgTitle, msgContent);
        singlePush4Ios(channelId, msgTitle, msgContent);
    }

    public static void listPush4All(List<String> channelIds, String msgTitle, String msgContent) {
        for (String channelId : channelIds) {
            singlePush4Android(channelId, msgTitle, msgContent);
            singlePush4Ios(channelId, msgTitle, msgContent);
        }
    }

    public static void allPush4Android(String msgTitle, String msgContent) {
        PushMsgToAllRequest request = new PushMsgToAllRequest();
        String message = buildPushMessage(msgTitle, msgContent);
        request.addMessage(message);
        request.addDeviceType(3);// 设置设备类型，deviceType => 1 for web, 2 for pc, 3
                                 // for android, 4 for ios, 5 for wp.
        // 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例70秒后推送
        // request.addExpires(System.currentTimeMillis() / 1000 + 70);
        PushMsgToAllResponse response;
        try {
            response = pushClient.pushMsgToAll(request);
            // Http请求返回值解析
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime() + ",timerId: " + response.getTimerId());
        }
        catch (PushClientException | PushServerException e) {
            e.printStackTrace();
        }
    }

    public static void allPush4Ios(String msgTitle, String msgContent) {
        PushMsgToAllRequest request = new PushMsgToAllRequest();
        String message = buildPushMessage(msgTitle, msgContent);
        request.addMessage(message);
        request.addDeviceType(4);// 设置设备类型，deviceType => 1 for web, 2 for pc, 3
                                 // for android, 4 for ios, 5 for wp.
        // 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例70秒后推送
        // request.addExpires(System.currentTimeMillis() / 1000 + 70);
        PushMsgToAllResponse response;
        try {
            response = pushClient.pushMsgToAll(request);
            // Http请求返回值解析
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime() + ",timerId: " + response.getTimerId());
        }
        catch (PushClientException | PushServerException e) {
            e.printStackTrace();
        }
    }

    private static String buildPushMessage(String msgTitle, String msgContent) {
        JSONObject notification = new JSONObject();
        notification.put(KEY_TITLE, msgTitle);
        notification.put(KEY_CONTENT, msgContent);
        return notification.toJSONString();
    }
}
