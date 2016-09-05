package com.hua.pathanimation.interpolater;

import android.view.animation.Interpolator;

/**
 * Created by hzw on 2016/9/2.
 */
public class BackOutInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        return ((input = input - 1) * input * ((1.70158f + 1) * input + 1.70158f) + 1);
    }

}
