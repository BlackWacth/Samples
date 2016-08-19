package com.hua.appupgrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hua.appupgrade.utils.L;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Subscription mSubscription;
    private OkHttpClient mOkHttpClient;
    private Request mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOkHttpClient = new OkHttpClient();
        mRequest = new Request.Builder()
                .url("http://publicobject.com/secrets/hellosecret.txt")
                .build();
        download();
    }
    
    private void download() {

        mOkHttpClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                subscriber.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                subscriber.onNext(response.body().string());
                L.i(response.body().string());
//                subscriber.onCompleted();
            }
        });

//        mSubscription = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(final Subscriber<? super String> subscriber) {
//                if(subscriber.isUnsubscribed()) return;
//                mOkHttpClient.newCall(mRequest).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        subscriber.onError(e);
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        subscriber.onNext(response.body().string());
//                        L.i(response.body().string());
//                        subscriber.onCompleted();
//                    }
//                });
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        L.i("onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        L.i(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        L.i("onNext : " + s);
//                    }
//                });

    }
}
