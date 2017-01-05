package com.joy.phomel.base.util;

import android.app.Activity;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by phomel on 2017/1/5.
 */

public class ActivityManager {

    //Activity列表
    public static ArrayList<Activity> activityList = new ArrayList<Activity>();

    /**
     * 添加Activity到列表中
     *
     * @param activity
     */
    public static void addAppActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    /**
     * 从列表移除Activity
     *
     * @param activity
     */
    public static void removeAppActivity(Activity activity) {
        if (activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }

    /**
     * 退出应用程序
     */
    public static void exitApp(Context context) {
        for (Activity ac : activityList) {
            if (!ac.isFinishing()) {
                ac.finish();
            }
        }
        activityList.clear();
        MobclickAgent.onKillProcess(context);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
