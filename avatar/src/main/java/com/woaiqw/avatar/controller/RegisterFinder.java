package com.woaiqw.avatar.controller;

import com.woaiqw.avatar.annotation.Subscribe;
import com.woaiqw.avatar.model.SubscribeInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by haoran on 2018/9/3.
 */
public class RegisterFinder {

    static HashMap<String, SubscribeInfo> map = new HashMap<>();

    public static HashMap<String, SubscribeInfo> getSubscribes() {
        return map;
    }

    public static void processorSubscribes(Object o) {
        if (o == null) {
            return;
        }
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (method.isBridge()) {
                continue;
            }
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation but requires "
                            + parameterTypes.length + " arguments.  Methods must require a single argument.");
                }
                Class<?> parameterClazz = parameterTypes[0];
                if (parameterClazz.isInterface()) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + parameterClazz
                            + " which is an interface.  Subscription must be on a concrete class type.");
                }
                if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + parameterClazz
                            + " but is not 'public'.");
                }
                // create subscribes
                Subscribe annotation = method.getAnnotation(Subscribe.class);
                int thread = annotation.thread();
                String tag = annotation.tag();
                SubscribeInfo info = new SubscribeInfo(o, thread, method, parameterClazz);
                map.put(tag, info);
            }
        }

    }
}
