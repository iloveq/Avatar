package com.woaiqw.avatar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.woaiqw.avatar.bean.SubscribeInfo;
import com.woaiqw.avatar.helper.Shadow;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haoran on 2018/8/31.
 * Toruk Makto ：rider of the last shadow
 */
public class ShadowService extends Service {


    // LruCache for post
    private LinkedHashMap<String, String> postMap;
    // Subscribes: key-register value-SubscribeInfo
    private HashMap<String, List<SubscribeInfo>> subscribes;

    private Shadow s;

    private Gson gson;

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        postMap = new LinkedHashMap<>(16, 0.75f, true);
        subscribes = new HashMap<>();
        gson = new Gson();
        s = Shadow.getDefault();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    IAvatarAidlInterface.Stub stub = new IAvatarAidlInterface.Stub() {

        @Override
        public void post(String tag, String content) {
            if (postMap.get(tag) == null) {
                postMap.put(tag, content);
            }
            String tempContent = postMap.get(tag);
            Log.e("Avatar", "subscribes.size:" + subscribes.size() + "--------" + "postMap.size:" + postMap.size());
            for (Map.Entry<String, List<SubscribeInfo>> entry : subscribes.entrySet()) {
                for (SubscribeInfo bean : entry.getValue()) {
                    if (tag.equals(bean.tag)) {
                        bean.methodParam = tempContent;
                        String wrapInfo = gson.toJson(bean);
                        try {
                            switch (bean.thread) {
                                case "POSTING":
                                    s.invokeSubscriber(wrapInfo, entry.getKey());
                                    break;
                                case "MAIN":
                                    if (s.isMainThread()) {
                                        s.invokeSubscriber(wrapInfo, entry.getKey());
                                    } else {
                                        s.getMainThreadPoster().enqueue(wrapInfo, entry.getKey());
                                    }
                                    break;
                                case "BACKGROUND":
                                    if (s.isMainThread()) {
                                        s.getBackgroundPoster().enqueue(wrapInfo, entry.getKey());
                                    } else {
                                        s.invokeSubscriber(wrapInfo, entry.getKey());
                                    }
                                    break;
                                case "ASYNC":
                                    s.getAsyncPoster().enqueue(wrapInfo, entry.getKey());
                                    break;
                                default:
                                    throw new IllegalStateException("Unknown thread mode: " + bean.thread);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        @Override
        public void register(String className, String list) {
            Log.e("className:----", list);
            List<SubscribeInfo> info = gson.fromJson(list, new TypeToken<List<SubscribeInfo>>() {
            }.getType());
            subscribes.put(className, info);

        }

        @Override
        public void unregister(String className) {
            clearTargetRegisterInSubscribesMap(className);
        }


    };

    private void clearTargetRegisterInSubscribesMap(String className) {
        if (TextUtils.isEmpty(className)) {
            return;
        }
        for (Map.Entry<String, List<SubscribeInfo>> entry : subscribes.entrySet()) {
            if (className.equals(entry.getKey().getClass().getName())) {
                subscribes.remove(entry.getKey());
            }
        }
    }


}
