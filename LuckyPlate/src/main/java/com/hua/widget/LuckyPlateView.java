package com.hua.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hua.R;

/**
 * Created by 华忠伟 on 2016/6/20.
 */
public class LuckyPlateView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    private SurfaceHolder mHolder;

    private Canvas mCanvas;

    private Thread mThread;

    private boolean isRunning;

    /**
     * 抽奖的文字
     */
    private String[] mStrs = new String[] { "单反相机", "IPAD", "恭喜发财", "IPHONE",
            "妹子一只", "恭喜发财" };
    /**
     * 每个盘块的颜色
     */
    private int[] mColors = new int[] { 0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01 };
    /**
     * 与文字对应的图片
     */
    private int[] mImgs = new int[] {
            R.mipmap.danfan, R.mipmap.ipad,
            R.mipmap.f040, R.mipmap.iphone,
            R.mipmap.meizi, R.mipmap.f040 };

    private Bitmap[] mIcons;

    private int mItemCount = 6;

    private RectF mRange = new RectF();

    private int mRadius;

    private Paint mArcPaint;

    private Paint mTextPaint;

    private float mSpeed;

    private volatile float mStartAngle = 0;

    private boolean isShouldEnd;

    private int mCenter;

    private int mPadding;

    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg2);

    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    public LuckyPlateView(Context context) {
        this(context, null);
    }

    public LuckyPlateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);

        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPadding = getPaddingLeft() + getPaddingRight();
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = width - mPadding;
        mCenter = width / 2;
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mTextSize);

        mRange = new RectF(getPaddingLeft(), getPaddingTop(), mRadius + getPaddingLeft(), mRadius + getPaddingTop());
        mItemCount = mImgs.length;
        mIcons = new Bitmap[mItemCount];
        for(int i = 0; i < mItemCount; i++) {
            mIcons[i] = BitmapFactory.decodeResource(getResources(), mImgs[i]);
        }

        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            if(end - start < 50) {
                try {
                    Thread.sleep(50 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if(mCanvas == null) {
                return;
            }
            drawBackground();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void drawBackground() {
        Rect destRect = new Rect(mPadding / 2, mPadding /2, getMeasuredWidth() - mPadding / 2, getMeasuredHeight() - mPadding / 2);
        mCanvas.drawBitmap(mBgBitmap, null, destRect, null);
    }
}
