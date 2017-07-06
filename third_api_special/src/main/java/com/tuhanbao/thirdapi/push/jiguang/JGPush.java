package com.tuhanbao.thirdapi.push.jiguang;

import java.util.List;
import java.util.Map;

import com.tuhanbao.base.util.log.LogManager;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 
 * @author WANGSHUO
 * 
 * （1）方法名：pushMsgToAudinceByTags 推送消息给指定类型用户 
 *  参数：String title 消息标题
 *      String msgContent 消息具体内容 
 *      String contentType 消息类型
 *      Map<String, String> extras 额外添加的内容
 *      String... tags 指定的用户类型
 *
 * （2）方法名：pushMsgToAudinceByRegisterIds 推送消息给指定Ids用户
 * 	 参数：String title 消息标题 
 *  	String msgContent 消息具体内容 
 *  	String contentType 消息类型
 * 	 	Map<String,String> extras 额外添加的内容 
 *  	List<String> registerIds 指定的用户Ids
 *
 * （3）方法名：pushAlertToAudinceByTags 推送弹框消息给指定用户 
 *  参数： Object alert 推送弹窗显示信息
 *      String... tags 指定的用户类型
 *
 * （4）方法名：pushAlertToAudinceByRegisterIds 推送弹框消息给指定Ids用户
 *  参数：Object alert 推送弹窗显示信息 
 *  	List<String> registerIds 指定的用户Ids
 * 
 * （5）方法名：pushMsgAndAlertToAudinceByTags 推送消息和弹框消息给指定类型用户 
 * 	参数： String title 消息标题 
 * 		String msgContent 消息具体内容 
 * 		String contentType 消息类型
 *      Map<String, String> extras 额外添加的内容
 *      Object alert 推送弹窗显示信息 String...tags 指定的用户类型
 *
 * （6）方法名： pushMsgAndAlertToAudinceByRegisterIds 推送消息和弹框消息给指定Ids用户
 *  参数：String title 消息标题
 *  	String msgContent 消息具体内容 
 *  	String contentType 消息类型
 *      Map<String, String> extras 额外添加的内容
 *      Object alert 推送弹窗显示信息
 *      List<String> registerIds 指定的用户Ids
 *
 * （7）方法名：pushMsgToAllAudince 推送消息给所有用户
 *  参数：String title 消息标题 String
 *         msgContent 消息具体内容
 *         String contentType 消息类型 
 *         Map<String, String> extras 额外添加的内容
 * 
 * （8）方法名：pushAlertToAllAudince 推送弹框消息给所有用户
 *  参数： Object alert 推送弹窗显示信息
 * 
 * （9）方法名：pushMsgAndAlertToAllAudince 推送消息和弹框消息给所有用户 
 * 参数：String title 消息标题
 *     String msgContent 消息具体内容 
 *     String contentType 消息类型
 *     Map<String, String> extras 额外添加的内容 
 *     Object alert 推送弹窗显示信息
 *     String... tags 指定的用户类型
 */
public class JGPush {
	private static JPushClient CLIENT = null;

	public static JPushClient getClient() {
		if (CLIENT == null) {
			CLIENT = new JPushClient(JGConfig.SECRET, JGConfig.APP_KEY);
			ClientConfig.getInstance().setMaxRetryTimes(JGConfig.MAX_TRY_TIME);
		}
		return CLIENT;
	}

	/**
	 * 推送消息
	 * 
	 * @param payload
	 *            PushPayload对象
	 */
	private static void pushMsg(PushPayload payload) {
		try {
			PushResult result = getClient().sendPush(payload);
			LogManager.info("Got result - " + result);
		} catch (APIConnectionException e) {
			LogManager.error(e);
		} catch (APIRequestException e) {
			LogManager.error(e);
			LogManager.info("HTTP Status: " + e.getStatus());
			LogManager.info("Error Code: " + e.getErrorCode());
			LogManager.info("Error Message: " + e.getErrorMessage());
			LogManager.info("Msg ID: " + e.getMsgId());
		}
	}

