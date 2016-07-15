package com.hua.myapplication;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    /**
     * 屏幕自动旋转开关
     * @param isRotation
     */
    public void setScreenRotation(boolean isRotation) {
        Settings.System.putInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, isRotation ? 1 : 0);
    }

}
