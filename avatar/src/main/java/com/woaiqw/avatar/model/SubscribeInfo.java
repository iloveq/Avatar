package com.woaiqw.avatar.model;

/**
 * Created by haoran on 2018/8/31.
 */
public class SubscribeInfo {

    private int threadMode;

    private Class register;

    private Object eventObj;

    public SubscribeInfo(int threadMode, Class register, Object eventObj) {
        this.threadMode = threadMode;
        this.register = register;
        this.eventObj = eventObj;
    }

    public int getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(int threadMode) {
        this.threadMode = threadMode;
    }

    public Class getRegister() {
        return register;
    }

    public void setRegister(Class register) {
        this.register = register;
    }

    public Object getEventObj() {
        return eventObj;
    }

    public void setEventObj(Object eventObj) {
        this.eventObj = eventObj;
    }


    @Override
    public String toString() {
        return "SubscribeInfo{" +
                "threadMode=" + threadMode +
                ", register=" + register +
                ", eventObj=" + eventObj +
                '}';
    }
}
