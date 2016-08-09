package com.hua.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    public static final String tag = "hzw";

    public String[] ary = {"Java", "C++", "Python", "Php"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                Log.i(tag, "OnSubscribe --> call");
//                subscriber.onNext("Java");
//                subscriber.onNext("C++");
//                subscriber.onNext("Python");
//                subscriber.onNext("PHP");
//                subscriber.onCompleted();
//            }
//        })
//        Observable.from(ary)
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i(tag, "onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i(tag, "onError : " + e.getMessage() + " : " + e.getCause());
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        Log.i(tag, "onNext : " + s);
//                    }
//                });

//        Observable.just("Java", "C++")
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        Log.i(tag, "Action1 -> call : " + s);
//                    }
//                });

        Observable.just("Java", "C++")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.i(tag, "map -> Func1 -> call : " + s);
                        return "Python";
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return "C++".equals(s);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i(tag, "subscribe -> Action1 -> call : " + s);
                    }
                });

    }
}
