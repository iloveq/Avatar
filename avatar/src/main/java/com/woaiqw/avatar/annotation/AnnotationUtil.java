package com.woaiqw.avatar.annotation;

import com.woaiqw.avatar.bean.SubscribeInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haoran on 2018/9/20.
 */
public class AnnotationUtil {
    public AnnotationUtil() {
        throw new IllegalStateException("the obj cannot be new ");
    }

    /******* utils ***************************************************************************************************/

    public static List<SubscribeInfo> processorAnnotation(Object o, String currentProcessName) {

        List<SubscribeInfo> info = new ArrayList<>();


        for (Method method : o.getClass().getDeclaredMethods()) {
            if (method.isBridge()) {
                continue;
            }
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Method " + method + " has @SubscribeInfo annotation but requires "
                            + parameterTypes.length + " arguments.  Methods must require a single argument.");
                }
                Class<?> parameterClazz = parameterTypes[0];
                if (parameterClazz.isInterface()) {
                    throw new IllegalArgumentException("Method " + method + " has @SubscribeInfo annotation on " + parameterClazz
                            + " which is an interface.  Subscription must be on a concrete class type.");
                }
                if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
                    throw new IllegalArgumentException("Method " + method + " has @SubscribeInfo annotation on " + parameterClazz
                            + " but is not 'public'.");
                }

                Subscribe annotation = method.getAnnotation(Subscribe.class);
                ThreadMode thread = annotation.thread();
                String tag = annotation.tag();
                SubscribeInfo bean = new SubscribeInfo();
                bean.className = o.getClass().getName();
                bean.process = currentProcessName;
                bean.methodName = method.getName();
                bean.thread = thread.name();
                bean.tag = tag;
                bean.methodParam="avatar";
                info.add(bean);

            }
        }

        return info;
    }

}
