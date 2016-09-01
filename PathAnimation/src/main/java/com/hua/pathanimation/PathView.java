package com.hua.pathanimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

/**
 *
 * Created by Bruce on 2016/8/30.
 */
public class PathView extends View {
    public static final String tag = "hzw";
    private Paint mPaint;
    private float mPathLength;
    private Path mPath;
    private RectF mRectF;
    private PathMeasure mPathMeasure;

    public PathView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);

        mPath = new Path();
        mRectF = new RectF(-320, 300, 780, 1400);
        mPath.addArc(mRectF, 90, -180);

        mPathMeasure = new PathMeasure(mPath, false);
        mPathLength = mPathMeasure.getLength();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(tag, "PathView --> onDraw");
        canvas.drawPath(mPath, mPaint);
    }

    public Paint getPaint() {
        return mPaint;
    }

    public float getPathLength() {
        return mPathLength;
    }

    public Path getPath() {
        return mPath;
    }

    public RectF getRectF() {
        return mRectF;
    }

    public PathMeasure getPathMeasure() {
        return mPathMeasure;
    }
}
