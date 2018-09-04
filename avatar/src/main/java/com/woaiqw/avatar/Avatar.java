package com.woaiqw.avatar;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.woaiqw.avatar.utils.ProcessUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by haoran on 2018/8/31.
 */
public class Avatar {


    private static Avatar sInstance;
    private static Context appContext;
    private static AtomicBoolean initFlag = new AtomicBoolean(false);
    private static boolean isMainProcess = false;

    private Connection con;

    public static void init(Application context) {
        if (initFlag.get() || context == null) {
            return;
        }
        appContext = context;
        isMainProcess = ProcessUtil.isMainProcess(context);
        initFlag.set(true);
    }

    public static Avatar get() {
        if (null == sInstance) {
            synchronized (Avatar.class) {
                if (null == sInstance) {
                    sInstance = new Avatar();
                }
            }
        }
        return sInstance;
    }

    /**** outer method ******************************************************************************************************/


    public void post(final String tag, final String content) {

        if (!initFlag.get()) {
            return;
        }

        try {
            if (con == null) {
                postInterval(tag, content);
            } else {
                if (con.getStub() != null) {
                    con.getStub().post(tag, content);
                } else {
                    postInterval(tag, content);
                }
            }

        } catch (Exception e) {

        }


    }


    //@Register
    public void register(final Object o) {

        try {
            final String name = o.getClass().getName();
            if (con == null) {
                register(name);
            } else {
                if (con.getStub() != null) {
                    con.getStub().register(name);
                } else {
                    unregister(name);
                }
            }

        } catch (Exception e) {

        }

    }


    //@Register
    public void unregister(final Object o) {
        try {
            final String name = o.getClass().getName();
            if (con == null) {
                unregister(name);
            } else {
                if (con.getStub() != null) {
                    con.getStub().unregister(name);
                } else {
                    unregister(name);
                }
            }

        } catch (Exception e) {

        }

    }

    /**** interval ******************************************************************************************************/

    private void postInterval(final String tag, final String content) {
        appContext.bindService(new Intent(appContext, ShadowService.class), con = new Connection(new ConnectionCallback() {
            @Override
            public void onConnected(IAvatarAidlInterface stub) throws RemoteException {
                stub.post(tag, content);
            }

            @Override
            public void onDisconnected(IAvatarAidlInterface stub) {

            }
        }), Context.BIND_AUTO_CREATE);
    }

    private void register(final String name) {
        appContext.bindService(new Intent(appContext, ShadowService.class), con = new Connection(new ConnectionCallback() {
            @Override
            public void onConnected(IAvatarAidlInterface stub) throws RemoteException {
                stub.register(name);
            }

            @Override
            public void onDisconnected(IAvatarAidlInterface stub) {

            }
        }), Context.BIND_AUTO_CREATE);
    }

    private void unregister(final String name) {
        appContext.bindService(new Intent(appContext, ShadowService.class), con = new Connection(new ConnectionCallback() {
            @Override
            public void onConnected(IAvatarAidlInterface stub) throws RemoteException {
                stub.unregister(name);
            }

            @Override
            public void onDisconnected(IAvatarAidlInterface stub) {

            }
        }), Context.BIND_AUTO_CREATE);
    }

    /******* inner define ***************************************************************************************************/

    private interface ConnectionCallback {

        void onConnected(IAvatarAidlInterface stub) throws RemoteException;

        void onDisconnected(IAvatarAidlInterface stub);

    }

    private class Connection implements ServiceConnection {

        IAvatarAidlInterface stub;
        ConnectionCallback callback;

        Connection(ConnectionCallback c) {
            callback = c;
        }

        IAvatarAidlInterface getStub() {
            return stub;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            stub = IAvatarAidlInterface.Stub.asInterface(service);
            try {
                callback.onConnected(stub);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            callback.onDisconnected(stub);
        }
    }


}
