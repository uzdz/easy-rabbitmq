package com.easy.rabbitmq.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * RabbitMQ 自定义String类型消息转换器
 * {@link TypeOfStringMessageConverter String message converter} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/11/30 21:42
 */
public class TypeOfStringMessageConverter implements MessageConverter {
    private static final Logger logger = LoggerFactory.getLogger(TypeOfStringMessageConverter.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) {
        logger.info("=========发送消息转换:toMessage=========");

        try {
            // 设置消息持久化
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            return object instanceof String ?
                    new Message(object.toString().getBytes(), messageProperties) :
                    new Message(objectMapper.writeValueAsString(object).getBytes(), messageProperties);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessageConversionException("Json串转换器发送消息异常, Object:" + object + ", MessageProperties:" + messageProperties);
        }
    }

    @Override
    public Object fromMessage(Message message) {
        logger.info("=========接受到消息转换:fromMessage=========");

        return new String(message.getBody());
    }
}
