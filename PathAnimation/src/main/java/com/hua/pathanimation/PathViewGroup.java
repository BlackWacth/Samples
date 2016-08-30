package com.hua.pathanimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hzw on 2016/8/30.
 */
public class PathViewGroup extends ViewGroup {

    public static final String tag = "hzw";
    public static final long duration = 2000;
    public static final int icons[] = new int[]{
            R.mipmap.ic_main_camera,
            R.mipmap.ic_main_selfie,
            R.mipmap.ic_main_antenna,
            R.mipmap.ic_main_fast_fill,
            R.mipmap.ic_main_fingerprint,
            R.mipmap.ic_main_more
    };

    private long mBlockDuration;
    private Bitmap[] mIconBitmaps;
    private IconView[] mIconViews;
    private Paint mPaint;
    private float mPathLength;
    private float mLength;
    private Path mPath;
    private RectF mRectF;
    private PathMeasure mPathMeasure;


    public PathViewGroup(Context context) {
        super(context);
        init(context);
    }

    public PathViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PathViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PathViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);

        mPath = new Path();
        mRectF = new RectF(-320, 300, 780, 1400);
        mPath.addArc(mRectF, 90, -180);

        mPathMeasure = new PathMeasure(mPath, false);
        mPathLength = mPathMeasure.getLength();
        mLength = mPathLength / icons.length;
        mBlockDuration = duration / icons.length;

        mIconBitmaps = new Bitmap[icons.length];
        mIconViews = new IconView[icons.length];
        long delay = 0;
        for (int i = 0; i < icons.length; i++) {
            mIconBitmaps[i] = BitmapFactory.decodeResource(getResources(), icons[i]);

            IconView iconView = new IconView(context);
            iconView.setBitmap(mIconBitmaps[i]);
            iconView.setPath(mPath);
            iconView.setPathMeasure(mPathMeasure);
            long dur = duration - mBlockDuration * i;
            iconView.setDuration(dur);
            float len = mPathLength - mLength * i;
            iconView.setPathLength(len);
            delay += mBlockDuration * i;
            iconView.setDelayMillis(delay);

            mIconViews[i] = iconView;
            addView(iconView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(tag, "onLayout");
        int childCount = this.getChildCount();
        for(int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            int halfWidth = mIconBitmaps[i].getWidth() / 2;
            int halfHeight = mIconBitmaps[i].getHeight() / 2;
            child.layout(230 - halfWidth, 1400 - halfHeight, 230 + halfWidth, 1400 + halfHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(tag, "onMeasure");
        int rw = MeasureSpec.getSize(widthMeasureSpec);
        int rh = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(tag, "rw = " + rw);
        Log.i(tag, "rh = " + rh);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int cWidth;
        int cHeight;
        int childCount = this.getChildCount();
        for(int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            cWidth = child.getMeasuredWidth();
            cHeight = child.getMeasuredHeight();

            Log.i(tag, "cWidth = " + cWidth);
            Log.i(tag, "cHeight = " + cHeight);
        }
        setMeasuredDimension(rw, rh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    public void startAnimation() {
        for(int i = 0; i < icons.length; i++) {
            mIconViews[i].startAnimator();
        }
    }
}
