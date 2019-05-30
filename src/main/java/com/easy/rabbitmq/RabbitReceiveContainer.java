package com.easy.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * 消费者监听容器
 * {@link RabbitReceiveContainer container} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/4 15:45
 */
public class RabbitReceiveContainer extends SimpleMessageListenerContainer {

    public RabbitReceiveContainer() {
    }

    public RabbitReceiveContainer(String queueName, Class<?> classConsumer) {

        try {
            super.setConnectionFactory(new CachingConnectionFactory());

            // 当consumer拒绝后消息是否requeue
            super.setDefaultRequeueRejected(false);

            // 设置该监听容器内的消费者的确认应答模式为自动签收
            super.setAcknowledgeMode(AcknowledgeMode.AUTO);

            // container容器添加监听的队列
            super.setQueueNames(queueName);

            // 设置消费者的consumerTag_tag
            super.setConsumerTagStrategy(new ConsumerTagStrategy() {
                @Override
                public String createConsumerTag(String queue) {
                    try {
                        InetAddress address = InetAddress.getLocalHost();
                        String ip = address.getHostAddress();
                        String hostName = address.getHostName();
                        return ip + "_" + hostName + "_" + UUID.randomUUID();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        throw new RuntimeException("定义消费者Tag异常，异常原因：" + e.getMessage());
                    }
                }
            });

            // 设置公平分发负载均衡
            super.setPrefetchCount(1);

            super.setMessageListener(new ChannelAwareMessageListener() {
                @Override
                public void onMessage(Message message, Channel channel) throws Exception {
                    Object bean = RabbitSpringFactory.getBean(classConsumer);
                    classConsumer.getMethod("handleMessage", String.class).invoke(bean, new String(message.getBody()));

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("消费者容器初始化异常，异常原因:" + e.getMessage());
        }
    }
}
