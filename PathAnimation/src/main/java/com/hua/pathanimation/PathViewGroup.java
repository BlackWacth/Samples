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
import android.view.View;

/**
 * Created by hzw on 2016/8/30.
 */
public class PathViewGroup extends View {

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
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
        for(int i = 0; i < icons.length; i++) {
            mIconViews[i].draw(canvas);
        }
    }

    public void startAnimation() {
        for(int i = 0; i < icons.length; i++) {
            mIconViews[i].startAnimator();
        }
    }
}
