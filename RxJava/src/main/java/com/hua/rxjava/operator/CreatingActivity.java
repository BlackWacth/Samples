package com.hua.rxjava.operator;

import android.view.View;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;

public class CreatingActivity extends BaseOperatorActivity {

    private String deferStr = "";

    @Override
    protected void onStart() {
        super.onStart();
        String tempStr = "";
        switch (position) {
            case 0:
                tempStr = "Create";
                break;

            case 1:
                tempStr = "Defer";
                break;

            case 2:
                tempStr = "From";
                break;

            case 3:
                tempStr = "Just";
                break;

            case 4:
                tempStr = "Interval";
                break;

            case 5:
                tempStr = "Range";
                break;

            case 6:
                tempStr = "Repeat/repeatWhen";
                break;

            case 7:
                tempStr = "Timer";
                break;

            case 8:
                tempStr = "Empty/Never/Throw";
                break;

        }
        setTitle(tempStr);
        mOperator.setText(tempStr);
    }

    @Override
    public void onClick(View v) {
        switch (position) {
            case 0:
                create();
                break;

            case 1:
                defer();
                break;

            case 2:
                from();
                break;

            case 3:
                just();
                break;

            case 4:
                interval();
                break;

            case 5:
                range();
                break;

            case 6:
                repeatAndRepeatWhen();
                break;

            case 7:
                timer();
                break;

            case 8:
                emptyAndNeverAndThrow();
                break;

        }
    }

    private void emptyAndNeverAndThrow() {
        Observable.empty();
        Observable.never();
    }

    /**
     * 重复产生结果，repeat()无参时，表示无限重复。
     */
    private void repeatAndRepeatWhen() {
        mSubscription = Observable.just(1, 2, 3)
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                                @Override
                                public Observable<?> call(Observable<? extends Void> observable) {
                                    return observable.zipWith(Observable.range(1, 3), new Func2<Void, Integer, Integer>() {
                                        @Override
                                        public Integer call(Void aVoid, Integer integer) {
                                            return integer;
                                        }
                                    }).flatMap(new Func1<Integer, Observable<?>>() {
                                        @Override
                                        public Observable<?> call(Integer integer) {
                                            append("----------------------------------");
                                            return Observable.timer(1, TimeUnit.SECONDS);
                                        }
                                    });
                                }
                            }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextInteger);
    }

    /**
     * 以一个数做为起始值，然后生产N个数，每个数从前一个叠加1。
     */
    private void range() {
        Observable.range(11, 20)
                .subscribe(onNextInteger);
    }

    /**
     * 从0开始，每隔一段时间，递增1， 到无穷大。
     * 递增过程会开一个子线程。
     */
    private void interval() {
        mSubscription = Observable.interval(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextLong);
    }

    /**
     * 1、一段时间后执行observalbe。
     * 2、跟interval的效果相同。
     * <p/>
     * 间隔过程会开一个子线程。
     */
    private void timer() {
        mSubscription = Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextLong);
    }

    /**
     * 直到订阅者订阅时，才创建Observable并执行，保证了执行时observable是最新状态。
     */
    private void defer() {
        deferStr = "10";
        Observable justObservable = Observable.just(deferStr);
        deferStr = "20";
        Observable deferObservable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable call() {
                return Observable.just(deferStr);
            }
        });
        deferStr = "30";

        justObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                append("just = " + o);
            }
        });

        deferObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                append("defer = " + o);
            }
        });
    }

    private void from() {
        String[] nums = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        Observable.from(nums)
                .subscribe(onNextString);
    }

    private void just() {
        Observable.just("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
                .subscribe(onNextString);
    }

    /**
     * 通过create创建observable
     */
    private void create() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //在subscriber取消订阅是不会再执行call函数中相关逻辑，避免意想不到的错误。
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                subscriber.onNext("Java");
                subscriber.onNext("Android");
                subscriber.onNext("C++");
                subscriber.onNext("PHP");
                subscriber.onCompleted();
            }
        }).subscribe(onNextString, onError, onCompleted);
    }
}
