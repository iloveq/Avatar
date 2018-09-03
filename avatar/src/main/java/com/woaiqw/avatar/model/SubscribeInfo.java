package com.woaiqw.avatar.model;

import java.lang.reflect.Method;

/**
 * Created by haoran on 2018/8/31.
 */
public class SubscribeInfo {

    private final Object source;

    private final int threadMode;

    private final Method method;

    private final Class<?> event;

    public SubscribeInfo(Object source, int threadMode, Method method, Class<?> event) {
        this.source = source;
        this.threadMode = threadMode;
        this.method = method;
        this.event = event;
    }

    public Object getSource() {
        return source;
    }

    public int getThreadMode() {
        return threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "SubscribeInfo{" +
                "source=" + source +
                ", threadMode=" + threadMode +
                ", method=" + method +
                ", event=" + event +
                '}';
    }
}
