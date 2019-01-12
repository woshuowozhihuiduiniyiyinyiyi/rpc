package com.hj.basic.rpc.service;

import com.hj.basic.rpc.annotation.Rpc;
import com.hj.basic.rpc.handler.HttpInvocationHandler;
import com.hj.basic.rpc.proxy.HttpProxy;
import com.hj.basic.rpc.util.PackageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author tangj
 * @description
 * @since 2019/1/12 10:20
 */
@Slf4j
public class ServicesSubscribe implements BeanDefinitionRegistryPostProcessor {

    private static String DEFAULT_PACKAGE_NAME = "com.hj";

    private static Map<String, Object> beanMap = new HashMap<>();

    public ServicesSubscribe() {
        log.info("ServicesSubscribe Construct");
        subscribe();
    }

    private void subscribe() {
        log.info("ServicesSubscribe subscribe");
        List<Class> classes = PackageUtils.scanPackage(DEFAULT_PACKAGE_NAME);
        if (CollectionUtils.isEmpty(classes)) {
            return;
        }

        Iterator<Class> iterator = classes.iterator();
        while (iterator.hasNext()) {
            Class cl = iterator.next();
            boolean flag = false;
            Method[] methods = cl.getMethods();
            for (Method method : methods) {
                if (Objects.nonNull(method.getAnnotation(Rpc.class))) {
                    flag = true;
                    break;
                }
            }

            if (!flag && Objects.isNull(cl.getAnnotation(Rpc.class))) {
                iterator.remove();
            }
        }

        if (CollectionUtils.isEmpty(classes)) {
            return;
        }

        HttpInvocationHandler httpInvocationHandler = new HttpInvocationHandler();
        for (Class cl : classes) {
            Object proxyObject = HttpProxy.getProxy(cl, httpInvocationHandler);
            beanMap.put(cl.getName(), proxyObject);
        }
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        for(Map.Entry<String, Object> entry: beanMap.entrySet()){

            beanDefinitionRegistry.registerBeanDefinition(entry.getKey(), );
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }


}
