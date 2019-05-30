package com.easy.rabbitmq.mode;

/**
 * {@link RabbitRegistrarMode enum} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/4 18:03
 */
public enum RabbitRegistrarMode {

    /**
     * 生产端
     */
    PROVIDER,

    /**
     * 消费端
     */
    CONSUMER,

    /**
     * 两者聚合
     */
    BOTH
}
