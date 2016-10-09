package com.hua.themeswitch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String tag = "MainActivity";
    private static final String key_theme = "pref_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences sharedPreferences = getSharedPreferences("theme_switch", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getBoolean(key_theme, true)) {
            setTheme(R.style.MTheme_Light);
        } else {
            setTheme(R.style.MTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch mSwitch = (Switch) findViewById(R.id.switch_theme);
        mSwitch.setChecked(sharedPreferences.getBoolean(key_theme, true));
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(key_theme, isChecked);
                editor.commit();
            }
        });
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void restartActivity() {
        overridePendingTransition(0, 0);
        finish();
        final Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(tag, "key = " + key);
        if(key.equals(key_theme)) {
            Log.i(tag, "switch-----");
            restartActivity();
        }
    }
}
