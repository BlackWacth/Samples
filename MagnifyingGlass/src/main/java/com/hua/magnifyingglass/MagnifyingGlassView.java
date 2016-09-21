package com.hua.magnifyingglass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hzw on 2016/9/13.
 */
public class MagnifyingGlassView extends View{

    private int mFactor = 3;
    private Bitmap mBgBitmap;
    private Bitmap mGlassBitmap;
    private Matrix mMatrix;
    private Path mPath;

    private float mRadius;
    private float mCurrentX, mCurrentY;
    private ShapeDrawable mShapeDrawable;
    private int left, top;

    public MagnifyingGlassView(Context context) {
        this(context, null);
    }

    public MagnifyingGlassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_antenna_home_phone);
        mBgBitmap = bitmap;
        mGlassBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_antenna_glass);

//        mPath = new Path();
        mRadius = Math.min(mGlassBitmap.getWidth(), mGlassBitmap.getHeight()) / 2.0f;
//        mPath.addCircle(mGlassBitmap.getWidth(), mGlassBitmap.getHeight(), mRadius, Path.Direction.CW);
//
        mMatrix = new Matrix();
//        mMatrix.setScale(mFactor, mFactor);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mBgBitmap, mBgBitmap.getWidth() * mFactor, mBgBitmap.getHeight() * mFactor, true);
        BitmapShader shader = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mShapeDrawable = new ShapeDrawable(new OvalShape());
        mShapeDrawable.getPaint().setShader(shader);
        mShapeDrawable.setBounds(0, 0, (int)mRadius * 2, (int)mRadius * 2);
        left = mGlassBitmap.getWidth() / 2;
        top = mGlassBitmap.getHeight() / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        mCurrentX = event.getX();
//        mCurrentY = event.getY();

        int x = (int) event.getX();
        int y = (int) event.getY();

        mMatrix.setTranslate(mRadius * 2 - x * mFactor, mRadius * 2 - y * mFactor);
        mShapeDrawable.getPaint().getShader().setLocalMatrix(mMatrix);
        mShapeDrawable.setBounds((int)(mCurrentX - mRadius), (int)(mCurrentY - mRadius), (int)(mCurrentX + mRadius), (int)(mCurrentY + mRadius));
        left = x - mGlassBitmap.getWidth() / 2;
        top = y - mGlassBitmap.getHeight() / 2;
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBgBitmap, 0, 0, null);
        canvas.drawBitmap(mGlassBitmap, left, top, null);
//        canvas.drawBitmap(mGlassBitmap, mCurrentX - mRadius, mCurrentY - mRadius, null);
//        canvas.translate(mCurrentX - mRadius * 2, mCurrentY - mRadius * 2);
//        canvas.clipPath(mPath);
//
//        canvas.translate(mRadius * 2 - mCurrentX * mFactor, mRadius * 2 - mCurrentY * mFactor);
//        canvas.drawBitmap(mBgBitmap, mMatrix, null);

        mShapeDrawable.draw(canvas);
    }
}
