package com.hua.pathanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PathAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_animation);
        final PathAnimationLayout pathAnimationLayout = (PathAnimationLayout) findViewById(R.id.path_animation_layout);
        pathAnimationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathAnimationLayout.startAnimation();
            }
        });
    }
}
