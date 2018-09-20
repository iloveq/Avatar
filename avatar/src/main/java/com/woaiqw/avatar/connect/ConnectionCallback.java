package com.woaiqw.avatar.connect;

import android.os.RemoteException;

import com.woaiqw.avatar.IAvatarAidlInterface;

/**
 * Created by haoran on 2018/9/20.
 */
public interface ConnectionCallback {

    void onConnected(IAvatarAidlInterface stub) throws RemoteException;

    void onDisconnected(IAvatarAidlInterface stub);

}
