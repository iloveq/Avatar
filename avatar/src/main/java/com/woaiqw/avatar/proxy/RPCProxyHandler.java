package com.woaiqw.avatar.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by haoran on 2018/9/20.
 */
public class RPCProxyHandler implements InvocationHandler {


    private Class<?> clazz;

    public <T> RPCProxyHandler(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //request

        return null;
    }


}
