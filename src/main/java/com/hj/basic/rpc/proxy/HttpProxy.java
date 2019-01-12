package com.hj.basic.rpc.proxy;

import com.hj.basic.rpc.handler.HttpInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author tangj
 * @description
 * @since 2019/1/12 16:54
 */
public class HttpProxy extends Proxy {

    protected HttpProxy(InvocationHandler h) {
        super(h);
    }

    public static Object getProxy(Class cl, HttpInvocationHandler httpInvocationHandler){
        return Proxy.newProxyInstance(cl.getClassLoader(), cl.getInterfaces(), httpInvocationHandler);
    }
}
