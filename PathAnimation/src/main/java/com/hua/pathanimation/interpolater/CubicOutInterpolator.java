package com.hua.pathanimation.interpolater;

import android.view.animation.Interpolator;

/**
 * Created by hzw on 2016/9/2.
 */
public class CubicOutInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return (input - 1) * (input - 1) * (input - 1) + 1;
    }
}
