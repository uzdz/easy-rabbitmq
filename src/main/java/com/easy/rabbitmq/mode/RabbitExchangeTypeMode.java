package com.easy.rabbitmq.mode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交换机路由类型
 * {@link RabbitExchangeTypeMode mode} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/5 11:11
 */
public enum RabbitExchangeTypeMode {

    /**
     * 广播模式
     */
    FANOUT("fanout"),

    /**
     * 全匹配模式
     */
    DIRECT("direct"),

    /**
     * 主题模式
     */
    TOPIC("topic");

    private String type;

    public static List<String> getAllType() {
        RabbitExchangeTypeMode[] values = RabbitExchangeTypeMode.values();
        List<RabbitExchangeTypeMode> typeModes = Arrays.asList(values);
        return typeModes.stream().map(RabbitExchangeTypeMode::getType).collect(Collectors.toList());
    }

    RabbitExchangeTypeMode(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
