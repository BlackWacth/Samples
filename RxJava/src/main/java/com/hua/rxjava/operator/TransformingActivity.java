package com.hua.rxjava.operator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

public class TransformingActivity extends BaseOperatorActivity {


    @Override
    protected void onStart() {
        super.onStart();
        String tempStr = "";
        switch (position) {
            case 0:
                tempStr = "buffer";
                break;

            case 1:
                tempStr = "flatMap";
                break;

            case 2:
                tempStr = "groupBy";
                break;

            case 3:
                tempStr = "Map";
                break;

            case 4:
                tempStr = "Scan";
                break;

            case 5:
                tempStr = "Window";
                break;

            case 6:
                tempStr = "cast";
                break;

            case 7:
                tempStr = "concatMap";
                break;

            case 8:
                tempStr = "switchMap";
                break;
        }
        setTitle(tempStr);
        mOperator.setText(tempStr);
    }

    @Override
    public void onClick(View v) {
        switch (position) {
            case 0:
                buffer();
                break;

            case 1:
                flatMap();
                break;

            case 2:
                groupBy();
                break;

            case 3:
                map();
                break;

            case 4:
                scan();
                break;

            case 5:
                window();
                break;

            case 6:
                cast();
                break;

            case 7:
                concatMap();
                break;

            case 8:
                switchMap();
                break;
        }
    }

    private void switchMap() {

        Observable.just(10, 20, 30)
                .flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        int delay = 200;
                        if (integer > 10) {
                            delay = 180;
                        }
                        return Observable.from(new Integer[] {integer, integer / 2})
                                .delay(delay, TimeUnit.MILLISECONDS);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        append("flatMap >>> " + integer);
                    }
                });

        Observable.just(10, 20, 30)
                .switchMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        int delay = 200;
                        if (integer > 10) {
                            delay = 180;
                        }
                        return Observable.from(new Integer[] {integer, integer / 2})
                                .delay(delay, TimeUnit.MILLISECONDS);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        append("switchMap --> " + integer);
                    }
                });
    }

    private void concatMap() {
        Observable.just(10, 20, 30)
                .flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        int delay = 200;
                        if (integer > 10) {
                            delay = 180;
                        }
                        return Observable.from(new Integer[] {integer, integer / 2})
                                .delay(delay, TimeUnit.MILLISECONDS);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        append("flatMap >>> " + integer);
                    }
                });

        Observable.just(10, 20, 30)
                .concatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        int delay = 200;
                        if (integer > 10) {
                            delay = 180;
                        }
                        return Observable.from(new Integer[] {integer, integer / 2})
                                .delay(delay, TimeUnit.MILLISECONDS);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        append("concatMap ===> " + integer);
                    }
                });
    }

    private void window() {
        mSubscription = Observable.interval(1, TimeUnit.SECONDS)
                .take(12)
                .window(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Observable<Long>>() {
                    @Override
                    public void call(Observable<Long> longObservable) {
                        append("subdivide begin ------------");
                        longObservable
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(onNextLong);
                    }
                });
    }

    private void scan() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer sum, Integer item) {
                        return sum + item;
                    }
                })
                .subscribe(onNextInteger);
    }

    private void cast() {
        mSubscription = Observable.just("1", "2", "3", "4", "5", "6")
                .cast(String.class)
                .subscribe(onNextString);
    }

    private void map() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer * integer;
                    }
                }).subscribe(onNextInteger);
    }

    /**
     * groupBy操作符是对源Observable产生的结果进行分组，形成一个类型为GroupedObservable的结果集，
     * GroupedObservable中存在一个方法为getKey()，
     * 可以通过该方法获取结果集的Key值（类似于HashMap的key)。
     *
     * 由于结果集中的GroupedObservable是把分组结果缓存起来，如果对每一个GroupedObservable不进行处理
     * （既不订阅执行也不对其进行别的操作符运算），就有可能出现内存泄露。因此，
     * 如果你对某个GroupedObservable不进行处理，最好是对其使用操作符take(0)处理。
     *
     * GroupedObservable 如果更新UI，还要切换到主线程。
     */
    private void groupBy() {
        mSubscription = Observable.interval(1, TimeUnit.SECONDS)
                .take(10)
                .groupBy(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong % 3;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GroupedObservable<Long, Long>>() {
                    @Override
                    public void call(final GroupedObservable<Long, Long> groupedObservable) {
                        groupedObservable
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Long>() {
                                    @Override
                                    public void call(Long aLong) {
                                        append(groupedObservable.getKey() + " : " + aLong);
                                    }
                                });
                    }
                });
    }

    private Observable<File> listFiles(final File f) {
        if (f.isFile()) {
            return Observable.just(f);
        } else {
            return Observable.from(f.listFiles())
                    .flatMap(new Func1<File, Observable<File>>() {
                        @Override
                        public Observable<File> call(File file) {
                            Log.i(tag, "flatMap >>> " + file.getAbsolutePath());
                            return listFiles(file);
                        }
                    });
        }
    }

    private void flatMap() {
        checkPermission();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            return ;
        }

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.i(tag, "file = " + file.getAbsolutePath());

        Observable.just(file)
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        Log.i(tag, "flatMap ---- " + file.getAbsolutePath());
                        return listFiles(file);
                    }
                })
                .map(new Func1<File, String>() {
                    @Override
                    public String call(File file) {
                        return file.getAbsolutePath();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextString);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 555) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
        }
    }

    private void findFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                findFile(f);
            }
        } else {
            append(file.getAbsolutePath());
        }
    }

    private void buffer() {
        final String[] mails = new String[]{"Here is an email!", "Another email!", "Yet another email!"};
        mSubscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    Random random = new Random();
                    while (true) {
                        String mail = mails[random.nextInt(mails.length)];
                        subscriber.onNext(mail);
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .buffer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        append(String.format("\nYou've got %d new messages!  Here they are!", list.size()));
                        for (String str : list) {
                            append(str);
                        }
                    }
                });

    }
}
