package com.woaiqw.avatar;

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


    private static volatile Avatar instance;


    private Avatar() {
    }

    //初始化
    public static synchronized Avatar get() {
        if (instance == null) {
            instance = new Avatar();
        }
        return instance;
    }


    /**
     * @param tag
     * @param content
     */
    public void post(Context c, final String tag, final String content) {

        c.bindService(new Intent(c, ShadowService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IAvatarAidlInterface stub = IAvatarAidlInterface.Stub.asInterface(service);
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

    private ServiceConnection connection = new ServiceConnection() {
        IAvatarAidlInterface stub;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            stub = IAvatarAidlInterface.Stub.asInterface(service);
            try {
                stub.register();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                stub.unregister();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    //@Register
    public void register(Object o) {

        RegisterFinder.processorSubscribes(o);

        if (o instanceof Context) {
            Context c = (Context) o;
            Intent intent = new Intent(c, ShadowService.class);
            c.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }

    }

    //@Register
    public void unregister(final Object o) {
        if (o instanceof Context) {
            Context c = (Context) o;
            c.unbindService(connection);
        }
    }


}
