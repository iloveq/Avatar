package com.woaiqw.avatar.model;

/**
 * Created by haoran on 2018/8/31.
 */
public class PostCard {

    private String tag;

    private Object eventObj;

    public PostCard(String tag, Object eventObj) {
        this.tag = tag;
        this.eventObj = eventObj;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getEventObj() {
        return eventObj;
    }

    public void setEventObj(Object eventObj) {
        this.eventObj = eventObj;
    }
}
