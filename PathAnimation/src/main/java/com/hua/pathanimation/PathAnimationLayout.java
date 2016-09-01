package com.hua.pathanimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * 路径动画容器控件
 * Created by Bruce on 2016/8/30.
 */
public class PathAnimationLayout extends ViewGroup {

    public static final String tag = "PathAnimationLayout";

    public static final int ICONS[] = new int[]{
            R.mipmap.ic_main_camera,
            R.mipmap.ic_main_selfie,
            R.mipmap.ic_main_antenna,
            R.mipmap.ic_main_fast_fill,
            R.mipmap.ic_main_fingerprint,
            R.mipmap.ic_main_more
    };

    /** 容器圆半径 */
    private int radius = 550;
    /** 容器圆心 X轴坐标 */
    private int circleX = 230;
    /** 容器圆心 Y轴坐标*/
    private int circleY = 750;

    private float startAngle = 90f;
    private float sweepAngele = -180f;

    private PathMeasure mPathMeasure;
    private float mPathLength;

    private Bitmap[] mBitmaps;

    private long mTotalDuration = 1000;

    private IconView[] mIconViews;
    private float blockLength;
    private long blockDuration;

    public PathAnimationLayout(Context context) {
        this(context, null);
    }

    public PathAnimationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathAnimationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //路径
        mPathMeasure = createPathMeasure();
        mPathLength = mPathMeasure.getLength();

        blockLength = mPathLength / (ICONS.length - 1);
        blockDuration = mTotalDuration / ICONS.length;
    }

    public void startAnimation() {
        Log.i(tag, "layout --- startAnimation() : " + mIconViews.length);
        for(IconView iconView : mIconViews) {
            iconView.startAnimator();
        }
    }

    private IconView[] initAllIcon(PathMeasure pathMeasure, float blockLength, long blockDuration) {
        int childCount = getChildCount();
        Log.i(tag, "initAllIcon : childCount = " + childCount);
        IconView[] iconViews = new IconView[childCount];
        for(int i = 0; i < childCount; i++) {
            iconViews[i] = initIcon(pathMeasure, i, blockLength, blockDuration);
        }
        return iconViews;
    }

    private IconView initIcon(PathMeasure pathMeasure, int index, float blockLength, long blockDuration) {
        View childView = getChildAt(index);
        IconView iconView = null;
        if(childView instanceof IconView) {
            iconView = (IconView) childView;
        }
        float length = mPathLength - index * blockLength;
        long duration = mTotalDuration - index * blockDuration;
        long delay = index * blockDuration;
        iconView.setPathMeasure(pathMeasure);
        iconView.setPathLength(length);
        iconView.setDelayMillis(delay);
        iconView.setDuration(duration);
        return iconView;
    }

    private PathMeasure createPathMeasure() {
        Path path = new Path();
        RectF rectF = new RectF(-(radius - circleX), 0, radius + circleX, radius * 2);
        path.addArc(rectF, startAngle, sweepAngele);
        return new PathMeasure(path, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        //测量所有子控件
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        int childWidth = 0;
        int childHeight = 0;

        for(int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childWidth = Math.max(childWidth, childView.getMeasuredWidth());
            childHeight = Math.max(childHeight, childView.getMeasuredHeight());
        }

        int max = Math.max(childWidth, childHeight);

        int maxWidth = circleX + radius + max / 2;
        int maxHeight = radius * 2 + max;

        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize : maxWidth, (heightMode == MeasureSpec.EXACTLY) ? heightSize : maxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        for(int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int cw = childView.getMeasuredWidth();
            int ch = childView.getMeasuredHeight();
            childView.layout(0, 0, cw, ch);
        }

        mIconViews = initAllIcon(mPathMeasure, blockLength, blockDuration);
    }
}
