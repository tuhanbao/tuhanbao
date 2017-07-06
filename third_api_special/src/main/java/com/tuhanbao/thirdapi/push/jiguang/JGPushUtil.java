package com.tuhanbao.thirdapi.push.jiguang;

import com.google.gson.JsonObject;
import com.tuhanbao.base.util.log.LogManager;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JGPushUtil {

    private static JPushClient CLIENT = null;

    public static JPushClient getClient() {
        if (CLIENT == null) {
            CLIENT = new JPushClient(JGConfig.SECRET, JGConfig.APP_KEY);
            ClientConfig.getInstance().setMaxRetryTimes(JGConfig.MAX_TRY_TIME);
        }
        return CLIENT;
    }

    /**
     * 推送消息给客户端
     * 
     * @param payload
     *            PushPayload对象
     */
    private static void pushMsg(PushPayload payload) {
        try {
            PushResult result = getClient().sendPush(payload);
            LogManager.info("Got result - " + result);
        }
        catch (APIConnectionException e) {
            LogManager.error(e);
        }
        catch (APIRequestException e) {
            LogManager.error(e);
            LogManager.info("HTTP Status: " + e.getStatus());
            LogManager.info("Error Code: " + e.getErrorCode());
            LogManager.info("Error Message: " + e.getErrorMessage());
            LogManager.info("Msg ID: " + e.getMsgId());
        }
    }

    // public static void sendPush(String title,String content,
    // String registerId) {
    // // HttpProxy proxy = new HttpProxy("localhost", 3128);
    // // Can use this https proxy: https://github.com/Exa-Networks/exaproxy
    //
    // // For push, all you need do is to build PushPayload object.
    // // 生成推送的内容，这里我们先测试全部推送
    //// SMS msg = SMS.content(content, 0);
    //// PushPayload payload = buildPushObject_all_all_message(content);
    //// PushPayload payload = buildPushObject_android_and_ios();
    // PushPayload payload =
    // buildPushObject_audienceOne(title,content,registerId);
    //
    // try {
    // PushResult result = jpushClient.sendPush(payload);
    // LogManager.info("Got result - " + result);
    // } catch (APIConnectionException e) {
    // LogManager.error("Connection error. Should retry later. ", e);
    // } catch (APIRequestException e) {
    // LogManager.error(
    // "Error response from JPush server. Should review and fix it. ",
    // e);
    // LogManager.info("HTTP Status: " + e.getStatus());
    // LogManager.info("Error Code: " + e.getErrorCode());
    // LogManager.info("Error Message: " + e.getErrorMessage());
    // LogManager.info("Msg ID: " + e.getMsgId());
    // }
    // }

    /**
     * 推送给特定用户
     * 
     * @param title
     *            标题
     * @param content
     *            主体
     * @param registerId
     */
    public static void pushAlertToJGUser(String title, String content, String registerId) {
        PushPayload payload = buildPushObject_audienceOne(title, content, registerId);
        pushMsg(payload);
    }

    /**
     * 推送弹窗消息给所有用户
     * 
     * @param alert
     * @return
     */
    public static void pushObject_all_alert(String alert) {
        pushMsg(PushPayload.alertAll(alert));
    }

    /**
     * 推送自定义消息给所有用户
     * 
     * @param msg
     * @return
     */
    public static void pushObject_all_message(String msg) {
        pushMsg(PushPayload.messageAll(msg));
    }

    /**
     * 推送自定义消息给指定用户
     * 
     * @param msg
     * @param registerId
     */
    public static void pushMessageToJGUser(String msgContent, String registerId) {
        pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.all()).setAudience(Audience.registrationId(registerId))
                .setMessage(Message.content(msgContent)).build());
    }

    /**
     * 推送弹窗消息给所有用户
     * 
     * @param title
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alert(String title) {
        return PushPayload.newBuilder().setPlatform(Platform.all())// 设置接受的平台
                .setAudience(Audience.all())// Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(Notification.alert(title)).build();
    }

    /**
     * 推送给特定用户
     * 
     * @param title
     * @param content
     * @param registerId
     * @return
     */
    public static PushPayload buildPushObject_audienceOne(String title, String content, String registerId) {
        return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(registerId))
                .setMessage(Message.newBuilder().setMsgContent(content).setTitle(title).build()).setNotification(Notification.alert(title)).build();
    }

    /**
     * 推送给Android用户
     * 
     * @param alert
     * @param title
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle(String alert, String title) {
        return PushPayload.newBuilder().setPlatform(Platform.android()).setAudience(Audience.all())
                .setNotification(Notification.android(alert, title, null)).build();
    }

    public static PushPayload buildPushObject_android_and_ios() {
        return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.all())
                .setNotification(Notification.newBuilder().setAlert("alert content")
                        .addPlatformNotification(AndroidNotification.newBuilder().setTitle("Android Title").build())
                        .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtra("extra_key", "extra_value").build()).build())
                .build();
    }

    /**
     * 给平台所用用户推送自定义消息：包含Json
     * 
     * @param alert
     *            标题
     * @param content
     *            主体
     * @param key
     *            Key值
     * @param value
     *            Json对象
     * @return
     */
    public static void buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String alert, String content, String key, JsonObject value) {
        pushMsg(PushPayload.newBuilder().setPlatform(Platform.ios()).setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(
                                IosNotification.newBuilder().setAlert(alert).setBadge(5).setSound("happy").addExtra(key, value).build())
                        .build())
                .setMessage(Message.content(content)).setOptions(Options.newBuilder().setApnsProduction(true).build()).build());
    }

    /**
     * 给平台所用用户推送自定义消息：包含key-value值的String字符串
     * 
     * @param alert
     *            标题
     * @param content
     *            主体
     * @param key
     *            Key值
     * @param value
     *            String字符串
     * @return
     */
    public static void buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String alert, String content, String key, String value) {
        pushMsg(PushPayload.newBuilder().setPlatform(Platform.ios()).setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(
                                IosNotification.newBuilder().setAlert(alert).setBadge(5).setSound("happy").addExtra(key, value).build())
                        .build())
                .setMessage(Message.content(content)).setOptions(Options.newBuilder().setApnsProduction(true).build()).build());
    }

    // public static PushPayload
    // buildPushObject_ios_audienceMore_messageWithExtras(
    // String content) {
    // return PushPayload
    // .newBuilder()
    // .setPlatform(Platform.android_ios())
    // .setAudience(
    // Audience.newBuilder()
    // .addAudienceTarget(
    // AudienceTarget.tag("tag1", "tag2"))
    // .addAudienceTarget(
    // AudienceTarget
    // .alias("alias1", "alias2"))
    // .build())
    // .setMessage(
    // Message.newBuilder().setMsgContent(content)
    // .addExtra("from", "JPush").build()).build();
    // }
}
