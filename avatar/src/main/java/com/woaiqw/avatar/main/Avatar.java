package com.woaiqw.avatar.main;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import com.woaiqw.avatar.IAvatarAidlInterface;
import com.woaiqw.avatar.service.ShadowService;

/**
 * Created by haoran on 2018/8/31.
 */
public class Avatar {

    private ShadowService service;
    private Application app;
    private static volatile Avatar instance;

    private Avatar(Application app) {
        this.app = app;
        service = new ShadowService();
    }

    //初始化
    public static synchronized Avatar getInstance(@NonNull Application context) {
        if (instance == null) {
            instance = new Avatar(context);
        }
        return instance;
    }


    /**
     * @param tag
     * @param content
     */
    public void post(final String tag, final String content) {
        //TODO：aidl post 方法 传递进程信息 onBind 解析   mProcessName = ProcessUtil.getProcessName(context, ProcessUtil.getMyProcessId());
        app.bindService(new Intent(app, ShadowService.class), new ServiceConnection() {
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


}
