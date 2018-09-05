
package com.woaiqw.avatar;


import android.os.Looper;

import com.woaiqw.avatar.log.Logger;
import com.woaiqw.avatar.poster.MainThreadSupport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ShadowBuilder {
    private final static ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();


    ExecutorService executorService = DEFAULT_EXECUTOR_SERVICE;
    MainThreadSupport mainThreadSupport;

    ShadowBuilder() {
    }


    MainThreadSupport getMainThreadSupport() {
        if (mainThreadSupport != null) {
            return mainThreadSupport;
        } else if (Logger.AndroidLogger.isAndroidLogAvailable()) {
            Object looperOrNull = getAndroidMainLooperOrNull();
            return looperOrNull == null ? null :
                    new MainThreadSupport.AndroidHandlerMainThreadSupport((Looper) looperOrNull);
        } else {
            return null;
        }
    }

    Object getAndroidMainLooperOrNull() {
        try {
            return Looper.getMainLooper();
        } catch (RuntimeException e) {
            // Not really a functional Android (e.g. "Stub!" maven dependencies)
            return null;
        }
    }

    public boolean isMainThread() {
        return mainThreadSupport != null ? mainThreadSupport.isMainThread() : true;
    }


    /**
     * Builds an EventBus based on the current configuration.
     */
    public Shadow build() {
        return new Shadow(this);
    }

}
