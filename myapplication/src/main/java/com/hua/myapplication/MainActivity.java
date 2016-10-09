package com.hua.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canWrite();
//                checkPermission();
            }
        });
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_DENIED) {
            Log.i(tag, "--- no WRITE_SETTINGS ----");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, 555);
        } else {
            Log.i(tag, "--- have WRITE_SETTINGS ----");
            initSettings();
        }
    }

    /**
     * 6.0对WRITE_SETTINGS的权限申请方式。
     */
    private void canWrite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                initSettings();
            }else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 555) {
            Log.i(tag, "--- callback WRITE_SETTINGS ----");
            for (String str : permissions) {
                Log.i(tag, "permission : " + str);
            }

            for (int code : grantResults) {
                Log.i(tag, "result : " + code);
            }

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(tag, "--- get WRITE_SETTINGS ----");
                initSettings();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initSettings() {
        setScreenOffTime(this, 60 * 10);
        changeBrightness(this);
    }

    /**
     * 设置系统休眠时间
     *
     * @param context
     *            上下文
     * @param second
     *            休眠时间，单位秒
     */
    public static void setScreenOffTime(Context context, int second) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, second * 1000);
    }


    /**
     * 改变屏幕亮度,改为最亮
     */
    public static void changeBrightness(Context context) {
        try {
            //先关闭自动亮度
            android.provider.Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            //调节亮度 255最亮
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
        } catch (Exception e) {
            Log.e("屏幕亮度改变失败", e.getMessage());
        }
    }
}
