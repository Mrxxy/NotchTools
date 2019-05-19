package com.github.mrxxy.notchtools.core;

import android.app.Activity;
import android.view.Window;

import com.github.mrxxy.notchtools.helper.NotchStatusBarUtils;


public abstract class AbsNotchScreenSupport implements INotchSupport {
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public int getStatusHeight(Window window) {
        return NotchStatusBarUtils.getStatusBarHeight(window.getContext());
    }

    @Override
    public void fullScreenNonUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        NotchStatusBarUtils.setFullScreenWithSystemUi(activity.getWindow(), false);
        onBindCallBackWithNotchProperty(activity, notchCallBack);
    }

    @Override
    public void fullScreenNonUseStatusForPortrait(Activity activity, OnNotchCallBack notchCallBack) {
        fullScreenNonUseStatus(activity, notchCallBack);
    }

    @Override
    public void fullScreenDonUseStatusForLandscape(Activity activity, OnNotchCallBack notchCallBack) {
        fullScreenNonUseStatus(activity, notchCallBack);
    }

    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        NotchStatusBarUtils.setFullScreenWithSystemUi(activity.getWindow(), false);
        onBindCallBackWithNotchProperty(activity, getNotchHeight(activity.getWindow()), notchCallBack);
    }

    protected void onBindCallBackWithNotchProperty(Activity activity, OnNotchCallBack notchCallBack) {
        if (notchCallBack != null) {
            NotchProperty notchProperty = new NotchProperty();
            notchProperty.setNotchHeight(getNotchHeight(activity.getWindow()));
            notchProperty.setNotch(isNotchScreen(activity.getWindow()));
            if (notchCallBack != null) {
                notchCallBack.onNotchPropertyCallback(notchProperty);
            }
        }
    }

    protected void onBindCallBackWithNotchProperty(Activity activity, int marginTop, OnNotchCallBack notchCallBack) {
        if (notchCallBack != null) {
            NotchProperty notchProperty = new NotchProperty();
            notchProperty.setNotchHeight(getNotchHeight(activity.getWindow()));
            notchProperty.setNotch(isNotchScreen(activity.getWindow()));
            notchProperty.setMarginTop(marginTop);
            if (notchCallBack != null) {
                notchCallBack.onNotchPropertyCallback(notchProperty);
            }
        }
    }
}
