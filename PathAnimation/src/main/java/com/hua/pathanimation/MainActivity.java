package com.hua.pathanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private PathAnimationLayout mPathAnimationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPathAnimationLayout = (PathAnimationLayout) findViewById(R.id.path_view_group);
        mPathAnimationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPathAnimationLayout.startAnimation();
            }
        });
    }
}
