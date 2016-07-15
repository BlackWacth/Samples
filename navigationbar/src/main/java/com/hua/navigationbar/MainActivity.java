package com.hua.navigationbar;

import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View mRootView;
    private int mState;

    private ActionBar mActionBar;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(mRootView);
        mActionBar = getSupportActionBar();
        findViewById(R.id.btn_visible).setOnClickListener(this);
        findViewById(R.id.btn_invisible).setOnClickListener(this);
        findViewById(R.id.btn_fullscreen).setOnClickListener(this);
        findViewById(R.id.btn_layout_fullscreen).setOnClickListener(this);
        findViewById(R.id.btn_layout_hide_navigation).setOnClickListener(this);
        findViewById(R.id.btn_ui_layout_flags).setOnClickListener(this);
        findViewById(R.id.btn_hide_navigation).setOnClickListener(this);
        findViewById(R.id.btn_low_profile).setOnClickListener(this);
        findViewById(R.id.btn_show).setOnClickListener(this);
        findViewById(R.id.btn_hide).setOnClickListener(this);
        findViewById(R.id.btn_enable_navbar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_visible:
                mState = View.SYSTEM_UI_FLAG_VISIBLE;
                mTitle = "SYSTEM_UI_FLAG_VISIBLE";
                break;

            case R.id.btn_invisible:
                mState = View.INVISIBLE;
                mTitle = "INVISIBLE";
                break;

            case R.id.btn_fullscreen:
                mState = View.SYSTEM_UI_FLAG_FULLSCREEN;
                mTitle = "SYSTEM_UI_FLAG_FULLSCREEN";
                break;

            case R.id.btn_layout_fullscreen:
                mState = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                mTitle = "SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN";
                break;

            case R.id.btn_layout_hide_navigation:
                mState = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                mTitle = "SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION";
                break;

            case R.id.btn_ui_layout_flags:
                mState = View.SYSTEM_UI_LAYOUT_FLAGS;
                mTitle = "SYSTEM_UI_LAYOUT_FLAGS";
                break;

            case R.id.btn_hide_navigation:
                mState = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                mTitle = "SYSTEM_UI_FLAG_HIDE_NAVIGATION";
                break;

            case R.id.btn_low_profile:
                mState = View.SYSTEM_UI_FLAG_LOW_PROFILE;
                mTitle = "SYSTEM_UI_FLAG_LOW_PROFILE";
                break;

            case R.id.btn_show:
                mState =
                        View.SYSTEM_UI_FLAG_VISIBLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                mTitle = "Show";
                break;

            case R.id.btn_hide:
                mState = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
                mTitle = "Hide";
                break;

            case R.id.btn_enable_navbar:
                Settings.System.putInt(getContentResolver(), "enable_navbar", 1);
                mTitle = "Enable Navbar";
                break;

        }
        mRootView.setSystemUiVisibility(mState);
        mActionBar.setTitle(mTitle);
    }
}
