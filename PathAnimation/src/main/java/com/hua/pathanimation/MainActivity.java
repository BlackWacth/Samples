package com.hua.pathanimation;

import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static final String tag = "MainActivity";
    public static final long totalDuration = 1000;

    private ImageView[] mIconImages = new ImageView[6];
    private ViewHolder[] mIconHolders = new ViewHolder[6];

    private PathMeasure mPathMeasure;
    private float mPathLength;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        mIconImages[0] = (ImageView) findViewById(R.id.iv_main_camera);
        mIconImages[1] = (ImageView) findViewById(R.id.iv_main_selfie);
        mIconImages[2] = (ImageView) findViewById(R.id.iv_main_antenna);
        mIconImages[3] = (ImageView) findViewById(R.id.iv_main_fast_fill);
        mIconImages[4] = (ImageView) findViewById(R.id.iv_main_fingerprint);
        mIconImages[5] = (ImageView) findViewById(R.id.iv_main_more);

        findViewById(R.id.rl_main_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimatorAll();
            }
        });

        mPathMeasure = createPathMeasure();
        mPathLength = mPathMeasure.getLength();
        float blockLength = mPathLength / (mIconImages.length - 1);
        long blockDuration = totalDuration / mIconImages.length;

        float tempLength = 0;
        long tempDuration = 0;
        long tempDelay = 0;
        for(int i = 0; i < mIconHolders.length; i++) {
            tempLength = mPathLength - i * blockLength;
            tempDuration = totalDuration - i * blockDuration;
            tempDelay = i * blockDuration;
            mIconHolders[i] = new ViewHolder(mIconImages[i], tempLength, tempDuration, tempDelay);
        }
    }

    private PathMeasure createPathMeasure() {
        Path path = new Path();
        RectF rectF = new RectF(-320, 300, 780, 1400);
        path.addArc(rectF, 90, -180);
        return new PathMeasure(path, false);
    }

    private void startAnimator(final ImageView view, float length, long duration) {
        Log.i(tag, "width = " + view.getWidth());
        Log.i(tag, "height = " + view.getHeight());
        final float halfWidth = view.getWidth() / 2;
        final float halfHeight = view.getHeight() / 2;
        ValueAnimator animator = ValueAnimator.ofFloat(0, length);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                float[] currentPoint = new float[2];
                mPathMeasure.getPosTan(distance, currentPoint, null);
                view.setX(currentPoint[0] - halfWidth);
                view.setY(currentPoint[1] - halfHeight);
            }
        });
        animator.start();
    }

    private void startAnimatorDelay(final ImageView view, final float length, final long duration, long delay) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimator(view, length, duration);
            }
        }, delay);
    }

    private void startAnimatorAll() {
        for (ViewHolder holder : mIconHolders) {
            startAnimatorDelay(holder.mImageView, holder.mLength, holder.mDuration, holder.mDelay);
        }
    }

    class ViewHolder {
        ImageView mImageView;
        float mLength;
        long mDuration;
        long mDelay;

        public ViewHolder(ImageView imageView, float length, long duration, long delay) {
            mImageView = imageView;
            mLength = length;
            mDuration = duration;
            mDelay = delay;
        }
    }
}
