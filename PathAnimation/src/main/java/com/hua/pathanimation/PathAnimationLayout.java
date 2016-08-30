package com.hua.pathanimation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bruce on 2016/8/30.
 */
public class PathAnimationLayout extends ViewGroup {

    public static final String tag = "PathAnimationLayout";

    public PathAnimationLayout(Context context) {
        this(context, null);
    }

    public PathAnimationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathAnimationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PathView pathView = new PathView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        pathView.setLayoutParams(params);
        addView(pathView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.i(tag, "widthSize = " + widthSize);
        Log.i(tag, "heightSize = " + heightSize);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int childCount = getChildCount();
        Log.i(tag, "childCount = " + childCount);
        View child = getChildAt(0);
        LayoutParams params = child.getLayoutParams();
        Log.i(tag, "params.width = " + params.width);
        Log.i(tag, "params.height = " + params.height);
        int measuredWidth = child.getMeasuredWidth();
        int measuredHeight = child.getMeasuredHeight();

        Log.i(tag, "measuredWidth = " + measuredWidth);
        Log.i(tag, "measuredHeight = " + measuredHeight);

        child.layout(0, 0, measuredWidth, measuredHeight);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
