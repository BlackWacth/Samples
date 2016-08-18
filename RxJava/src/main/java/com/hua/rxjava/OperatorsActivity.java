package com.hua.rxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hua.rxjava.operator.CombiningActivity;
import com.hua.rxjava.operator.CreatingActivity;
import com.hua.rxjava.operator.FilteringActivity;
import com.hua.rxjava.operator.TransformingActivity;

public class OperatorsActivity extends AppCompatActivity {

    public static final String tag = "OperatorsActivity";
    public static final String EXTRA_SPECIFIC_OPERATOR = "EXTRA_SPECIFIC_OPERATOR";

    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;
    private Class<?> jumpCls = null;

    //创建操作
    public final static String[] creating = new String[]{"1 Create", "2 Defer", "3 From", "4 Just", "5 Interval", "6 Range", "7 Repeat/repeatWhen", "9 Timer", "10 Empty/Never/Throw"};

    //变换操作
    public final static  String[] transforming = new String[]{"Buffer", "FlatMap", "GroupBy", "Map", "Scan", "Window", "cast", "concatMap", "switchMap"};

    //过滤操作
    public final static  String[] filtering = new String[]{"Debounce", "Distinct", "ElemenAt", "Filter", "First", "IgnoreElements", "Last", "Simple", "Skip", "SkipLast", "Take", "TakeList", "ofType", "single", "takeFirst"};

    //组合操作
    public final static  String[] combining = new String[]{"And/Then/When", "CombineLatest", "Join", "Merge", "StartWith", "Switch", "Zip", "SwitchOnNext", "groupJoin", "mergeDelayError"};

    //错误操作
    public final static  String[] errorHanding = new String[]{"Catch", "Retry"};

    //辅助操作
    public final static  String[] utility = new String[]{"Delay", "Do", "Materialize/Dematerialize", "ObserveOn", "Serialize", "Subscribe", "TimeInterval", "Timeout", "Timestamp", "Using"};

    //条件与布尔操作
    public final static  String[] conditional = new String[]{"All", "Amb", "Contains", "DefaultIfEmpty", "SequenceEqual", "SkipUntil", "SkipWhile", "TakeUntil", "TakeWhile"};

    //算术和聚合操作
    public final static  String[] mathematical = new String[]{"Average", "Concat", "Count", "Max", "Min", "Reduce", "Sum"};

    //异步操作
    public final static  String[] async = new String[]{"start", "toAsync/asyncAction/asyncFunc", "startFuture", "forEachFuture", "fromAction", "fromCallable", "fromRunnable", "runAsync"};

    //连接操作
    public final static  String[] connect = new String[]{"Connect", "Publish", "RefCount", "Replay"};

    //转换操作
    public final static  String[] convert = new String[]{"To", "getIterator", "toFuture", "toIterable", "toList", "toMap", "toMultiMap", "toSortedList", "nest"};

    //阻塞操作
    public final static  String[] blocking = new String[]{"ForEach", "First", "Last", "MostRecent", "Next", "Single", "Latest"};

    //字符串操作
    public final static  String[] stringOperator = new String[]{"ByLine", "Decode", "Encode", "From", "Join", "Split", "StringConcat"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators);
        mListView = (ListView) findViewById(R.id.operation_listview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int position = getIntent().getIntExtra(MainActivity.EXTRA_OPERATOR_CATEGORIES, 0);
        String[] tempAry = null;
        String tempStr = "";
        switch (position) {
            case 0:
                jumpCls = CreatingActivity.class;
                tempAry = creating;
                tempStr = "创建操作";
                break;

            case 1:
                jumpCls = TransformingActivity.class;
                tempAry = transforming;
                tempStr = "变换操作";
                break;

            case 2:
                jumpCls = FilteringActivity.class;
                tempAry = filtering;
                tempStr = "过滤操作";
                break;

            case 3:
                jumpCls = CombiningActivity.class;
                tempAry = combining;
                tempStr = "组合操作";
                break;

            case 4:
                tempAry = errorHanding;
                tempStr = "错误操作";
                break;

            case 5:
                tempAry = utility;
                tempStr = "辅助操作";
                break;

            case 6:
                tempAry = conditional;
                tempStr = "条件与布尔操作";
                break;

            case 7:
                tempAry = mathematical;
                tempStr = "算术和聚合操作";
                break;

            case 8:
                tempAry = async;
                tempStr = "异步操作";
                break;

            case 9:
                tempAry = connect;
                tempStr = "连接操作";
                break;

            case 10:
                tempAry = convert;
                tempStr = "转换操作";
                break;

            case 11:
                tempAry = blocking;
                tempStr = "阻塞操作";
                break;

            case 12:
                tempAry = stringOperator;
                tempStr = "字符串操作";
                break;
        }

        setTitle(tempStr);
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tempAry);
        mListView.setAdapter(mArrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(tag, jumpCls.toString());
                Log.i(tag, position + "");
                Intent intent = new Intent(OperatorsActivity.this, jumpCls);
                intent.putExtra(EXTRA_SPECIFIC_OPERATOR, position);
                startActivity(intent);
            }
        });
    }
}
