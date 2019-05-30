package com.easy.rabbitmq.annotations;

import com.easy.rabbitmq.RabbitNoneConsumer;
import com.easy.rabbitmq.mode.RabbitExchangeTypeMode;
import com.easy.rabbitmq.mode.RabbitRegistrarMode;
import com.easy.rabbitmq.registrars.RabbitComponentRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 交换机、队列、绑定组件注册器
 * {@link RabbitRegistrar annotations} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/3 11:07
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Documented
@Configuration
@Import(RabbitComponentRegistrar.class)
public @interface RabbitRegistrar {

    /**
     * 交换机名称
     * @return
     */
    String exchangeName() default "";

    /**
     * 交换机类型
     * @return
     */
    RabbitExchangeTypeMode exchangeType() default RabbitExchangeTypeMode.DIRECT;

    /**
     * 队列名称
     * @return
     */
    String queueName() default "";

    /**
     * 交换机与队列的routingKey
     * @return
     */
    String routingKey() default "";

    /**
     * 消费端类
     * @return
     */
    Class<?> consumerClasses() default RabbitNoneConsumer.class;

    /**
     * 注册类型
     * @return
     */
    RabbitRegistrarMode registrarType() default RabbitRegistrarMode.BOTH;
}
