package com.woaiqw.avatar.annotation;

import com.woaiqw.avatar.thread.ThreadMode;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by haoran on 2018/9/20.
 */
public class AnnotationUtil {
    public AnnotationUtil() {
        throw new IllegalStateException("the obj cannot be new ");
    }

    /******* utils ***************************************************************************************************/

    public static String processorAnnotation(Object o) {

        StringBuilder s = new StringBuilder();
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
                ThreadMode thread = annotation.thread();
                String tag = annotation.tag();
                //methodName.tag.threadName.content$
                s.append(method.getName()).append(".").append(tag).append(".").append(thread.name()).append(".").append("avatar").append("$");

            }
        }
        return s.toString();
    }

}
