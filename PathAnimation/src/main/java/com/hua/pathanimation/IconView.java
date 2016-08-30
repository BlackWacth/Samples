package com.hua.pathanimation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
 * 单个图标对象
 * Created by hzw on 2016/8/30.
 */
public class IconView extends View{

    public static final String tag = "hzw";
    private Bitmap mBitmap;
    private Paint mPaint;
    private long mDuration;
    private float mPathLength;
    private Path mPath;
    private PathMeasure mPathMeasure;
    private Rect srcRect;
    private RectF dstRect;
    private long delayMillis = 0;
    private float width, halfWidth;
    private float height, halfHeight;

    public IconView(Context context) {
        super(context);
        init();
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        srcRect = new Rect();
        dstRect = new RectF();
        mPath = new Path();
        mPathMeasure = new PathMeasure();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, srcRect, dstRect, mPaint);
    }

    public void startAnimator() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofFloat(0, mPathLength);
                animator.setDuration(mDuration);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float distance = (float) animation.getAnimatedValue();
                        Log.i(tag, "distance : " + distance);
                        float[] currentPoint = new float[2];
                        mPathMeasure.getPosTan(distance, currentPoint, null);
                        dstRect.left = (int) (currentPoint[0] - halfWidth);
                        dstRect.top = (int) (currentPoint[1] - halfHeight);
                        dstRect.right = (int) (currentPoint[0] + halfWidth);
                        dstRect.bottom = (int) (currentPoint[1] + halfHeight);
                        postInvalidate();
                    }
                });
            }
        }, delayMillis);
    }

    public PathMeasure getPathMeasure() {
        return mPathMeasure;
    }

    public void setPathMeasure(PathMeasure pathMeasure) {
        mPathMeasure = pathMeasure;
        mPathLength = mPathMeasure.getLength();
    }

    public long getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public Rect getSrcRect() {
        return srcRect;
    }

    public void setSrcRect(Rect srcRect) {
        this.srcRect = srcRect;
    }

    public RectF getDstRect() {
        return dstRect;
    }

    public void setDstRect(RectF dstRect) {
        this.dstRect = dstRect;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        width = mBitmap.getWidth();
        halfWidth = width / 2;
        height = mBitmap.getHeight();
        halfHeight = height / 2;
        srcRect.left = 0;
        srcRect.top = 0;
        srcRect.right = (int) width;
        srcRect.bottom = (int) height;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public float getPathLength() {
        return mPathLength;
    }

    public void setPathLength(float pathLength) {
        mPathLength = pathLength;
    }

    public Path getPath() {
        return mPath;
    }

    public void setPath(Path path) {
        mPath = path;
        mPathMeasure.setPath(mPath, false);
    }
}
