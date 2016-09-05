package com.hua.scratchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener{

    private ScratchView mScratchView;
    private TextView mCurrentPercent, mTvEraseSize, mTvMinPercent;
    private RadioGroup mMaskColor, mHaveWatermark;
    private SeekBar mEraseSize, mMinPercent;
    private Button mClear, mReset;

    private int defEraseSize = 50;
    private int defMinPercent = 80;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCurrentPercent = (TextView) findViewById(R.id.tv_scratch_percent);
        mTvEraseSize = (TextView) findViewById(R.id.tv_erase_size);
        mTvMinPercent = (TextView) findViewById(R.id.tv_min_percent);
        mMaskColor = (RadioGroup) findViewById(R.id.rg_mask_color);
        mHaveWatermark = (RadioGroup) findViewById(R.id.rg_watermark);
        mEraseSize = (SeekBar) findViewById(R.id.sb_erase_size);
        mMinPercent = (SeekBar) findViewById(R.id.sb_min_percent);
        mScratchView = (ScratchView) findViewById(R.id.scratch_view);
        mClear = (Button) findViewById(R.id.btn_clear);
        mReset = (Button) findViewById(R.id.btn_reset);

        mCurrentPercent.setText("0 %");
        mScratchView.setEraseStatusListener(new ScratchView.EraseStatusListener() {
            @Override
            public void onProgress(int percent) {
                mCurrentPercent.setText(percent + " %");
            }

            @Override
            public void onCompleted(View view) {
                Toast.makeText(MainActivity.this, "onCompleted !!", Toast.LENGTH_SHORT).show();
            }
        });

        mMaskColor.check(R.id.rb_mask_color04);
        mMaskColor.setOnCheckedChangeListener(this);

        mHaveWatermark.check(R.id.rb_watermark_no);
        mHaveWatermark.setOnCheckedChangeListener(this);

        mEraseSize.setMax(100);
        mEraseSize.setProgress(defEraseSize);
        mTvEraseSize.setText(defEraseSize+"");
        mScratchView.setEraseSize(defEraseSize);
        mEraseSize.setOnSeekBarChangeListener(this);

        mMinPercent.setMax(100);
        mMinPercent.setProgress(defMinPercent);
        mTvMinPercent.setText(defMinPercent + " %");
        mScratchView.setMinWipePercent(defMinPercent);
        mMinPercent.setOnSeekBarChangeListener(this);

        mClear.setOnClickListener(this);
        mReset.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_mask_color01 :
                mScratchView.setMaskColor(getResources().getColor(R.color.red300));
                break;

            case R.id.rb_mask_color02 :
                mScratchView.setMaskColor(getResources().getColor(R.color.blue300));
                break;

            case R.id.rb_mask_color03 :
                mScratchView.setMaskColor(getResources().getColor(R.color.yello300));
                break;

            case R.id.rb_mask_color04 :
                mScratchView.setMaskColor(getResources().getColor(R.color.grey));
                break;

            case R.id.rb_watermark_yes:
                mScratchView.setWatermark(R.mipmap.alipay);
                break;

            case R.id.rb_watermark_no:
                mScratchView.setWatermark(-1);
                break;
        }
        mScratchView.reset();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear :
                mScratchView.clear();
                break;

            case R.id.btn_reset :
                mScratchView.reset();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_erase_size :
                mTvEraseSize.setText(progress + "");
                mScratchView.setEraseSize(progress);
                break;

            case R.id.sb_min_percent :
                mTvMinPercent.setText(progress + " %");
                mScratchView.setMinWipePercent(progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
