package com.hua.activitytransition;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton mFloatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_btn);
        mFloatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, mFloatingActionButton, mFloatingActionButton.getTransitionName());
            startActivity(new Intent(this, SecondActivity.class), options.toBundle());
        } else {
            startActivity(new Intent(this, SecondActivity.class));
        }
//        startActivity(new Intent(this, SecondActivity.class));
    }
}
