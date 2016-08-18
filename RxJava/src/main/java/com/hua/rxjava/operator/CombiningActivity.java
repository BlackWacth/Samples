package com.hua.rxjava.operator;

import android.util.AndroidException;
import android.util.Log;
import android.view.View;

import com.hua.rxjava.OperatorsActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by hzw on 2016/8/18.
 */
public class CombiningActivity extends BaseOperatorActivity {

    @Override
    protected void onStart() {
        super.onStart();
        String tempStr = OperatorsActivity.combining[position];
        setTitle(tempStr);
        mOperator.setText(tempStr);
    }

    @Override
    public void onClick(View v) {
        switch (position) {
            case 0:
                andThenWhen();
                break;

            case 1:
                combineLatest();
                break;

            case 2:
                join();
                break;

            case 3:
                merge();
                break;

            case 4:
                startWith();
                break;

            case 5:
                switchOperator();
                break;

            case 6:
                zip();
                break;

            case 7:
                switchOnNext();
                break;

            case 8:
                groupJoin();
                break;

            case 9:
                mergeDelayError();
                break;

            case 10:
                break;

            case 11:
                break;

        }
    }

    /**
     * 与merge类似，区别在于mergeDelayError把错误放在结果合并完成之后。
     */
    private void mergeDelayError() {
        Observable<Long> errorObservable = Observable.error(new Exception("this is end"));

        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.i(tag, "observable1 : " + aLong);
                        return aLong * 5;
                    }
                })
                .take(3)
                .mergeWith(errorObservable.delay(6, TimeUnit.SECONDS));

        Observable<Long> observable2 = Observable.interval(2, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.i(tag, "observable2 : " + aLong);
                        return aLong * 10;
                    }
                })
                .take(5);

        mSubscription = Observable.mergeDelayError(observable1, observable2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextLong, onError, onCompleted);
    }

    /**
     * groupJoin操作符非常类似于join操作符，区别在于join操作符中第四个参数的传入函数不一致
     */
    private void groupJoin() {
        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.i(tag, "observable1 : " + aLong);
                        return aLong * 5;
                    }
                })
                .take(5);

        Observable<Long> observable2 = Observable.interval(2, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.i(tag, "observable2 : " + aLong);
                        return aLong * 10;
                    }
                })
                .take(5);

        observable1.groupJoin(observable2, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                Log.i(tag, "func observable1 -------- : " + aLong);
                return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
            }
        }, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                Log.i(tag, "func observable2 -------- : " + aLong);
                return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
            }
        }, new Func2<Long, Observable<Long>, Observable<Long>>() {
            @Override
            public Observable<Long> call(final Long aLong, Observable<Long> longObservable) {
                return longObservable.map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong2) {
                        return aLong + aLong2;
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Observable<Long>>() {
                    @Override
                    public void call(Observable<Long> longObservable) {
                        longObservable
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(onNextLong);
                    }
                }, onError, onCompleted);
    }

    private void switchOnNext() {

    }

    private void zip() {

    }

    private void switchOperator() {

    }

    private void startWith() {

    }

    /**
     * merge操作符是按照两个Observable提交结果的时间顺序，对Observable进行合并，
     * 如ObservableA每隔500毫秒产生数据为0,5,10,15,20；
     * 而ObservableB每隔500毫秒产生数据0,10,20,30,40，
     * 其中第一个数据延迟500毫秒产生，最后合并结果为：0,0,5,10,10,20,15,30,20,40
     *
     * 出现错误立即停止
     */
    private void merge() {
        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.i(tag, "observable1 : " + aLong);
                        return aLong * 5;
                    }
                })
                .take(5);

        Observable<Long> observable2 = Observable.interval(2, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.i(tag, "observable2 : " + aLong);
                        return aLong * 10;
                    }
                })
                .take(5);

        mSubscription = Observable.merge(observable1, observable2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextLong, onError, onCompleted);
    }

    /**
     * join操作符把类似于combineLatest操作符，也是两个Observable产生的结果进行合并，
     * 合并的结果组成一个新的Observable，但是join操作符可以控制每个Observable产生结果的生命周期，
     * 在每个结果的生命周期内，可以与另一个Observable产生的结果按照一定的规则进行合并
     */
    private void join() {
        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.i(tag, "observable1 : " + aLong);
                        return aLong * 5;
                    }
                })
                .take(5);

        Observable<Long> observable2 = Observable.interval(2, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.i(tag, "observable2 : " + aLong);
                        return aLong * 10;
                    }
                })
                .take(5);

        observable1.join(observable2, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                Log.i(tag, "func observable1 -------- : " + aLong);
                return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
            }
        }, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                Log.i(tag, "func observable2 -------- : " + aLong);
                return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
            }
        }, new Func2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) {
                Log.i(tag, "result =======> : " + aLong);
                return aLong + aLong2;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextLong, onError, onCompleted);
    }

    /**
     * 把两个Observable产生的结果进行合并，合并的结果组成一个新的Observable。
     * 这两个Observable中任意一个Observable产生的结果，都和另一个Observable最后产生的结果，
     * 按照一定的规则进行合并。
     */
    private void combineLatest() {
        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.i(tag, "observable1 : " + aLong);
                        return aLong * 5;
                    }
                })
                .take(5);

        Observable<Long> observable2 = Observable.interval(2, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.i(tag, "observable2 : " + aLong);
                        return aLong * 10;
                    }
                })
                .take(5);

        mSubscription = Observable.combineLatest(observable1, observable2, new Func2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) {
                Log.i(tag, "a1 = " + aLong + ", a2 = " + aLong2);
                return aLong + aLong2;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextLong, onError, onCompleted);
    }

    private void andThenWhen() {

    }

}
