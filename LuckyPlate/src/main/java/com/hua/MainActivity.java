package com.hua;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_open).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_isOpen).setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.i("hzw", "----------------");
//            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("hzw", ">>>>>>>>>");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open:
                setTouchMode(true);
                toast("Open");
                break;

            case R.id.btn_close:
                setTouchMode(false);
                toast("Close");
                break;

            case R.id.btn_isOpen:
                toast(isOpenTouchMode(this) + "");
                break;
        }
    }

    public void setTouchMode(boolean isChecked) {
        Settings.System.putInt(getContentResolver(), "touch_disable_mode", isChecked ? 1 : 0);
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public static boolean isOpenTouchMode(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "touch_disable_mode", 0) == 1 ? true : false;
    }
}

