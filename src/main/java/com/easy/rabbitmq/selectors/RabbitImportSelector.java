package com.easy.rabbitmq.selectors;

import com.easy.rabbitmq.RabbitAutoConfiguration;
import com.easy.rabbitmq.RabbitSpringFactory;
import com.easy.rabbitmq.annotations.EnableRabbitMq;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.List;

/**
 * RabbitMQ 自动配置组件注入
 * {@link RabbitImportSelector registrar} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
* @date: 2018/11/30 18:20
 */
public class RabbitImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableRabbitMq.class.getName()));

        boolean startup = attributes.getBoolean("startup");

        String[] component = new String[2];
        if (startup) {
            RabbitAutoConfiguration.startup = true;
            component[0] = RabbitAutoConfiguration.class.getName();
            component[1] = RabbitSpringFactory.class.getName();
        }

        List<String> upAssembly = Arrays.asList(component);
        if (upAssembly.contains(RabbitAutoConfiguration.class.getName()) &&
                upAssembly.contains(RabbitSpringFactory.class.getName())) {
            return component;
        } else {
            return new String[0];
        }
    }
}
