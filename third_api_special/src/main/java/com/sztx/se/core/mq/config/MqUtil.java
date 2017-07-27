package com.sztx.se.core.mq.config;

import com.tuhanbao.base.util.objutil.StringUtil;

public class MqUtil {

	public static boolean equalsMessage(String verifiedExchange, String namedExchange, String verifiedRoutingKey, String namedRoutingKey) {
		return equalsExchange(verifiedExchange, namedExchange) && equalsRoutingKey(verifiedRoutingKey, namedRoutingKey);
	}

	public static boolean equalsExchange(String verifiedExchange, String namedExchange) {
		boolean flag = false;

		if (!StringUtil.isEmpty(verifiedExchange) && !StringUtil.isEmpty(namedExchange)) {
			if (verifiedExchange.equals(namedExchange) || verifiedExchange.equals(namedExchange + MqConstants.MQ_PRE_FLAG)
					|| verifiedExchange.equals(namedExchange + MqConstants.MQ_GRAY_FLAG)) {
				flag = true;
			}
		}

		return flag;
	}

	public static boolean equalsRoutingKey(String verifiedRoutingKey, String namedRoutingKey) {
		boolean flag = false;

		if (!StringUtil.isEmpty(verifiedRoutingKey) && !StringUtil.isEmpty(namedRoutingKey)) {
			if (verifiedRoutingKey.equals(namedRoutingKey)) {
				flag = true;
			}
		}

		return flag;
	}
}
