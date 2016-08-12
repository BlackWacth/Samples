package com.hua.okhttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hua.okhttp.global.C;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView mListView;
    private List<String> mList;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.lv_list);
        mList = new ArrayList<>();

        mAdapter = new ListAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private void addItem(String item, boolean isAppend) {
        if (!isAppend) {
            mList.clear();
        }
        mList.add(item);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mList != null) {
            mList.clear();
        }
        addItem("同步Get", true);
        addItem("异步Get", true);
        addItem("Post请求", true);
        addItem("提取响应头", true);
        addItem("Post提交String", true);
        addItem("Post提交Stream", true);
        addItem("Post提交文件", true);
        addItem("Post提交表单", true);
        addItem("Post提交分块请求", true);
        addItem("Gson解析", true);
        addItem("响应缓存", true);
        addItem("超时", true);
        addItem("Call配置", true);
        addItem("验证处理", true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, BaseOkHttpActivity.class);
        intent.putExtra(C.EXTRA_BASE_OKHTTP, position);
        startActivity(intent);
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public String getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new Holder();
                holder.mTextView = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.mTextView.setText(getItem(position));
            return convertView;
        }
    }

    class Holder {
        TextView mTextView;
    }
}
