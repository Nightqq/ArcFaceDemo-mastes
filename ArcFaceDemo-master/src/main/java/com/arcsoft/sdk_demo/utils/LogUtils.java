package com.arcsoft.sdk_demo.utils;

import android.util.Log;

/**
 * Created by Administrator on 2018/4/13.
 */

public class LogUtils {
    //可以全局控制是否打印log日志
    private static boolean isPrintLog = true;
    private static int LOG_MAXLENGTH = 2000;
    public static void v(String msg) {
        v("LogUtil", msg);
    }

    public static void v(String tagName, String msg) {
        if (isPrintLog) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.v(tagName + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.v(tagName + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }
}
