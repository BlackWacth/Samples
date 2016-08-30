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

    private float mPathLength;
    private float mLength;
    private PathMeasure mPathMeasure;
    private PathView mPathView;


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

    private void init(Context context) {

        mPathView = new PathView(context);
        addView(mPathView);

        mPathMeasure = mPathView.getPathMeasure();
        mPathLength = mPathView.getPathLength();

        mLength = mPathLength / icons.length;
        mBlockDuration = duration / icons.length;

        mIconBitmaps = new Bitmap[icons.length];
        mIconViews = new IconView[icons.length];

        long delay = 0;
        Bitmap bitmap;
        for (int i = 0; i < icons.length; i++) {
            bitmap = BitmapFactory.decodeResource(getResources(), icons[i]);

            IconView iconView = new IconView(context);
            iconView.setBitmap(bitmap);
            iconView.setPathMeasure(mPathMeasure);
            long dur = duration - mBlockDuration * i;
            iconView.setDuration(dur);
            float len = mPathLength - mLength * i;
            iconView.setPathLength(len);
            delay += mBlockDuration * i;
            iconView.setDelayMillis(delay);
            mIconBitmaps[i] = bitmap;
            mIconViews[i] = iconView;
            addView(iconView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(tag, "onLayout");
        int childCount = this.getChildCount();

        //路径
        View pathView = getChildAt(0);
        LayoutParams pathParams = pathView.getLayoutParams();
        pathView.layout(0, 0, pathParams.width, pathParams.height);

        //图标
        for(int i = 1; i < childCount; i++) {
            View child = this.getChildAt(i);
            int halfWidth = mIconBitmaps[i-1].getWidth() / 2;
            int halfHeight = mIconBitmaps[i-1].getHeight() / 2;
            child.layout(230 - halfWidth, 1400 - halfHeight, 230 + halfWidth, 1400 + halfHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(tag, "onMeasure");

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int cWidth;
        int cHeight;
        int childCount = this.getChildCount();
        for(int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);

            cWidth = child.getMeasuredWidth();
            cHeight = child.getMeasuredHeight();

//            Log.i(tag, "cWidth = " + cWidth);
//            Log.i(tag, "cHeight = " + cHeight);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    public void startAnimation() {
        for(int i = 0; i < icons.length; i++) {
            mIconViews[i].startAnimator();
        }
    }

}
