package com.hua.rotate3d;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String tag = "hzw";

    private Button mRotate;
    private ImageView mImage;

    private Rotate3DAnimation mAnimation;

    private float centerX;
    private float centerY;
    private float depthZ;
    private long duration = 2000;
    private boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(tag, "onCreate");
        mRotate = (Button) findViewById(R.id.rotate);
        mRotate.setOnClickListener(this);
        mImage = (ImageView) findViewById(R.id.iv_img);

        centerX = mImage.getWidth() / 2;
        centerY = mImage.getHeight() / 2;
        Log.i(tag, "onCreate---centerX = " + centerX);
        Log.i(tag, "onCreate---centerY = " + centerY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(tag, "onStart");
        centerX = mImage.getWidth() / 2;
        centerY = mImage.getHeight() / 2;
        Log.i(tag, "onStart---centerX = " + centerX);
        Log.i(tag, "onStart---centerY = " + centerY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(tag, "onResume");
        centerX = mImage.getWidth() / 2;
        centerY = mImage.getHeight() / 2;
        Log.i(tag, "onResume---centerX = " + centerX);
        Log.i(tag, "onResume---centerY = " + centerY);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i(tag, "onWindowFocusChanged : " + hasFocus);
        if(hasFocus) {
            centerX = mImage.getWidth() / 2;
            centerY = mImage.getHeight() / 2;
            Log.i(tag, "centerX = " + centerX);
            Log.i(tag, "centerY = " + centerY);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(tag, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(tag, "onStop");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rotate:
//                centerX = mImage.getWidth() / 2;
//                centerY = mImage.getHeight() / 2;
//
//                Log.i(tag, "centerX = " + centerX);
//                Log.i(tag, "centerY = " + centerY);

                if(mAnimation == null) {
                    obtainAnimation();
                }
                if(isRunning) {
                    return ;
                }
                mImage.startAnimation(mAnimation);
                return;
        }

    }

    /**
     * 注释
     */
    private void obtainAnimation() {
        mAnimation = new Rotate3DAnimation(0, 180, centerX, centerY, depthZ, false);
        mAnimation.setDuration(duration);
        mAnimation.setFillAfter(true);
        mAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
