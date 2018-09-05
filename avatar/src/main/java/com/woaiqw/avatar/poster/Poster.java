package com.woaiqw.avatar.poster;

import com.woaiqw.avatar.model.SubscribeInfo;


public interface Poster {

    void enqueue(SubscribeInfo subscribeInfo, Object source);

}
