package com.easy.rabbitmq.sender;

/**
 * 顶层抽象发送类
 * {@link BaseSender sender} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/24 11:45
 */
public interface BaseSender {

    /**
     * 普通消息发送
     * @param exchangeName 交换机名称
     * @param routingKey 路由key
     * @param message 发送消息体
     */
    void send(String exchangeName, String routingKey, Object message);

    /**
     *
     * 发送广播消息
     * @param msg 消息实体
     * @return 是否成功
     */
    boolean sendMulticastMsg(String msg);

    /**
     * 向指定用户发送消息
     * @param userId 用户id
     * @param msg 消息实体
     * @return 是否成功
     */
    boolean sendMsgByUserId(String userId, String msg);

    /**
     * 向指定组织机构发送消息
     * @param organSign 机构标识
     * @param msg 消息实体
     * @return 是否成功
     */
    boolean sendMsgByOrganSign(String organSign, String msg);

    /**
     * 判断队列是否存在
     * @param queue 队列名
     * @return
     */
    boolean queueDeclarePassive(String queue);
}
