package com.hua.dragger2.coffee;

/**
 * Created by hzw on 2016/7/15.
 */
public class SimpleMaker implements CoffeeMaker {

    Cooker mCooker;

    public SimpleMaker(Cooker cooker) {
        mCooker = cooker;
    }

    @Override
    public String makeCoffee() {
        return mCooker.make();
    }
}
