package com.github.mrxxy.notchtools;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.github.mrxxy.notchtools.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.fullscreen_usenotch).setOnClickListener(this);
        findViewById(R.id.fullscreen_nonotch).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.fullscreen_usenotch:
                intent.setClass(MainActivity.this, FullScreenUseNotchActivity.class);
                break;
            case R.id.fullscreen_nonotch:
                intent.setClass(MainActivity.this, FullScreenNoUseNotchActivity.class);
                break;
            default:
                break;
        }
        try {
            startActivity(intent);
        } catch (Exception e) {

        }
    }
}
