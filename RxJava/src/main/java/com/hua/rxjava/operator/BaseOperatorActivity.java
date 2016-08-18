package com.hua.rxjava.operator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hua.rxjava.OperatorsActivity;
import com.hua.rxjava.R;

import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

public abstract class BaseOperatorActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String tag = "BaseOperatorActivity";

    protected Subscription mSubscription;

    protected TextView mOperatorInfo;
    protected Button mOperator;

    public int position;

    public Action1<String> onNextString = new Action1<String>() {
        @Override
        public void call(String s) {
            append("onNext : " + s);
            Log.i(tag, "onNextString : " + s);
        }
    };

    public Action1<Integer> onNextInteger = new Action1<Integer>() {
        @Override
        public void call(Integer s) {
            append("onNext : " + s);
            Log.i(tag, "onNextString : " + s);
        }
    };

    public Action1<Long> onNextLong = new Action1<Long>() {
        @Override
        public void call(Long aLong) {
            append("onNext : " + aLong);
            Log.i(tag, "onNextString : " + aLong);
        }
    };

    public Action0 onCompleted = new Action0() {
        @Override
        public void call() {
            append("completed !!!");
        }
    };

    public Action1<Throwable> onError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            append("onError : " + throwable.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_operator);

        mOperator = (Button) findViewById(R.id.btn_operator);
        mOperator.setOnClickListener(this);

        mOperatorInfo = (TextView) findViewById(R.id.tv_operator_info);
    }

    @Override
    protected void onStart() {
        super.onStart();
        position = getIntent().getIntExtra(OperatorsActivity.EXTRA_SPECIFIC_OPERATOR, 0);
    }

    public void append(String text, boolean isAppend) {
        if(!isAppend) {
            mOperatorInfo.setText("");
        }
        mOperatorInfo.append(text + "\n");
    }

    public void append(String text) {
        append(text, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