	public void pushMsgToAudinceByTags(String msgContent, String... tags) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.tag(tags))
				.setMessage(Message.newBuilder().setMsgContent(msgContent).build()).build());
	}

	public void pushMsgToAudinceByTags(String title, String msgContent, String... tags) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.tag(tags))
				.setMessage(Message.newBuilder().setMsgContent(msgContent).setTitle(title).build()).build());
	}

	public void pushMsgToAudinceByTags(String title, String msgContent, String contentType, String... tags) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.tag(tags)).setMessage(
				Message.newBuilder().setContentType(contentType).setMsgContent(msgContent).setTitle(title).build())
				.build());
	}

	public void pushMsgToAudinceByTags(String title, String msgContent, String contentType, Map<String, String> extras,
			String... tags) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.tag(tags))
				.setMessage(Message.newBuilder().setContentType(contentType).setMsgContent(msgContent).setTitle(title)
						.addExtras(extras).build())
				.build());
	}

	public void pushMsgToAudinceByRegisterIds(String msgContent, List<String> registerIds) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.registrationId(registerIds))
				.setMessage(Message.newBuilder().setMsgContent(msgContent).build()).build());
	}

	public void pushMsgToAudinceByRegisterIds(String title, String msgContent, List<String> registerIds) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.registrationId(registerIds))
				.setMessage(Message.newBuilder().setMsgContent(msgContent).setTitle(title).build()).build());
	}

	public void pushMsgToAudinceByRegisterIds(String title, String msgContent, String contentType,
			List<String> registerIds) {
		pushMsg(new Builder()
				.setPlatform(Platform.all()).setAudience(Audience.registrationId(registerIds)).setMessage(Message
						.newBuilder().setContentType(contentType).setMsgContent(msgContent).setTitle(title).build())
				.build());
	}

	public void pushMsgToAudinceByRegisterIds(String title, String msgContent, String contentType,
			Map<String, String> extras, List<String> registerIds) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.registrationId(registerIds))
				.setMessage(Message.newBuilder().setContentType(contentType).setMsgContent(msgContent).setTitle(title)
						.addExtras(extras).build())
				.build());
	}

	public void pushAlertToAudinceByTags(Object alert, String... tags) {
		pushMsg(new Builder().setPlatform(Platform.android_ios()).setAudience(Audience.tag(tags))
				.setNotification(Notification.newBuilder().setAlert(alert).build()).build());
	}

	public void pushAlertToAudinceByRegisterIds(Object alert, List<String> registerIds) {
		pushMsg(new Builder().setPlatform(Platform.android_ios()).setAudience(Audience.registrationId(registerIds))
				.setNotification(Notification.newBuilder().setAlert(alert).build()).build());
	}

	public void pushMsgAndAlertToAudinceByRegisterIds(String title, String msgContent, String contentType,
			Map<String, String> extras, Object alert, List<String> registerIds) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(registerIds))
				.setMessage(Message.newBuilder().setContentType(contentType).setMsgContent(msgContent).setTitle(title)
						.addExtras(extras).build())
				.setNotification(Notification.alert(alert)).build());
	}

	public void pushMsgAndAlertToAudinceByRegisterIds(String title, String msgContent, String contentType,
			Map<String, String> extras, List<String> registerIds) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(registerIds))
				.setMessage(Message.newBuilder().setContentType(contentType).setMsgContent(msgContent).setTitle(title)
						.addExtras(extras).build())
				.setNotification(Notification.alert(title)).build());
	}

	public void pushMsgAndAlertToAudinceByRegisterIds(String title, String msgContent, String contentType,
			List<String> registerIds) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.registrationId(registerIds)).setMessage(Message.newBuilder()
						.setContentType(contentType).setMsgContent(msgContent).setTitle(title).build())
				.setNotification(Notification.alert(title)).build());
	}

	public void pushMsgAndAlertToAudinceByRegisterIds(String title, String msgContent, List<String> registerIds) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(registerIds))
				.setMessage(Message.newBuilder().setMsgContent(msgContent).setTitle(title).build())
				.setNotification(Notification.alert(title)).build());
	}

	public void pushMsgAndAlertToAudinceByTags(String title, String msgContent, String contentType,
			Map<String, String> extras, Object alert, String... tags) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.tag(tags)).setMessage(Message.newBuilder().setContentType(contentType)
						.setMsgContent(msgContent).setTitle(title).addExtras(extras).build())
				.setNotification(Notification.alert(alert)).build());
	}

	public void pushMsgAndAlertToAudinceByTags(String title, String msgContent, String contentType,
			Map<String, String> extras, String... tags) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.tag(tags)).setMessage(Message.newBuilder().setContentType(contentType)
						.setMsgContent(msgContent).setTitle(title).addExtras(extras).build())
				.setNotification(Notification.alert(title)).build());
	}

	public void pushMsgAndAlertToAudinceByTags(String title, String msgContent, String contentType, String... tags) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.tag(tags)).setMessage(Message.newBuilder().setContentType(contentType)
						.setMsgContent(msgContent).setTitle(title).build())
				.setNotification(Notification.alert(title)).build());
	}

	public void pushMsgAndAlertToAudinceByTags(String title, String msgContent, String... tags) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.tag(tags))
				.setMessage(Message.newBuilder().setMsgContent(msgContent).setTitle(title).build())
				.setNotification(Notification.alert(title)).build());
	}

	public void pushMsgToAllAudince(String title, String msgContent, String contentType, Map<String, String> extras) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.all()).setMessage(Message.newBuilder()
				.setContentType(contentType).setMsgContent(msgContent).setTitle(title).addExtras(extras).build())
				.build());
	}

	public void pushMsgToAllAudince(String title, String msgContent, String contentType) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.all()).setMessage(
				Message.newBuilder().setContentType(contentType).setMsgContent(msgContent).setTitle(title).build())
				.build());
	}

	public void pushMsgToAllAudince(String title, String msgContent) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.all())
				.setMessage(Message.newBuilder().setMsgContent(msgContent).setTitle(title).build()).build());
	}

	public void pushMsgToAllAudince(String msgContent) {
		pushMsg(new Builder().setPlatform(Platform.all()).setAudience(Audience.all())
				.setMessage(Message.newBuilder().setMsgContent(msgContent).build()).build());
	}

	public void pushAlertToAllAudince(Object alert) {
		pushMsg(new Builder().setPlatform(Platform.android_ios()).setAudience(Audience.all())
				.setNotification(Notification.newBuilder().setAlert(alert).build()).build());
	}

	public void pushMsgAndAlertToAllAudince(String title, String msgContent, String contentType,
			Map<String, String> extras, Object alert) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.all()).setMessage(Message.newBuilder().setContentType(contentType)
						.setMsgContent(msgContent).setTitle(title).addExtras(extras).build())
				.setNotification(Notification.alert(alert)).build());
	}

	public void pushMsgAndAlertToAllAudince(String title, String msgContent, String contentType,
			Map<String, String> extras) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.all()).setMessage(Message.newBuilder().setContentType(contentType)
						.setMsgContent(msgContent).setTitle(title).addExtras(extras).build())
				.setNotification(Notification.alert(title)).build());
	}

	public void pushMsgAndAlertToAllAudince(String title, String msgContent, String contentType) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.all()).setMessage(Message.newBuilder().setContentType(contentType)
						.setMsgContent(msgContent).setTitle(title).build())
				.setNotification(Notification.alert(title)).build());
	}

	public void pushMsgAndAlertToAllAudince(String title, String msgContent) {
		pushMsg(PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.all())
				.setMessage(Message.newBuilder().setMsgContent(msgContent).setTitle(title).build())
				.setNotification(Notification.alert(title)).build());
	}

}
