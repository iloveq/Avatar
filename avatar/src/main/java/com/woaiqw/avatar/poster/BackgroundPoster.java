/*
 * Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.woaiqw.avatar.poster;

import com.woaiqw.avatar.PendingPost;
import com.woaiqw.avatar.PendingPostQueue;
import com.woaiqw.avatar.Shadow;
import com.woaiqw.avatar.model.SubscribeInfo;

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

    public void enqueue(SubscribeInfo subscribeInfo, String source) {
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
