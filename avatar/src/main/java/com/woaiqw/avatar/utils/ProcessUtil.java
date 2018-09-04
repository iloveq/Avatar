package com.woaiqw.avatar.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

/**
 * Created by wanglei on 2016/11/25.
 */

public class ProcessUtil {


    public static String getCurrentProcessName(Application application) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) application.getSystemService
                (Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    public static boolean isMainProcess(Application application) {
        String packageName = application.getPackageName();
        return packageName.equals(ProcessUtil.getCurrentProcessName(application));
    }

}
