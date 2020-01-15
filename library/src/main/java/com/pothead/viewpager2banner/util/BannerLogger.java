package com.pothead.viewpager2banner.util;

import android.util.Log;

/**
 * Created by Pothead on 2019-12-03.
 */
public class BannerLogger {
    private static final String TAG = "ViewPager2Banner";

    private static boolean isDebug = false;

    private BannerLogger() {

    }

    public static void setIsDebug(boolean isDebug) {
        BannerLogger.isDebug = isDebug;
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

}
