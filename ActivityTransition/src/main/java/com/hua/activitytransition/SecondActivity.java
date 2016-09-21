package com.hua.activitytransition;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class SecondActivity extends AppCompatActivity {

    private RelativeLayout mContainer;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_second, null);
        setContentView(view);
        mContainer = (RelativeLayout) findViewById(R.id.second_container);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.second_float_btn);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setupEnterAnimation(); // 入场动画
////            setupExitAnimation(); // 退场动画
//        } else {
//            initViews();
//        }

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                animateRevealShow();
            }
        });
    }



    public void backActivity(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            onBackPressed();
        } else {
            defaultBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        GuiUtils.animateRevealHide(this, mContainer, 100, R.color.colorAccent, new GuiUtils.OnRevealAnimationListener() {
//            @Override
//            public void onRevealHide() {
//                defaultBackPressed();
//            }
//
//            @Override
//            public void onRevealShow() {
//
//            }
//        });

    }

    private void defaultBackPressed() {
        super.onBackPressed();
    }

    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.arc_motion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    private void animateRevealShow() {
        GuiUtils.animateRevealShow(this, mContainer, mFloatingActionButton.getWidth() / 2, R.color.teal400, new GuiUtils.OnRevealAnimationListener() {
            @Override
            public void onRevealHide() {

            }

            @Override
            public void onRevealShow() {
//                initViews();
            }
        });
    }

    private void setupExitAnimation() {
        Fade fade = new Fade();
        getWindow().setReenterTransition(fade);
        fade.setDuration(300);
    }

    private void initViews() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(SecondActivity.this, android.R.anim.fade_in);
                animation.setDuration(300);
                mContainer.startAnimation(animation);
            }
        });
    }
}
