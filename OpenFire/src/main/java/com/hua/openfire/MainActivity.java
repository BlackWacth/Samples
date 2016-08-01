package com.hua.openfire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hua.openfire.auth.GoogleSignInActivity;
import com.hua.openfire.base.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_auth).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auth:
                startActivity(GoogleSignInActivity.class);
                break;
        }
    }
}
