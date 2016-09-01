package com.hua.pathanimation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public static final String tag = "IconView";
    private Bitmap mBitmap;
    private Paint mPaint;
    private long mDuration;
    private float mPathLength;
    private PathMeasure mPathMeasure;
    private Rect srcRect;
    private RectF dstRect;
    private long delayMillis = 0;
    private float width, halfWidth;
    private float height, halfHeight;

    public IconView(Context context) {
        this(context, null);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.icon);
        int srcId = typedArray.getResourceId(R.styleable.icon_src, R.mipmap.ic_main_camera);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), srcId);
        setBitmap(bitmap);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, srcRect, dstRect, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int)width, (int)height);
    }

    public void startAnimator() {
        Log.i(tag, "icon --- startAnimator()");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mPathMeasure == null) {
                    return ;
                }
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
                animator.start();
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

    public RectF getDstRect() {
        return dstRect;
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
        if(srcRect == null) {
            srcRect = new Rect(0, 0, (int)width, (int)height);
        }
        if(dstRect == null) {
            dstRect = new RectF(0, 0, width, height);
        }
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
}
