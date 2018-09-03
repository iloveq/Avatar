package com.woaiqw.avatar.model;

/**
 * Created by haoran on 2018/8/31.
 */
public class PostCard {

    private String tag;

    private String eventObj;

    private final int hashCode;

    public PostCard(String tag, String eventObj) {
        this.tag = tag;
        this.eventObj = eventObj;
        final int prime = 31;
        hashCode = (prime + tag.hashCode()) * prime + eventObj.hashCode();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostCard postCard = (PostCard) o;
        return hashCode == postCard.hashCode &&
                tag.equals(postCard.tag) &&
                eventObj.equals(postCard.eventObj);
    }

    @Override
    public int hashCode() {

        return hashCode;
    }

    @Override
    public String toString() {
        return "PostCard{" +
                "tag='" + tag + '\'' +
                ", eventObj='" + eventObj + '\'' +
                ", hashCode=" + hashCode +
                '}';
    }
}
