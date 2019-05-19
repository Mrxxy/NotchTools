package com.github.mrxxy.notchtools.phone;


import android.app.Activity;
import android.os.Build;
import android.view.Window;

import androidx.annotation.RequiresApi;

import com.github.mrxxy.notchtools.core.AbsNotchScreenSupport;
import com.github.mrxxy.notchtools.core.OnNotchCallBack;
import com.github.mrxxy.notchtools.helper.NotchStatusBarUtils;

/**
 * https://open.oppomobile.com/service/message/detail?id=61876
 */
public class OppoNotchScreen extends AbsNotchScreenSupport {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isNotchScreen(Window window) {
        if (window == null) {
            return false;
        }
        return window.getContext().getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getNotchHeight(Window window) {
        if (!isNotchScreen(window)) {
            return 0;
        }

        return NotchStatusBarUtils.getStatusBarHeight(window.getContext());
    }

    /**
     * oppo手机，如果想全屏但不使用状态栏内容，需要加一个fake的假刘海
     *
     * @param activity
     * @param notchCallBack
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenNonUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenNonUseStatus(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            NotchStatusBarUtils.setFakeNotchView(activity.getWindow());
        }
    }

    /**
     * 竖屏下与fullScreenDontUseStatus保持一致
     *
     * @param activity
     * @param notchCallBack
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenNonUseStatusForPortrait(Activity activity, OnNotchCallBack notchCallBack) {
        fullScreenNonUseStatus(activity, notchCallBack);
    }

    /**
     * 横屏下需要把NotchStatusBar隐藏掉，否则有可能会出现横屏上方有条黑边
     *
     * @param activity
     * @param notchCallBack
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void fullScreenDonUseStatusForLandscape(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenDonUseStatusForLandscape(activity, notchCallBack);
        if (isNotchScreen(activity.getWindow())) {
            NotchStatusBarUtils.removeFakeNotchView(activity.getWindow());
        }
    }

    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenUseStatus(activity, notchCallBack);
    }

}
