package com.woaiqw.avatar.helper;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.woaiqw.avatar.Avatar;
import com.woaiqw.avatar.CacheCenter;
import com.woaiqw.avatar.bean.SubscribeInfo;
import com.woaiqw.avatar.poster.AsyncPoster;
import com.woaiqw.avatar.poster.BackgroundPoster;
import com.woaiqw.avatar.poster.MainThreadSupport;
import com.woaiqw.avatar.poster.PendingPost;
import com.woaiqw.avatar.poster.Poster;
import com.woaiqw.avatar.utils.ProcessUtil;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;

/**
 * Created by haoran on 2018/9/5.
 */
public class Shadow {

    private static final String TAG = "Shadow";

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
        mainThreadPoster = mainThreadSupport != null ? mainThreadSupport.createPoster(this) : null;
        backgroundPoster = new BackgroundPoster(this);
        asyncPoster = new AsyncPoster(this);
    }

    public boolean isMainThread() {
        return mainThreadSupport == null || mainThreadSupport.isMainThread();
    }

    public void invokeSubscriber(PendingPost pendingPost) {
        String source = pendingPost.source;
        String subscribeInfo = pendingPost.subscribeInfo;
        PendingPost.releasePendingPost(pendingPost);
        invokeSubscriber(subscribeInfo, source);

    }

    public void invokeSubscriber(String subscribe, String source) {
        SubscribeInfo info = new Gson().fromJson(subscribe, SubscribeInfo.class);
        try {
            Object o = CacheCenter.getInstance().get(source);
            if (o == null && ProcessUtil.isChildProcess(info.process)) {
                Log.e(TAG, "register is null");
                Intent intent = new Intent();
                intent.setAction("POST");
                intent.putExtra("source", source);
                intent.putExtra("info", subscribe);
                Avatar.appContext.sendBroadcast(intent);
                return;
            }
            Log.e(TAG, o.toString());
            Method method = o.getClass().getDeclaredMethod(info.methodName, String.class);
            Log.e(TAG, "create method");
            method.invoke(o, info.methodParam);
            Log.e(TAG, "method.invoke");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
