package com.hua.bezier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hua.bezier.widget.BezierView;

public class MainActivity extends AppCompatActivity {

    private BezierView mBezierView;

    private SeekBar mSeekBar;

    private TextView mTextView;

    private Switch mLoop, mTangent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBezierView = (BezierView) findViewById(R.id.bezier);
        mTextView = (TextView) findViewById(R.id.textview);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mLoop = (Switch) findViewById(R.id.loop);
        mTangent = (Switch) findViewById(R.id.tangent);

        mTextView.setText(mBezierView.getOrderStr() + "阶贝塞尔曲线");

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress = 1;
                }
                mBezierView.setRate(progress * 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mLoop.setChecked(false);
        mTangent.setChecked(true);
        mLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBezierView.setLoop(isChecked);
            }
        });
        mTangent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBezierView.setTangent(isChecked);
            }
        });
    }

    public void start(View view) {
        mBezierView.start();
    }

    public void stop(View view) {
        mBezierView.stop();
    }

    public void add(View view) {
        if (mBezierView.addPoint()) {
            mTextView.setText(mBezierView.getOrderStr() + "阶贝塞尔曲线");
        } else {
            showToast("Add point failed.");
        }
    }

    public void del(View view) {
        if (mBezierView.delPoint()) {
            mTextView.setText(mBezierView.getOrderStr() + "阶贝塞尔曲线");
        } else {
            showToast("Delete point failed.");
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
