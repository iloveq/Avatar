package com.woaiqw.avatar.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.woaiqw.avatar.log.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;



/**
 * Created by wanglei on 2016/11/25.
 */

public class ProcessUtil {

    private static final String TAG = "ProcessUtil";
    private static String sProcessName;

    public static boolean isMainProcess(Context context) {
        String processName = getProcessName(context);
        if (processName.equals(context.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 这是最可靠的一种获取当前进程名称的方式
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        if (!TextUtils.isEmpty(sProcessName)) {
            return sProcessName;
        }
        int count = 0;
        do {
            String processName = getProcessNameImpl(context);
            if (!TextUtils.isEmpty(processName)) {
                sProcessName = processName;
                return processName;
            }
        } while (count++ < 3);

        return null;
    }

    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Exception e) {
            Log.e(TAG,"getProcessName read is fail. exception="+ e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e(TAG,"getProcessName close is fail. exception=" + e);
            }
        }
        return null;
    }

    private static String getProcessNameImpl(Context context) {
        // get by ams
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
        if (processes != null) {
            int pid = android.os.Process.myPid();
            for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
                if (processInfo.pid == pid && !TextUtils.isEmpty(processInfo.processName)) {
                    return processInfo.processName;
                }
            }
        }

        // get from kernel
        String ret = getProcessName(android.os.Process.myPid());
        if (!TextUtils.isEmpty(ret) && ret.contains(context.getPackageName())) {
            return ret;
        }

        return null;
    }


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
