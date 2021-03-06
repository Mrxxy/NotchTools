package com.github.mrxxy.notchtools.phone;


import android.app.Activity;
import android.view.Window;

import com.github.mrxxy.notchtools.core.AbsNotchScreenSupport;
import com.github.mrxxy.notchtools.core.OnNotchCallBack;


public class CommonScreen extends AbsNotchScreenSupport {

    @Override
    public boolean isNotchScreen(Window window) {
        return false;
    }

    @Override
    public int getNotchHeight(Window window) {
        return 0;
    }

    @Override
    public void fullScreenNonUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenNonUseStatus(activity, notchCallBack);
    }

    @Override
    public void fullScreenUseStatus(Activity activity, OnNotchCallBack notchCallBack) {
        super.fullScreenUseStatus(activity, notchCallBack);
    }

}
