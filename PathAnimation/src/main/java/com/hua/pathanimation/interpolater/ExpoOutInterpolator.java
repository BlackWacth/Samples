package com.hua.pathanimation.interpolater;

import android.view.animation.Interpolator;

/**
 * Created by hzw on 2016/9/2.
 */
public class ExpoOutInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return (float) ((input == 1) ? 1 : (-Math.pow(2, -10 * input) + 1));
    }
}
