package com.woaiqw.avatar;

import com.woaiqw.avatar.model.SubscribeInfo;
import com.woaiqw.avatar.poster.AsyncPoster;
import com.woaiqw.avatar.poster.BackgroundPoster;
import com.woaiqw.avatar.poster.MainThreadSupport;
import com.woaiqw.avatar.poster.Poster;

import java.util.concurrent.ExecutorService;

/**
 * Created by haoran on 2018/9/5.
 */
public class Shadow {


    private static final ShadowBuilder DEFAULT_BUILDER = new ShadowBuilder();
    private static volatile Shadow defaultInstance;

    private final ExecutorService executorService;
    private final Poster mainThreadPoster;
    private final BackgroundPoster backgroundPoster;
    private final AsyncPoster asyncPoster;

    private final MainThreadSupport mainThreadSupport;

    private Shadow() {
        this(DEFAULT_BUILDER);
    }

    public static Shadow getDefault() {
        if (defaultInstance == null) {
            synchronized (Shadow.class) {
                if (defaultInstance == null) {
                    defaultInstance = new Shadow();
                }
            }
        }
        return defaultInstance;
    }

    Shadow(ShadowBuilder builder) {
        executorService = builder.executorService;
        mainThreadSupport = builder.getMainThreadSupport();
        mainThreadPoster = mainThreadSupport != null ?mainThreadSupport.createPoster(this) : null;
        backgroundPoster = new BackgroundPoster(this);
        asyncPoster = new AsyncPoster(this);
    }

    public boolean isMainThread() {
        return mainThreadSupport == null || mainThreadSupport.isMainThread();
    }

    public void invokeSubscriber(PendingPost pendingPost) {
        Object source = pendingPost.source;
        SubscribeInfo subscribeInfo = pendingPost.subscribeInfo;
        PendingPost.releasePendingPost(pendingPost);

        invokeSubscriber(subscribeInfo, source);

    }

    void invokeSubscriber(SubscribeInfo subscribeInfo, Object source) {
        try {
            subscribeInfo.getMethod().invoke(source, subscribeInfo.getEvent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Poster getMainThreadPoster() {
        return mainThreadPoster;
    }

    public BackgroundPoster getBackgroundPoster() {
        return backgroundPoster;
    }

    public AsyncPoster getAsyncPoster() {
        return asyncPoster;
    }

}
