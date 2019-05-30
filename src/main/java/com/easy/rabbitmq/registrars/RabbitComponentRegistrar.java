package com.easy.rabbitmq.registrars;

import com.easy.rabbitmq.RabbitAutoConfiguration;
import com.easy.rabbitmq.RabbitBaseConsumer;
import com.easy.rabbitmq.RabbitComponentEntity;
import com.easy.rabbitmq.RabbitReceiveContainer;
import com.easy.rabbitmq.annotations.RabbitRegistrar;
import com.easy.rabbitmq.mode.RabbitExchangeTypeMode;
import com.easy.rabbitmq.mode.RabbitRegistrarMode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import java.util.*;

/**
 * 交换机、队列、绑定注册类
 * {@link RabbitComponentRegistrar registrars component} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/3 13:37
 */
public class RabbitComponentRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (RabbitAutoConfiguration.startup) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                    importingClassMetadata.getAnnotationAttributes(RabbitRegistrar.class.getName()));

            // 交换机类型
            RabbitExchangeTypeMode exchangeType = attributes.getEnum("exchangeType");

            // 注册器类型
            RabbitRegistrarMode registrarType = attributes.getEnum("registrarType");

            if (registrarType.compareTo(RabbitRegistrarMode.PROVIDER) == 0) {
                registrarContainer(checkComponent(
                        attributes.getString("exchangeName"),
                        exchangeType.getType(),
                        attributes.getString("queueName"),
                        attributes.getString("routingKey")), registry);
            } else if (registrarType.compareTo(RabbitRegistrarMode.CONSUMER) == 0) {
                consumerRegistry(attributes.getString("queueName"),
                        attributes.getClass("consumerClasses"), registry);
            } else {
                registrarContainer(checkComponent(
                        attributes.getString("exchangeName"),
                        exchangeType.getType(),
                        attributes.getString("queueName"),
                        attributes.getString("routingKey")), registry);

                consumerRegistry(attributes.getString("queueName"),
                        attributes.getClass("consumerClasses"), registry);
            }
        }
    }

    /**
     * 消费端监听容器注入
     * @param queueName
     * @param consumerClass
     * @param registry
     */
    private void consumerRegistry(String queueName, Class<?> consumerClass, BeanDefinitionRegistry registry) {
        Preconditions.checkNotNull(queueName, "sorry rabbitmq consumer queue name can not be null.");

        if (!RabbitBaseConsumer.class.isAssignableFrom(consumerClass)) {
            throw new RuntimeException("sorry rabbitmq consumer class can not be null and parent class must extends RabbitBaseConsumer.class.");
        }

        // 获取第一个数组中的Class作为消费端
        AbstractBeanDefinition consumer = BeanDefinitionBuilder.rootBeanDefinition(RabbitReceiveContainer.class)
                .addConstructorArgValue(queueName)
                .addConstructorArgValue(consumerClass).getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(consumer, registry);
    }

    /**
     * 组件容器注入
     * @param rabbitComponentEntity 组件实体
     * @param registry 注册容器
     */
    private void registrarContainer(RabbitComponentEntity rabbitComponentEntity, BeanDefinitionRegistry registry) {
        AbstractBeanDefinition exchange = null;
        switch (rabbitComponentEntity.getExchangeType()) {
            case "direct":
                exchange = BeanDefinitionBuilder.rootBeanDefinition(DirectExchange.class)
                        .addConstructorArgValue(rabbitComponentEntity.getExchangeName()).addConstructorArgValue(true)
                        .addConstructorArgValue(false).addConstructorArgValue(null).getBeanDefinition();
                break;
            case "topic":
                exchange = BeanDefinitionBuilder.rootBeanDefinition(TopicExchange.class)
                        .addConstructorArgValue(rabbitComponentEntity.getExchangeName()).addConstructorArgValue(true)
                        .addConstructorArgValue(false).addConstructorArgValue(null).getBeanDefinition();
                break;
            case "fanout":
                exchange = BeanDefinitionBuilder.rootBeanDefinition(FanoutExchange.class)
                        .addConstructorArgValue(rabbitComponentEntity.getExchangeName()).addConstructorArgValue(true)
                        .addConstructorArgValue(false).addConstructorArgValue(null).getBeanDefinition();
                break;
        }
        BeanDefinitionReaderUtils.registerWithGeneratedName(exchange, registry);

        Map<String, Object> args = new HashMap<>(2);
        args.put("x-overflow", "reject-publish");
        args.put("x-queue-mode", "lazy");

        AbstractBeanDefinition queue = BeanDefinitionBuilder.rootBeanDefinition(Queue.class)
                .addConstructorArgValue(rabbitComponentEntity.getQueueName()).addConstructorArgValue(true)
                .addConstructorArgValue(false).addConstructorArgValue(false)
                .addConstructorArgValue(args).getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(queue, registry);

        AbstractBeanDefinition binding = BeanDefinitionBuilder.rootBeanDefinition(Binding.class)
                .addConstructorArgValue(rabbitComponentEntity.getQueueName()).addConstructorArgValue(Binding.DestinationType.QUEUE)
                .addConstructorArgValue(rabbitComponentEntity.getExchangeName()).addConstructorArgValue(rabbitComponentEntity.getRoutingKey())
                .addConstructorArgValue(Collections.EMPTY_MAP).getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(binding, registry);
    }

    /**
     * 检查注入组件内容是否正确
     * @param exchangeName 交换机名称
     * @param exchangeType 交换机类型
     * @param queueName 队列名称
     * @param routingKey 路由key
     * @return
     */
    private RabbitComponentEntity checkComponent(String exchangeName, String exchangeType,
                                                 String queueName, String routingKey) {
        exchangeName = Strings.emptyToNull(exchangeName);
        exchangeType = Strings.emptyToNull(exchangeType);
        queueName = Strings.emptyToNull(queueName);
        routingKey = Strings.emptyToNull(routingKey);

        Preconditions.checkNotNull(exchangeName, "sorry rabbitmq component exchangeName can not be null.");

        Preconditions.checkNotNull(exchangeType, "sorry rabbitmq component exchangeType can not be null.");

        Preconditions.checkNotNull(queueName, "sorry rabbitmq component queueName can not be null.");

        Preconditions.checkNotNull(routingKey, "sorry rabbitmq component routingKey can not be null.");

        if (!RabbitExchangeTypeMode.getAllType().contains(exchangeType)) {
          throw new RuntimeException("sorry rabbitmq component exchangeType can not be " + exchangeType + ", Types include fanout、direct、topic.");
        }

        RabbitComponentEntity rabbitComponentEntity = new RabbitComponentEntity();
        rabbitComponentEntity.setExchangeName(exchangeName);
        rabbitComponentEntity.setExchangeType(exchangeType);
        rabbitComponentEntity.setQueueName(queueName);
        rabbitComponentEntity.setRoutingKey(routingKey);

        return rabbitComponentEntity;
    }
}
