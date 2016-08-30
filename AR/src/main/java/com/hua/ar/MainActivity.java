package com.hua.ar;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    public static final String tag = "hzw";
    public static final int REQUEST_CODE = 555;
    private final float DISPLACEMENT = 22;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceViewCallback mSurfaceViewCallback;
    private boolean previewing;
    private Camera mCamera;
    private SensorManager mSensorManager;
    private Float tmp_x, tmp_y, tmp_z;
    private RelativeLayout group;
    private ImageView imageView;
    private int mCurrentCamIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkPermission(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        }

        mSurfaceView = (SurfaceView) findViewById(R.id.sv_surface);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceViewCallback = new SurfaceViewCallback();
        mSurfaceHolder.addCallback(mSurfaceViewCallback);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        group = (RelativeLayout) findViewById(R.id.rl_group);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        imageView = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pikachu);
        imageView.setImageBitmap(bitmap);
        group.addView(imageView);

        imageView.setX(400);
        imageView.setY(400);
    }

    public boolean checkPermission (String permission) {
        if(ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public boolean checkPermissions(String... permissions) {
        for (String permission : permissions) {
            if(!checkPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE) {

        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        if(tmp_x != null && tmp_y != null && tmp_z != null) {
            float xPosition = 0;
            float yPosition = 0;
            float zPosition = 0;

            float xAngle = tmp_x - values[0];
            float yAngle = tmp_y - values[1];
            float zAngle = tmp_z - values[2];

            if(xAngle < -180) {
                xAngle += 360;
            } else if (xAngle > 180) {
                xAngle -= 360;
            }

            xPosition = imageView.getX() + xAngle * DISPLACEMENT;
            yPosition = imageView.getY() + yAngle * DISPLACEMENT;

            if(xPosition > 180 * DISPLACEMENT) {
                xPosition = 180 * -DISPLACEMENT;
            } else if (xPosition < 180 * -DISPLACEMENT) {
                xPosition = 180 * DISPLACEMENT;
            }

            imageView.setX(xPosition);
            imageView.setY(yPosition);
        }
        tmp_x = values[0];
        tmp_y = values[1];
        tmp_z = values[2];

        Log.i(tag, "x = " + tmp_x);
        Log.i(tag, "y = " + tmp_y);
        Log.i(tag, "z = " + tmp_z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        setSensor();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    private void setSensor() {
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        Log.i(tag, "sensor size = " + sensors.size());
        if(sensors.size() > 0) {
            mSensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        } else {
            Toast.makeText( this, " 您的手机沒有方向传感器! ", Toast.LENGTH_LONG).show();
        }
    }

    private final class SurfaceViewCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mCamera = openFrontFacingCameraGingerbread();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if(previewing) {
                mCamera.stopPreview();
                previewing = false;
            }
            try{
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
                previewing = true;
                setCameraDisplayOrientation(MainActivity.this, mCurrentCamIndex, mCamera);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            previewing = false;
        }
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        Log.i(tag, "cameraCount = " + cameraCount);
        for(int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cam = Camera.open(i);
            }
        }
        return cam;
    }
}
