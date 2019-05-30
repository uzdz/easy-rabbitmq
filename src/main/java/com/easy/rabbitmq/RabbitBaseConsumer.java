package com.easy.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * 消费端父类
 * {@link RabbitBaseConsumer base} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/4 16:16
 */
public abstract class RabbitBaseConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitBaseConsumer.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void handleMessage(String message) {
        try {
            logger.info("接受到消息，开始进行转换处理。message：" + message);
            Class<?> aClass = convertClass();
            if (aClass.equals(String.class)) {
                consumer(message);
            } else {
                consumer(objectMapper.readValue(message, aClass));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("消费端接受消息异常，异常原因：" + e.getMessage());
        }
    }

    /**
     * 子类接受消息需要转换成的类
     * @return
     */
    abstract public Class<?> convertClass();

    /**
     * 子类消费实现
     * @param message
     */
    abstract public void consumer(Object message);
}
