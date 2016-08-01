package com.hua.openfire.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by hzw on 2016/8/1.
 */
public class BaseActivity extends AppCompatActivity{

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
