package com.woaiqw.avatar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.woaiqw.avatar.annotation.Subscribe;
import com.woaiqw.avatar.model.PostCard;
import com.woaiqw.avatar.model.SubscribeInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
    private LinkedHashMap<String, PostCard> postMap;
    // Subscribes: key-register value-SubscribeInfo
    private HashMap<Object, List<SubscribeInfo>> subscribes;

    @Override
    public IBinder onBind(Intent intent) {
        postMap = new LinkedHashMap<>(16, 0.75f, true);
        subscribes = new HashMap<>();
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
                postMap.put(tag, new PostCard(tag, content));
            }

            PostCard postCard = postMap.get(tag);
            String eventObj = postCard.getEventObj();

            Log.e("Avatar", "subscribes.size:" + subscribes.size() + "--------" + "postMap.size:" + postMap.size());

            for (Map.Entry<Object, List<SubscribeInfo>> entry : subscribes.entrySet()) {
                for (SubscribeInfo info : entry.getValue()) {
                    if (tag.equals(info.getTag())) {
                        try {
                            info.getMethod().invoke(entry.getKey(), eventObj);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

        @Override
        public void register(String className) {
            processorSubscribes(className);

        }

        @Override
        public void unregister(String className) {
            clearTargetRegister(className);
        }


    };

    private void clearTargetRegister(String className) {
        if (TextUtils.isEmpty(className)) {
            return;
        }
        for (Map.Entry<Object, List<SubscribeInfo>> entry : subscribes.entrySet()) {
            if (className.equals(entry.getKey().getClass().getName())) {
                subscribes.remove(entry.getKey());
            }
        }
    }


    public void processorSubscribes(String className) {
        Object o = null;
        try {
            try {
                o = Class.forName(className).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (o == null) {
            return;
        }
        List<SubscribeInfo> list = new ArrayList<>();
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (method.isBridge()) {
                continue;
            }
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation but requires "
                            + parameterTypes.length + " arguments.  Methods must require a single argument.");
                }
                Class<?> parameterClazz = parameterTypes[0];
                if (parameterClazz.isInterface()) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + parameterClazz
                            + " which is an interface.  Subscription must be on a concrete class type.");
                }
                if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
                    throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + parameterClazz
                            + " but is not 'public'.");
                }
                // create subscribes
                Subscribe annotation = method.getAnnotation(Subscribe.class);
                int thread = annotation.thread();
                String tag = annotation.tag();
                SubscribeInfo info = new SubscribeInfo(tag, thread, method, parameterClazz);
                list.add(info);
            }
        }
        subscribes.put(o, list);
    }


}
