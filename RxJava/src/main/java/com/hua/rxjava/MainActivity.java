package com.hua.rxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public static final String tag = "MainActivity";
    public static final String EXTRA_OPERATOR_CATEGORIES = "EXTRA_OPERATOR_CATEGORIES";

    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;

    private String[] titles = new String[]{
            "1 创建操作",
            "2 变换操作",
            "3 过滤操作",
            "4 组合操作",
            "5 错误操作",
            "6 辅助操作",
            "7 条件与布尔操作",
            "8 算术和聚合操作",
            "9 异步操作",
            "10 连接操作",
            "11 转换操作",
            "12 阻塞操作",
            "13 字符串操作"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.main_listview);

        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);

        mListView.setAdapter(mArrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(tag, tag + " ： position = " + position);
                Intent intent = new Intent(MainActivity.this, OperatorsActivity.class);
                intent.putExtra(EXTRA_OPERATOR_CATEGORIES, position);
                startActivity(intent);
            }
        });
    }
}
