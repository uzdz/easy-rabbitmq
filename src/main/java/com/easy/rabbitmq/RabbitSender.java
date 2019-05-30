package com.easy.rabbitmq;

import com.easy.rabbitmq.sender.BaseSender;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.StringUtils;

/**
 * 消息发送客户端
 * {@link RabbitSender sender} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/4 00:00
 */
public class RabbitSender implements BaseSender {

    private RabbitTemplate rabbitTemplate;

    private ConnectionFactory connectionFactory;

    public RabbitSender() {
    }

    public RabbitSender(RabbitTemplate rabbitTemplate, ConnectionFactory connectionFactory) {
        this.rabbitTemplate = rabbitTemplate;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void send(String exchangeName, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    @Override
    public boolean sendMulticastMsg(String msg) {
        rabbitTemplate.convertAndSend(ExchangeConstant.MESSAGE_PUSH_MULTICAST, "", msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("60000");
                return message;
            }
        });
        return true;
    }

    @Override
    public boolean sendMsgByUserId(String userId, String msg) {
        rabbitTemplate.convertAndSend(ExchangeConstant.MESSAGE_PUSH_USER, userId, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("60000");
                return message;
            }
        });
        return true;
    }

    @Override
    public boolean sendMsgByOrganSign(String organSign, String msg) {
        rabbitTemplate.convertAndSend(ExchangeConstant.MESSAGE_PUSH_ORGANIZATION, organSign, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("60000");
                return message;
            }
        });
        return true;
    }

    @Override
    public boolean queueDeclarePassive(String queue) {
        try {
            if (StringUtils.isEmpty(queue)) {
                return false;
            }

            Channel channel = connectionFactory.createConnection().createChannel(false);
            channel.queueDeclarePassive(queue);
            channel.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
