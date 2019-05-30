package com.easy.rabbitmq;

import com.google.common.base.Preconditions;
import com.easy.rabbitmq.converters.TypeOfStringMessageConverter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ApplicationObjectSupport;

import java.util.Collection;
import java.util.LinkedList;

/**
 * RabbitMQ 配置启动类
 * {@link RabbitAutoConfiguration Auto-configuration} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/11/30 15:42
 */
@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitAutoConfiguration extends ApplicationObjectSupport
        implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    public static volatile boolean startup = false;

    private final RabbitProperties rabbitProperties;

    public RabbitAutoConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        logger.info("The contents of the bean name with RabbitProperties: " + rabbitProperties.toString());

        Preconditions.checkNotNull(rabbitProperties.getAddress(), "sorry rabbitmq address can not be null.");
        Preconditions.checkNotNull(rabbitProperties.getPort(), "sorry rabbitmq port can not be null.");
        Preconditions.checkNotNull(rabbitProperties.getUsername(), "sorry rabbitmq username can not be null.");
        Preconditions.checkNotNull(rabbitProperties.getPassword(), "sorry rabbitmq password can not be null.");

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses(rabbitProperties.getAddress());
        cachingConnectionFactory.setPort(rabbitProperties.getPort());
        cachingConnectionFactory.setUsername(rabbitProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitProperties.getPassword());
        cachingConnectionFactory.setVirtualHost(rabbitProperties.getVirtualHost() == null ? "/" : rabbitProperties.getVirtualHost());
        cachingConnectionFactory.setConnectionTimeout(rabbitProperties.getConnectionTimeout());
        cachingConnectionFactory.setPublisherReturns(true);
        cachingConnectionFactory.setPublisherConfirms(true);

        Collection<RabbitReceiveContainer> rabbitReceiveContainers = new LinkedList<RabbitReceiveContainer>(
                getApplicationContext().getBeansOfType(RabbitReceiveContainer.class).values());

        rabbitReceiveContainers.stream().forEach((container) -> {
            container.setConnectionFactory(cachingConnectionFactory);
        });

        logger.info("RabbitMQ connection factory already infused into the container!");
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.setApplicationContext(getApplicationContext());
        rabbitAdmin.initialize();

        logger.info("RabbitMQ rabbit admin already infused into the container!");
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(new TypeOfStringMessageConverter());
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnCallback(this::returnedMessage);
        rabbitTemplate.setMandatory(true);
        logger.info("RabbitMQ rabbit template already infused into the container!");
        return rabbitTemplate;
    }

    @Bean
    public RabbitSender rabbitSender(RabbitTemplate rabbitTemplate, ConnectionFactory connectionFactory) {
        return new RabbitSender(rabbitTemplate, connectionFactory);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            logger.info("[Confirm模式]消息发送成功，已成功投递到Broker");
        } else {
            logger.error("[Confirm模式]发送失败，驳回原因：CorrelationData：" + correlationData + "，" + "ack：" + ack + "，" + "cause：" + cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.error("[Return模式]发送失败，驳回原因：message：" + message + "，" + "replyCode：" + replyCode + "，" + "replyText：" + replyText + "，" + "exchange：" + exchange + "，" + "routingKey" + routingKey);
    }
}
