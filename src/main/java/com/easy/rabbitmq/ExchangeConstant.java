package com.easy.rabbitmq;

/**
 * 交换机constant
 * {@link ExchangeConstant exchange} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/3 13:42
 */
public class ExchangeConstant {

	/**
	 * 消息推送 user
	 */
	public static final String MESSAGE_PUSH_USER = "message.push.user";
	
	/**
	 * 消息推送 组织机构
	 */
	public static final String MESSAGE_PUSH_ORGANIZATION = "message.push.organization";
	
	/**
	 * 消息推送 广播
	 */
	public static final String MESSAGE_PUSH_MULTICAST = "message.push.multicast";
}
