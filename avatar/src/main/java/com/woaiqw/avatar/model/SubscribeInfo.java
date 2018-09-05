package com.woaiqw.avatar.model;

import com.woaiqw.avatar.thread.ThreadMode;

import java.lang.reflect.Method;

/**
 * Created by haoran on 2018/8/31.
 */
public class SubscribeInfo {

    private final String tag;

    private final ThreadMode threadMode;

    private final Method method;

    private final Class<?> event;

    public SubscribeInfo(String tag, ThreadMode threadMode, Method method, Class<?> event) {
        this.tag = tag;
        this.threadMode = threadMode;
        this.method = method;
        this.event = event;
    }

    public String getTag() {
        return tag;
    }

    public ThreadMode getThreadMode() {
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
                "tag=" + tag +
                ", threadMode=" + threadMode +
                ", method=" + method +
                ", event=" + event +
                '}';
    }
}
