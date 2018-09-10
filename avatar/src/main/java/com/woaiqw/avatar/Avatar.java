package com.woaiqw.avatar;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.woaiqw.avatar.annotation.Subscribe;
import com.woaiqw.avatar.thread.ThreadMode;
import com.woaiqw.avatar.utils.ProcessUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by haoran on 2018/8/31.
 */
public class Avatar {


    private static Avatar sInstance;
    public static Context appContext;
    private static AtomicBoolean initFlag = new AtomicBoolean(false);
    private static boolean isMainProcess = false;
    private static HashMap<String, Object> sourceCache = new HashMap<>();
    private Connection con;
    private static BroadcastReceiver b = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String source = intent.getStringExtra("source");
            String subscribeInfo = intent.getStringExtra("info");
            if (!TextUtils.isEmpty(source) && !TextUtils.isEmpty(subscribeInfo)) {
                String[] info = subscribeInfo.split("\\.");
                try {
                    Object o = sourceCache.get(source);
                    if (o == null) {
                        Log.e("Shadow", "register == null");
                        return;
                    }
                    Log.e("Shadow", o.toString());
                    Method method = o.getClass().getDeclaredMethod(info[0], String.class);
                    Log.e("Shadow", "create method");
                    method.invoke(o, info[3]);
                    Log.e("Shadow", "method.invoke");
                } catch (Exception e) {
                    Log.e("Shadow", e.toString());
                    throw new RuntimeException(e);
                }
            }
        }
    };

    public HashMap<String, Object> getSourceCache() {
        return sourceCache;
    }

    public static void init(Application context) {
        if (initFlag.get() || context == null) {
            return;
        }
        appContext = context.getApplicationContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction("POST");
        appContext.registerReceiver(b, filter);
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

    public static void recycleSource() {
        appContext.unregisterReceiver(b);
        appContext = null;
        sourceCache.clear();
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

        if (!initFlag.get()) {
            return;
        }

        try {
            final String name = o.getClass().getName();
            sourceCache.put(name, o);
            String registerInfo = processorAnnotation(o);
            Log.e("AVATAR:", registerInfo);
            if (con == null) {
                register(name, registerInfo);
            } else {
                if (con.getStub() != null) {
                    con.getStub().register(name, registerInfo);
                } else {
                    unregister(name);
                }
            }

        } catch (Exception e) {

        }

    }


    //@Register
    public void unregister(final Object o) {

        if (!initFlag.get()) {
            return;
        }

        try {
            final String name = o.getClass().getName();
            sourceCache.remove(name);
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

        if (appContext == null) {
            return;
        }

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

    private void register(final String name, final String subscribes) {

        if (appContext == null) {
            return;
        }

        appContext.bindService(new Intent(appContext, ShadowService.class), con = new Connection(new ConnectionCallback() {
            @Override
            public void onConnected(IAvatarAidlInterface stub) throws RemoteException {
                stub.register(name, subscribes);
            }

            @Override
            public void onDisconnected(IAvatarAidlInterface stub) {

            }
        }), Context.BIND_AUTO_CREATE);
    }

    private void unregister(final String name) {

        if (appContext == null) {
            return;
        }

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


    /******* utils ***************************************************************************************************/

    private String processorAnnotation(Object o) {

        StringBuilder s = new StringBuilder();
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
                ThreadMode thread = annotation.thread();
                String tag = annotation.tag();
                //methodName.tag.threadName.content$
                s.append(method.getName()).append(".").append(tag).append(".").append(thread.name()).append(".").append("avatar").append("$");

            }
        }
        return s.toString();
    }

}
