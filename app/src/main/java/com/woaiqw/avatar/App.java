package com.woaiqw.avatar;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Avatar.setContext(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
