package com.easy.rabbitmq.annotations;

import com.easy.rabbitmq.selectors.RabbitImportSelector;
import org.springframework.context.annotation.Import;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * RabbitMQ 启动配置注解
 * {@link EnableRabbitMq annotation} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/11/30 15:42
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = java.lang.annotation.ElementType.TYPE)
@Documented
@Import(RabbitImportSelector.class)
public @interface EnableRabbitMq {

    /**
     * 启动RabbitMQ消息中间件
     * @return the default value is false
     */
    boolean startup() default false;
}
