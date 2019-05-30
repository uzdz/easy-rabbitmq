package com.easy.rabbitmq;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * spring容器
 * {@link RabbitSpringFactory spring factory} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/5 15:49
 */
public class RabbitSpringFactory implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory factory) {
        beanFactory = factory;
    }

    /**
     * 根据类获取bean
     * @param aClass
     * @return
     */
    public static <T> T getBean(Class<T> aClass) {
        if (null != beanFactory) {
            return beanFactory.getBean(aClass);
        }
        return null;
    }
}
