package com.activiti.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * spring 工具类
 *
 * @author liuguofeng
 * @date 2023/11/04 12:36
 **/
@Component
public final class SpringUtils implements BeanFactoryPostProcessor {
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }


    /**
     * 获取springBean对象
     *
     * @param name ioc容器定义的名称
     * @return 結果
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 获取springBean对象
     *
     * @param c 类型
     */
    public static <T> T getBean(Class<T> c) throws BeansException {
        return (T) beanFactory.getBean(c);
    }

}
