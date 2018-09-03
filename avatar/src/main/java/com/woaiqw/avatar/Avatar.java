package com.woaiqw.avatar;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.woaiqw.avatar.controller.RegisterFinder;

/**
 * Created by haoran on 2018/8/31.
 */
public class Avatar {

    private ShadowService service;

    private static volatile Avatar instance;

    private static Context c;

    private Avatar() {
        service = new ShadowService();
    }

    //初始化
    public static synchronized Avatar get() {
        if (instance == null) {
            instance = new Avatar();
        }
        return instance;
    }

    public static void setContext(Application app) {
        c = app;
    }

    /**
     * @param tag
     * @param content
     */
    public void post(final String tag, final String content) {
        //TODO：aidl post 方法 传递进程信息 onBind 解析   mProcessName = ProcessUtil.getProcessName(context, ProcessUtil.getMyProcessId());

        c.bindService(new Intent(c, ShadowService.class), new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IAvatarAidlInterface.Stub stub = (IAvatarAidlInterface.Stub) service;
                try {
                    stub.post(tag, content);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    //TODO:Service 被GC,启动失败,异常处理
    //ExceptionCallback

    //@Register
    public void register(Object o) {

        RegisterFinder.processorSubscribes(o);

        c.bindService(new Intent(c, ShadowService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IAvatarAidlInterface.Stub stub = (IAvatarAidlInterface.Stub) service;
                try {
                    stub.register();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);

    }

    //@Register
    public void unregister() {

        c.bindService(new Intent(c, ShadowService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IAvatarAidlInterface.Stub stub = (IAvatarAidlInterface.Stub) service;
                try {
                    stub.unregister();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);

    }


}
