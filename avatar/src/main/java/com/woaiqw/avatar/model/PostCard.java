package com.woaiqw.avatar.model;

/**
 * Created by haoran on 2018/8/31.
 */
public class PostCard {

    private String tag;

    private String eventObj;

    public PostCard(String tag, String eventObj) {
        this.tag = tag;
        this.eventObj = eventObj;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getEventObj() {
        return eventObj;
    }

    public void setEventObj(String eventObj) {
        this.eventObj = eventObj;
    }
}
