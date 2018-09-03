package com.woaiqw.avatar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.woaiqw.avatar.controller.RegisterFinder;
import com.woaiqw.avatar.model.PostCard;
import com.woaiqw.avatar.model.SubscribeInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by haoran on 2018/8/31.
 * Toruk Makto ：rider of the last shadow
 */
public class ShadowService extends Service {

    // LruCache for post
    private LinkedHashMap<String, PostCard> postMap;
    // Subscribes: key-register value-SubscribeInfo
    private HashMap<String, SubscribeInfo> subscribes;

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        postMap = new LinkedHashMap<>(16, 0.75f, true);
        subscribes = new HashMap<>();
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
                postMap.put(tag, new PostCard(tag, content));
            }

            PostCard postCard = postMap.get(tag);
            String eventObj = postCard.getEventObj();
            SubscribeInfo subscribeInfo = subscribes.get(tag);

            try {
                subscribeInfo.getMethod().invoke(subscribeInfo.getSource(), eventObj);
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof Error) {
                    throw (Error) e.getCause();
                }
            }

        }

        @Override
        public void register() {

            subscribes.putAll(RegisterFinder.getSubscribes());

        }

        @Override
        public void unregister() {
            subscribes.remove("");

        }


    };

}
