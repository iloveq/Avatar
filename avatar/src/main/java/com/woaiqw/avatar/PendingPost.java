package com.woaiqw.avatar;

import java.util.ArrayList;
import java.util.List;

public final class PendingPost {
    private final static List<PendingPost> pendingPostPool = new ArrayList<>();

    String source;
    String subscribeInfo;
    PendingPost next;

    private PendingPost(String source, String subscribeInfo) {
        this.source = source;
        this.subscribeInfo = subscribeInfo;
    }

    public static PendingPost obtainPendingPost(String subscribeInfo, String source) {
        synchronized (pendingPostPool) {
            int size = pendingPostPool.size();
            if (size > 0) {
                PendingPost pendingPost = pendingPostPool.remove(size - 1);
                pendingPost.source = source;
                pendingPost.subscribeInfo = subscribeInfo;
                pendingPost.next = null;
                return pendingPost;
            }
        }
        return new PendingPost(source, subscribeInfo);
    }

    static void releasePendingPost(PendingPost pendingPost) {
        pendingPost.source = null;
        pendingPost.subscribeInfo = null;
        pendingPost.next = null;
        synchronized (pendingPostPool) {
            // Don't let the pool grow indefinitely
            if (pendingPostPool.size() < 10000) {
                pendingPostPool.add(pendingPost);
            }
        }
    }

}