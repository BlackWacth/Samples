package com.hua.scratchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by hzw on 2016/9/5.
 */
public class ScratchView extends View {

    private static final String tag = "ScratchView";

    /**
     * 最小的橡皮擦尺寸大小
     */
    private final static float DEFAULT_ERASER_SIZE = 60f;
    /**
     * 默认蒙板的颜色
     */
    private final static int DEFAULT_MASKER_COLOR = 0xffcccccc;
    /**
     * 默认擦除比例
     */
    private final static int DEFAULT_MIN_PERCENT = 80;
    /**
     * 最大擦除比例
     */
    private final static int DEFAULT_MAX_PERCENT = 100;

    /** 遮罩颜色 */
    private int mMaskColor = DEFAULT_MASKER_COLOR;
    /** 橡皮擦大小 */
    private float mEraseSize = DEFAULT_ERASER_SIZE;
    /** 最小擦除比例 */
    private int mMinWipePercent = DEFAULT_MIN_PERCENT;
    /**水印画笔 */
    private Paint mWatermarkPaint;
    /** 遮罩画笔 */
    private Paint mMaskPaint;
    /** 橡皮擦画笔 */
    private Paint mErasePaint;
    /** 橡皮擦擦除路径 */
    private Path mErasePath;

    /**最小滑动距离 */
    private int mTouchSlop;

    /**遮罩层bitmap */
    private Bitmap mMaskBitmap;

    /**擦除效果起始点的x坐标 */
    private float mStartX;

    /**擦除效果起始点的y坐标 */
    private float mStartY;

    /**擦除是否完成 */
    private boolean isCompleted = false;

    /**当前擦除比例 */
    private int mCurrentPercent = 0;

    /** 遮罩层canvas*/
    private Canvas mMaskCanvas;

    /** 水印drawable*/
    private BitmapDrawable mWatermarkDrawable;

    /** 橡皮擦擦除状态监听器*/
    private EraseStatusListener mEraseStatusListener;

