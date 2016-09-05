package com.hua.pathanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.hua.pathanimation.interpolater.BackOutInterpolator;
import com.hua.pathanimation.interpolater.BreathInterpolator;
import com.hua.pathanimation.interpolater.CircularOutInterpolator;
import com.hua.pathanimation.interpolater.ExpoOutInterpolator;
import com.hua.pathanimation.interpolater.QuadOutInterpolator;

/**
 *
 * Created by hzw on 2016/9/1.
 */
public class PathCircleLayout extends RelativeLayout {

    public static final String tag = "PathCircleLayout";

    public static final int ICONS[] = new int[]{
            R.mipmap.ic_main_camera,
            R.mipmap.ic_main_selfie,
            R.mipmap.ic_main_antenna,
            R.mipmap.ic_main_fast_fill,
            R.mipmap.ic_main_fingerprint,
            R.mipmap.ic_main_more
    };

    public static final String[] NAMES = new String[] {
            "后置摄像",
            "臻美自拍",
            "超强天线",
            "闪充+续航",
            "正面指纹",
            "更多"
    };

    private PathCircleItemView[] mItemViews = new PathCircleItemView[ICONS.length];

    private PathMeasure mPathMeasure;
    private float mPathLength;
    private long mTotalDuration = 1000;
    private ViewHolder[] mHolders = new ViewHolder[ICONS.length];
    private OnItemClickListener mOnItemClickListener;
    private boolean isCompleted = false;

    public PathCircleLayout(Context context) {
        this(context, null);
    }

    public PathCircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.setBackgroundColor(Color.argb(0, 0, 0, 0));
        View view = LayoutInflater.from(context).inflate(R.layout.path_circle_layout, null);
        mItemViews[0] = (PathCircleItemView) view.findViewById(R.id.path_circle_item_camera);
        mItemViews[1] = (PathCircleItemView) view.findViewById(R.id.path_circle_item_selfie);
        mItemViews[2] = (PathCircleItemView) view.findViewById(R.id.path_circle_item_antenna);
        mItemViews[3] = (PathCircleItemView) view.findViewById(R.id.path_circle_item_fast_fill);
        mItemViews[4] = (PathCircleItemView) view.findViewById(R.id.path_circle_item_fingerprint);
        mItemViews[5] = (PathCircleItemView) view.findViewById(R.id.path_circle_item_more);

        for(int i = 0; i < mItemViews.length; i++) {
            mItemViews[i].setIcon(ICONS[i]);
            mItemViews[i].setName(NAMES[i]);
        }

        addView(view);

        mPathMeasure = createPathMeasure();
        mPathLength = mPathMeasure.getLength();

        float blockLength = mPathLength / (mItemViews.length - 1);
        long blockDuration = mTotalDuration / mItemViews.length;

        float tempLength;
        long tempDuration;
        long tempDelay;
        for(int i = 0; i < mHolders.length; i++) {
            tempLength = mPathLength - i * blockLength;
            tempDuration = mTotalDuration - i * blockDuration;
            tempDelay = i * blockDuration;
            mHolders[i] = new ViewHolder(mItemViews[i], tempLength, tempDuration, tempDelay);
        }
        reset();

        for (int i = 0; i < mItemViews.length; i++) {
            addClick(mItemViews[i], i);
        }
    }

    private void addClick(PathCircleItemView view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null && isCompleted) {
                    mOnItemClickListener.onItemClick(position, (PathCircleItemView) v);
                }
            }
        });
    }

    private PathMeasure createPathMeasure() {
        Path path = new Path();
        RectF rectF = new RectF(-320, 300, 780, 1400);
        path.addArc(rectF, 90, -180);
        return new PathMeasure(path, false);
    }

    public void startAnimator(final int position, final PathCircleItemView view, float length, long duration) {
        view.setVisibility(VISIBLE);
        final float halfWidth = view.getWidth() / 2;
        final float halfHeight = view.getHeight() / 2;
        ValueAnimator animator = ValueAnimator.ofFloat(0, length);
        animator.setDuration(duration);
//        animator.setInterpolator(new BackOutInterpolator());
        animator.setInterpolator(new ExpoOutInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                float[] currentPoint = new float[2];
                mPathMeasure.getPosTan(distance, currentPoint, null);
                view.setX(currentPoint[0] - halfWidth);
                view.setY(currentPoint[1] - halfHeight);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.startAnimation();
                isCompleted = true;
            }
        });
        animator.start();
    }

    public void startAnimatorDelay(final int position, final PathCircleItemView view, final float length, final long duration, long delay) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimator(position, view, length, duration);
            }
        }, delay);
    }

    public void startAnimatorAll() {
        ViewHolder holder;
        for (int i = 0; i < mHolders.length; i++) {
            holder = mHolders[i];
            startAnimatorDelay(i, holder.mItemView, holder.mLength, holder.mDuration, holder.mDelay);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        child.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void reset() {
        for (PathCircleItemView item : mItemViews) {
            item.reset();
            item.setX(0f);
            item.setY(0f);
            item.setVisibility(INVISIBLE);
        }
    }

    class ViewHolder {
        PathCircleItemView mItemView;
        float mLength;
        long mDuration;
        long mDelay;

        public ViewHolder(PathCircleItemView itemView, float length, long duration, long delay) {
            mItemView = itemView;
            mLength = length;
            mDuration = duration;
            mDelay = delay;
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position, PathCircleItemView view);
    }
}
