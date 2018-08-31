package com.woaiqw.avatar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.woaiqw.avatar.IAvatarAidlInterface;
import com.woaiqw.avatar.model.PostCard;
import com.woaiqw.avatar.model.SubscribeInfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by haoran on 2018/8/31.
 * Toruk Makto ：rider of the last shadow
 */
public class ShadowService extends Service {

    //LruCache for post
    private LinkedHashMap<String, PostCard> postMap;

    //Subscribes: key-tag value-SubscribeInfo
    private HashMap<String, List<SubscribeInfo>> subscribes;

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        postMap = new LinkedHashMap<>(16, 0.75f, true);
        subscribes = new HashMap<>();
        //TODO:编译时注解 初始化subscribes
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
        public void post(String tag, String content) throws RemoteException {
            //
            postMap.put(tag, new PostCard(tag, content));

            //TODO: 通过 tag 找到 subscribe

            //TODO: 将 content 置入 SubscribeInfo 缓存起来

            //TODO: 通过 SubscribeInfo 的 class 信息 匹配 register

            //TODO; 调用 method.invoke(register) 方法 调用事件
        }

        @Override
        public void register() throws RemoteException {

            //TODO: 运行时注解添加 register 信息

        }

        @Override
        public void unregister() throws RemoteException {

            //TODO: 运行时注解对事件解绑

        }


    };

}
