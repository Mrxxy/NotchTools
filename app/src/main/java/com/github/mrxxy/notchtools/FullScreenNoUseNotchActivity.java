package com.github.mrxxy.notchtools;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.Mrxxy.notchtools.NotchTools;
import com.github.mrxxy.notchtools.R;


public class FullScreenNoUseNotchActivity extends BaseActivity {

    private ImageView mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_no_use_notch);
        mBackView = findViewById(R.id.img_back);
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        NotchTools.getFullScreenTools().fullScreenDontUseStatusForPortrait(this);
    }

    /**
     * onWindowFocusChanged最好也进行全屏适配，防止失去焦点又重回焦点时的flag不正确。
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            NotchTools.getFullScreenTools().fullScreenDontUseStatusForPortrait(this);
        }
        super.onWindowFocusChanged(hasFocus);
    }
}
