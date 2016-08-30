package com.hua.pathanimation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by hzw on 2016/8/30.
 */
public class PathView extends View{

    public static final String tag = "hzw";
    public static final int ICON[] = new int[]{
            R.mipmap.ic_main_camera,
            R.mipmap.ic_main_selfie,
            R.mipmap.ic_main_antenna,
            R.mipmap.ic_main_fast_fill,
            R.mipmap.ic_main_fingerprint,
            R.mipmap.ic_main_more
    };



    private Paint mPathPaint;
    private Path mPath;
    private PathMeasure mPathMeasure;
    private float mPathLength;
    private float[] mCurrentPoint = new float[2];
    private RectF mRectF;
    private Bitmap[] mIconBitmap;
    private Rect srcRect, dstRect;
    private int width, halfWidth;
    private int height, halfHeight;

    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(3);
        mPathPaint.setColor(Color.RED);

        mPath = new Path();
        getWidth();
        mRectF = new RectF(-320, 300, 780, 1400);
        mPath.addArc(mRectF, 90, -180);


        mPathMeasure = new PathMeasure(mPath, false);
        mPathLength = mPathMeasure.getLength();
        Log.i(tag, "pathLength : " + mPathLength);
        mIconBitmap = new Bitmap[ICON.length];
        for (int i = 0; i < ICON.length; i++) {
            mIconBitmap[i] = BitmapFactory.decodeResource(getResources(), ICON[i]);
        }
        width = mIconBitmap[0].getWidth();
        halfWidth = width / 2;
        height = mIconBitmap[0].getWidth();
        halfHeight = height / 2;
        srcRect = new Rect(0, 0, width, height);
        dstRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPathPaint);
        dstRect.left = (int) (mCurrentPoint[0] - halfWidth);
        dstRect.top = (int) (mCurrentPoint[1] - halfHeight);
        dstRect.right = (int) (mCurrentPoint[0] + halfWidth);
        dstRect.bottom = (int) (mCurrentPoint[1] + halfHeight);
        canvas.drawBitmap(mIconBitmap[0], srcRect, dstRect, mPathPaint);
    }

    public void startAnimation(float length, int duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, length);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                Log.i(tag, "distance : " + distance);
                mPathMeasure.getPosTan(distance, mCurrentPoint, null);
                postInvalidate();
            }
        });
        animator.start();
    }

    public float getPathLength() {
        return mPathLength;
    }
}
