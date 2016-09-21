package com.hua.magnifyingglass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 * 放大镜
 * Created by hzw on 2016/9/13.
 */
public class MagnifyingGlassByPathView extends View {

    private int mFactor = 3;
    private Bitmap mBgBitmap;
    private Bitmap mGlassBitmap;
    private Matrix mMatrix;
    private Path mPath;

    private float mRadius;
    private float mCurrentX;
    private float mCurrentY = 218;
    private boolean isShowGlass = true;

    public MagnifyingGlassByPathView(Context context) {
        this(context, null);
    }

    public MagnifyingGlassByPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_antenna_home_phone);
        mGlassBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_antenna_glass);

        mPath = new Path();
        mRadius = Math.min(mGlassBitmap.getWidth(), mGlassBitmap.getHeight()) / 2.0f - 60;
        mPath.addCircle(mGlassBitmap.getWidth(), mGlassBitmap.getHeight(), mRadius, Path.Direction.CW);

        mMatrix = new Matrix();
        mMatrix.setScale(mFactor, mFactor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBgBitmap, 0, 0, null);
        if(isShowGlass) {
            canvas.save();
            canvas.translate(mCurrentX - mRadius * 2 - 55, mCurrentY - mRadius * 2 - 65);
            canvas.clipPath(mPath);
            canvas.translate(mRadius - mCurrentX * mFactor, mRadius - mCurrentY * mFactor);
            canvas.drawBitmap(mBgBitmap, mMatrix, null);
            canvas.restore();
            canvas.drawBitmap(mGlassBitmap, mCurrentX - mRadius, mCurrentY - mRadius, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCurrentX = event.getX();
//        mCurrentY = event.getY();
        invalidate();
        return true;
    }

    public void showGlass() {
        isShowGlass = true;
        invalidate();
    }
}
