package com.hua.pathanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PathCircleActivity extends AppCompatActivity {

    private PathCircleLayout mPathCircleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_circle);
        mPathCircleLayout = (PathCircleLayout) findViewById(R.id.path_circle_layout);
        mPathCircleLayout.addOnItemClickListener(new PathCircleLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position, PathCircleItemView view) {
                Toast.makeText(PathCircleActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPathCircleLayout.startAnimatorAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPathCircleLayout.reset();
    }
}
