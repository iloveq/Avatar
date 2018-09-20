package com.woaiqw.avatar.poster;


import com.woaiqw.avatar.helper.Shadow;

/**
 * Posts events in background.
 *
 * @author Markus
 */
public class AsyncPoster implements Runnable, Poster {

    private final PendingPostQueue queue;
    private final Shadow s;

    public AsyncPoster(Shadow eventBus) {
        this.s = eventBus;
        queue = new PendingPostQueue();
    }

    public void enqueue(String subscribeInfo, String event) {
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscribeInfo, event);
        queue.enqueue(pendingPost);
        s.getExecutorService().execute(this);
    }

    @Override
    public void run() {
        PendingPost pendingPost = queue.poll();
        if (pendingPost == null) {
            throw new IllegalStateException("No pending post available");
        }
        s.invokeSubscriber(pendingPost);
    }

}
