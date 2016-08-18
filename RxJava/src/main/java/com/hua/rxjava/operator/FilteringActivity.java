package com.hua.rxjava.operator;

import android.view.View;

import com.hua.rxjava.OperatorsActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hzw on 2016/8/18.
 */
public class FilteringActivity extends BaseOperatorActivity {

    @Override
    protected void onStart() {
        super.onStart();
        String tempStr = OperatorsActivity.filtering[position];
        setTitle(tempStr);
        mOperator.setText(tempStr);
    }

    @Override
    public void onClick(View v) {
        switch (position) {
            case 0:
                debounce();
                break;

            case 1:
                distinct();
                break;

            case 2:
                elemenAt();
                break;

            case 3:
                filter();
                break;

            case 4:
                first();
                break;

            case 5:
                ignoreElements();
                break;

            case 6:
                last();
                break;

            case 7:
                sample();
                break;

            case 8:
                skip();
                break;

            case 9:
                skipLast();
                break;

            case 10:
                take();
                break;

            case 11:
                takeLast();
                break;

            case 12:
                ofType();
                break;

            case 13:
                single();
                break;

            case 14:
                takeFirst();
                break;
        }
    }

    /**
     * takeFirst操作符类似于take操作符，同时也类似于first操作符，
     * 都是获取源Observable产生的结果列表中符合指定条件的前一个或多个，
     * 与first操作符不同的是，first操作符如果获取不到数据，
     * 则会抛出NoSuchElementException异常，
     * 而takeFirst则会返回一个空的Observable，该Observable只有onCompleted通知而没有onNext通知。
     */
    private void takeFirst() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .takeFirst(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 4;
                    }
                })
                .subscribe(onNextInteger, onError, onCompleted);
    }

    /**
     * single操作符是对源Observable的结果进行判断，如果产生的结果满足指定条件的数量不为1，
     * 则抛出异常，否则把满足条件的结果提交给订阅者
     */
    private void single() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .single(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 8;
                    }
                })
                .subscribe(onNextInteger, onError, onCompleted);

        //产生结果只能只有一个，否则异常
//        mSubscription = Observable.just(2)
//                .single()
//                .subscribe(onNextInteger, onError, onCompleted);
    }

    /**
     * ofType操作符类似于filter操作符，区别在于ofType操作符是按照类型对结果进行过滤
     */
    private void ofType() {
        mSubscription = Observable.just(1, "hello world", true, 200L, 0.23f)
                .ofType(String.class)
                .subscribe(onNextString);
    }

    /**
     * takeLast操作符是把源Observable产生的结果的后n项提交给订阅者，
     * 提交时机是Observable发布onCompleted通知之时。
     */
    private void takeLast() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .takeLast(2)
                .subscribe(onNextInteger);
    }

    /**
     * take操作符是把源Observable产生的结果，提取前面的n个提交给订阅者，而忽略后面的结果.
     */
    private void take() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .take(3)
                .subscribe(onNextInteger);
    }

    /**
     * skipLast操作符针对源Observable产生的结果，忽略Observable最后产生的n个结果，
     * 而把前面产生的结果提交给订阅者处理.
     *
     * skipLast操作符提交满足条件的结果给订阅者是存在延迟效果的
     *
     */
    private void skipLast() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .skipLast(3)
                .subscribe(onNextInteger);
    }

    /**
     * skip操作符针对源Observable产生的结果，跳过前面n个不进行处理，
     * 而把后面的结果提交给订阅者处理
     */
    private void skip() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .skip(3)
                .subscribe(onNextInteger);
    }

    /**
     * sample操作符定期扫描源Observable产生的结果，
     * 在指定的时间间隔范围内对源Observable产生的结果进行采样
     *
     * 最后只输出距离采样时间之前最近的的结果。
     */
    private void sample() {
        mSubscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if(subscriber.isUnsubscribed()) return;

                try {
                    for (int i = 0; i < 9; i++) {
                        subscriber.onNext(i);
                        Thread.sleep(1000);
                    }
                    Thread.sleep(3200);
                    subscriber.onNext(9);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .sample(2200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextInteger, onError, onCompleted);
    }

    /**
     * last操作符把源Observable产生的结果的最后一个提交给订阅者，
     * last操作符可以使用takeLast(1)替代。
     */
    private void last() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .last()
                .subscribe(onNextInteger);
    }

    /**
     * ignoreElements操作符忽略所有源Observable产生的结果，
     * 只把Observable的onCompleted和onError事件通知给订阅者。
     * ignoreElements操作符适用于不太关心Observable产生的结果，
     * 只是在Observable结束时(onCompleted)或者出现错误时能够收到通知。
     */
    private void ignoreElements() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .ignoreElements()
                .subscribe(onNextInteger, onError, onCompleted);
    }

    /**
     * first操作符是把源Observable产生的结果的第一个提交给订阅者，
     * first操作符可以使用elementAt(0)和take(1)替代。
     */
    private void first() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .first()
                .subscribe(onNextInteger);
    }

    /**
     * filter操作符是对源Observable产生的结果按照指定条件进行过滤，
     * 只有满足条件的结果才会提交给订阅者
     */
    private void filter() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 3;
                    }
                }).subscribe(onNextInteger);
    }

    /**
     * elementAt操作符在源Observable产生的结果中，仅仅把指定索引的结果提交给订阅者，
     * 索引是从0开始的。
     */
    private void elemenAt() {
        mSubscription = Observable.just(1, 2, 3, 4, 5, 6)
                .elementAt(2)
                .subscribe(onNextInteger);
    }

    /**
     * distinct操作符对源Observable产生的结果进行过滤，把重复的结果过滤掉，
     * 只输出不重复的结果给订阅者，非常类似于SQL里的distinct关键字。
     */
    private void distinct() {
        mSubscription = Observable.just(1, 2, 1, 1, 2, 3)
                .distinct()
                .subscribe(onNextInteger);
    }

    /**
     * debounce操作符对源Observable每产生一个结果后，如果在规定的间隔时间内没有别的结果产生，
     * 则把这个结果提交给订阅者处理，否则忽略该结果。
     *
     * 如果源Observable产生的最后一个结果后在规定的时间间隔内调用了onCompleted，
     * 那么通过debounce操作符也会把这个结果提交给订阅者。
     */
    private void debounce() {
        mSubscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (subscriber.isUnsubscribed()) return;

                try {
                    for (int i = 1; i < 10; i++) {
                        subscriber.onNext(i);
                        Thread.sleep(i * 100);
                    }
//                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextInteger, onError, onCompleted);

    }

}
