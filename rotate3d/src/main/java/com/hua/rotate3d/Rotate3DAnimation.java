package com.hua.rotate3d;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by 华忠伟 on 2016/6/21.
 */
public class Rotate3DAnimation extends Animation {

    private float mFromDegrees;
    private float mToDegrees;
    private float mCenterX;
    private float mCenterY;
    private float mDepthZ;
    private boolean isReverse;
    private Camera mCamera;

    public Rotate3DAnimation(float fromDegrees, float toDegrees, float centerX, float centerY, float depthZ, boolean isReverse) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        this.isReverse = isReverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + (mToDegrees - fromDegrees) * interpolatedTime;

        float centerX = mCenterX;
        float centerY = mCenterY;

        Camera camera = mCamera;
        Matrix matrix = t.getMatrix();

        camera.save();
        if(isReverse) {
            camera.translate(0f, 0f, mDepthZ * interpolatedTime);
        } else {
            camera.translate(0f, 0f, mDepthZ * (1.0f - interpolatedTime));
        }
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
