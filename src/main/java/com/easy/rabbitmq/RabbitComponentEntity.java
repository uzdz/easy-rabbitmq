package com.easy.rabbitmq;

/**
 * 交换机、队列、绑定实体类
 * {@link RabbitComponentEntity entity} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/3 13:42
 */
public class RabbitComponentEntity {

    /** 交换机名称*/
    private String exchangeName;

    /** 交换机类型 fanout:广播 direct:全匹配 topic:主题*/
    private String exchangeType;

    /** 队列名称*/
    private String queueName;

    /** 路由key*/
    private String routingKey;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    @Override
    public String toString() {
        return "RabbitComponentEntity{" +
                "exchangeName='" + exchangeName + '\'' +
                ", exchangeType='" + exchangeType + '\'' +
                ", queueName='" + queueName + '\'' +
                ", routingKey='" + routingKey + '\'' +
                '}';
    }
}