    public ScratchView(Context context) {
        super(context);
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.ScratchView);
        init(typedArray);
    }

    public ScratchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScratchView);
        init(typedArray);
    }

    public ScratchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScratchView, defStyleAttr, 0);
        init(typedArray);
    }

    private void init(TypedArray typedArray) {
        mMaskColor = typedArray.getColor(R.styleable.ScratchView_maskColor, DEFAULT_MASKER_COLOR);
        Log.i(tag, "MaskColor = " + mMaskColor);
        int watermarkResId = typedArray.getResourceId(R.styleable.ScratchView_watermark, -1);
        mEraseSize = typedArray.getFloat(R.styleable.ScratchView_eraseSize, DEFAULT_ERASER_SIZE);
        mMinWipePercent = typedArray.getInt(R.styleable.ScratchView_minWipePercent, DEFAULT_MIN_PERCENT);
        typedArray.recycle();

        //蒙层（遮罩）画笔
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskPaint.setDither(true);
        setMaskColor(mMaskColor);

        //水印画笔
        mWatermarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWatermarkPaint.setDither(true);
        setWatermark(watermarkResId);

        //设置橡皮擦画笔
        mErasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mErasePaint.setDither(true);
        mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); //设置擦除效果。
        mErasePaint.setStyle(Paint.Style.STROKE);
        mErasePaint.setStrokeCap(Paint.Cap.ROUND); //设置笔尖状态，让绘制圆滑
        setEraseSize(mEraseSize);

        mErasePath = new Path();

        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mTouchSlop =viewConfiguration.getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureSize(widthMeasureSpec);
        int height = measureSize(heightMeasureSpec);
        Log.i(tag, "width = " + width);
        Log.i(tag, "height = " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createMasker(w, h);
    }

    /**
     * 创建遮罩层
     */
    private void createMasker(int w, int h) {
        mMaskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mMaskCanvas = new Canvas(mMaskBitmap);
        Rect rect = new Rect(0, 0, w, h);
        mMaskCanvas.drawRect(rect, mMaskPaint);
        if(mWatermarkDrawable != null) {
            Rect bounds = new Rect(rect);
            mWatermarkDrawable.setBounds(bounds);
            mWatermarkDrawable.draw(mMaskCanvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mMaskBitmap, 0, 0, mMaskPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startErase(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                Log.i(tag, "ACTION_MOVE");
                movingErase(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_UP:
                stopErase();
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 准备开始擦除
     */
    private void startErase(float x, float y) {
        mErasePath.reset();
        mErasePath.moveTo(x, y);
        mStartX = x;
        mStartY = y;
    }

    /**
     * 移动橡皮擦进行擦除
     */
    private void movingErase(float x, float y) {
        int dx = (int) Math.abs(x - mStartX);
        int dy = (int) Math.abs(y - mStartY);
        if(dx >= mTouchSlop || dy >= mTouchSlop) {
            mStartX = x;
            mStartY = y;

            mErasePath.lineTo(x, y);
            mMaskCanvas.drawPath(mErasePath, mErasePaint);
            onErase();

            mErasePath.reset();
            mErasePath.moveTo(mStartX, mStartY);
        }
    }

    /**
     * 停在擦除
     */
    private void stopErase() {
        mStartX = 0;
        mStartY = 0;
        mErasePath.reset();
    }

    /**
     * 计算当前擦除像素个数，更新状态
     */
    private void onErase() {
        int width = getWidth();
        int height = getHeight();
        AsyncTask<Integer, Integer, Boolean> eraseTask = new AsyncTask<Integer, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Integer... params) {
                int w = params[0];
                int h = params[1];
                int pixels[] = new int[w * h];
                //获取覆盖图层中所有的像素信息，stride用于表示一行的像素个数有多少
                mMaskBitmap.getPixels(pixels, 0, w, 0, 0, w, h);
                float erasePixelCount = 0; //擦除的像素个数
                float totalPixelCount = w * h; //总像素个数
                for(int i = 0; i < totalPixelCount; i++) {
                    if(pixels[i] == 0) {
                        erasePixelCount ++;
                    }
                }

                int percent = 0;
                if(erasePixelCount >= 0 && totalPixelCount > 0) {
                    percent = Math.round(erasePixelCount * 100 / totalPixelCount);
                    publishProgress(percent);
                }
                return percent >= mMinWipePercent;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                mCurrentPercent = values[0];
                onPercentUpdate();
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if(result && !isCompleted) {
                    isCompleted = true;
                    if(mEraseStatusListener != null) {
                        mEraseStatusListener.onCompleted(ScratchView.this);
                    }
                }
            }
        };
        eraseTask.execute(width, height);
    }

    /**
     * 更新擦除进度
     */
    private void onPercentUpdate() {
        if(mEraseStatusListener != null) {
            mEraseStatusListener.onProgress(mCurrentPercent);
        }
    }

    /**
     * 测量
     * @param measureSpec
     * @return
     */
    private int measureSize(int measureSpec) {
        int size = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY) {
            size = specSize;
        } else if(specMode == MeasureSpec.AT_MOST) {
            size = Math.min(size, specSize);
        }
        return size;
    }

    /**
     * 设置遮罩层颜色
     * @param maskColor
     */
    public void setMaskColor(int maskColor) {
        this.mMaskColor = maskColor;
        mMaskPaint.setColor(mMaskColor);
    }

    public int getMaskColor() {
        return mMaskColor;
    }

    /**
     * 设置水印
     * @param watermark
     */
    public void setWatermark(int watermark) {
        if(watermark == -1) {
            mWatermarkDrawable = null;
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), watermark);
            mWatermarkDrawable = new BitmapDrawable(getResources(), bitmap);
            mWatermarkDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        }
    }

    public int getMinWipePercent() {
        return mMinWipePercent;
    }

    public void setMinWipePercent(int minWipePercent) {
        mMinWipePercent = minWipePercent;
    }

    public BitmapDrawable getWatermarkDrawable() {
        return mWatermarkDrawable;
    }

    /**
     * 重置
     */
    public void reset() {
        isCompleted = false;
        createMasker(getWidth(), getHeight());
        invalidate();
        onErase();
    }

    /**
     * 擦除这个遮罩层
     */
    public void clear() {
        int w = getWidth();
        int h = getHeight();
        mMaskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mMaskCanvas = new Canvas(mMaskBitmap);

        Rect rect = new Rect(0, 0, w, h);
        mMaskCanvas.drawRect(rect, mErasePaint);
        invalidate();

        onErase();
    }
    /**
     * 设置橡皮擦大小
     * @param eraseSize
     */
    public void setEraseSize(float eraseSize) {
        mEraseSize = eraseSize;
        mErasePaint.setStrokeWidth(mEraseSize);
    }

    public float getEraseSize() {
        return mEraseSize;
    }

    public EraseStatusListener getEraseStatusListener() {
        return mEraseStatusListener;
    }

    public void setEraseStatusListener(EraseStatusListener eraseStatusListener) {
        mEraseStatusListener = eraseStatusListener;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * 擦除状态监听器
     */
    interface EraseStatusListener {
        /**
         * 擦除进度
         * @param percent
         */
        void onProgress(int percent);

        /**
         * 擦除完成
         * @param view
         */
        void onCompleted(View view);
    }
}
