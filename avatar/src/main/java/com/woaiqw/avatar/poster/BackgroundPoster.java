package com.woaiqw.avatar.poster;

import com.woaiqw.avatar.PendingPost;
import com.woaiqw.avatar.PendingPostQueue;
import com.woaiqw.avatar.Shadow;

/**
 * Posts events in background.
 *
 * @author Markus
 */
public final class BackgroundPoster implements Runnable, Poster {

    private final PendingPostQueue queue;
    private final Shadow s;

    private volatile boolean executorRunning;

    public BackgroundPoster(Shadow s) {
        this.s = s;
        queue = new PendingPostQueue();
    }

    public void enqueue(String subscribeInfo, String source) {
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscribeInfo, source);
        synchronized (this) {
            queue.enqueue(pendingPost);
            if (!executorRunning) {
                executorRunning = true;
                s.getExecutorService().execute(this);
            }
        }
    }

    @Override
    public void run() {
        try {
            try {
                while (true) {
                    PendingPost pendingPost = queue.poll(1000);
                    if (pendingPost == null) {
                        synchronized (this) {
                            // Check again, this time in synchronized
                            pendingPost = queue.poll();
                            if (pendingPost == null) {
                                executorRunning = false;
                                return;
                            }
                        }
                    }
                    s.invokeSubscriber(pendingPost);
                }
            } catch (InterruptedException e) {
                // s.getLogger().log(Level.WARNING, Thread.currentThread().getName() + " was interruppted", e);
            }
        } finally {
            executorRunning = false;
        }
    }

}
