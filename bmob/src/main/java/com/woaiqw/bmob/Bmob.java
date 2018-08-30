package com.woaiqw.bmob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * Created by haoran on 2018/8/30.
 */
public class Bmob {

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            proxy.accept(intent.getExtras());
        }
    };

    private Bmob() {
    }

    private static class Holder {
        private static final Bmob IN = new Bmob();
    }

    public static Bmob get() {
        return Holder.IN;
    }

    private IReceiver proxy;


    public void register(Context context, String tag, IReceiver proxy) {
        if (proxy == null) throw new RuntimeException(" proxy cannot be null ");
        this.proxy = proxy;
        context.registerReceiver(receiver, new IntentFilter(tag));
    }

    public void post(Context context, String tag, Bundle m) {
        Intent intent = new Intent(tag);
        intent.putExtras(m);
        context.sendBroadcast(intent);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(receiver);
    }


}
