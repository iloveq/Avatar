package com.woaiqw.avatar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.woaiqw.avatar.IAvatarAidlInterface;
/**
 * Created by haoran on 2018/8/31.
 * Toruk Makto ：rider of the last shadow
 */
public class ShadowService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
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

    IAvatarAidlInterface.Stub stub = new IAvatarAidlInterface.Stub(){

        @Override
        public String post(String tag, String content) throws RemoteException {
            return null;
        }
    };

}
