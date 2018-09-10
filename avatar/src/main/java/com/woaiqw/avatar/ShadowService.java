package com.woaiqw.avatar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
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
    private HashMap<String, List<String>> subscribes;

    private Shadow s;

    @Override
    public IBinder onBind(Intent intent) {
        postMap = new LinkedHashMap<>(16, 0.75f, true);
        subscribes = new HashMap<>();
        s = Shadow.getDefault();
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();

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
            String eventContent = postMap.get(tag);
            Log.e("Avatar", "subscribes.size:" + subscribes.size() + "--------" + "postMap.size:" + postMap.size());
            for (Map.Entry<String, List<String>> entry : subscribes.entrySet()) {
                for (String info : entry.getValue()) {
                    String[] arr = info.split("\\.");
                    if (tag.equals(arr[1])) {
                        arr[3] = eventContent;
                        StringBuilder builder = new StringBuilder();
                        info = builder.append(arr[0]).append(".").append(arr[1]).append(".").append(arr[2]).append(".").append(arr[3]).toString();
                        try {
                            switch (arr[2]) {
                                case "POSTING":
                                    s.invokeSubscriber(info, entry.getKey());
                                    break;
                                case "MAIN":
                                    if (s.isMainThread()) {
                                        s.invokeSubscriber(info, entry.getKey());
                                    } else {
                                        s.getMainThreadPoster().enqueue(info, entry.getKey());
                                    }
                                    break;
                                case "BACKGROUND":
                                    if (s.isMainThread()) {
                                        s.getBackgroundPoster().enqueue(info, entry.getKey());
                                    } else {
                                        s.invokeSubscriber(info, entry.getKey());
                                    }
                                    break;
                                case "ASYNC":
                                    s.getAsyncPoster().enqueue(info, entry.getKey());
                                    break;
                                default:
                                    throw new IllegalStateException("Unknown thread mode: " + arr[2]);
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
            String[] $s = list.split("\\$");
            subscribes.put(className, Arrays.asList($s));

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
        for (Map.Entry<String, List<String>> entry : subscribes.entrySet()) {
            if (className.equals(entry.getKey().getClass().getName())) {
                subscribes.remove(entry.getKey());
            }
        }
    }


}
