package com.woaiqw.avatar.connect;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.woaiqw.avatar.IAvatarAidlInterface;

/**
 * Created by haoran on 2018/9/20.
 */
public class Connection implements ServiceConnection {

    private IAvatarAidlInterface stub;
    private ConnectionCallback callback;

    public Connection(ConnectionCallback c) {
        callback = c;
    }

    public IAvatarAidlInterface getStub() {
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
