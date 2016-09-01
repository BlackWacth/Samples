package com.hua.pathanimation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by hzw on 2016/9/1.
 */
public class PathCircleItemView extends LinearLayout {

    public static final String tag = "PathCircleItemView";

    private ImageView mIcon;
    private TextView mName;
    private long duration = 500;

    public PathCircleItemView(Context context) {
        this(context, null);
    }

    public PathCircleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.path_circle_item_view, null);
        mIcon = (ImageView) view.findViewById(R.id.path_circle_item_icon);
        mName = (TextView) view.findViewById(R.id.path_circle_item_name);
        reset();
        addView(view);
    }

    public void startAnimation() {
        mName.setVisibility(View.VISIBLE);
        mName.animate().alpha(1f).setDuration(duration).setInterpolator(new DecelerateInterpolator()).start();
    }

    public ImageView getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon.setImageResource(icon);
    }

    public TextView getName() {
        return mName;
    }

    public void setName(String name) {
        mName.setText(name);
    }

    public void reset() {
        mName.setAlpha(0f);
        mName.setVisibility(View.INVISIBLE);
    }
}
