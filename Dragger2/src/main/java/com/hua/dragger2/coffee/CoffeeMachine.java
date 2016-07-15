package com.hua.dragger2.coffee;

/**
 * Created by hzw on 2016/7/15.
 */
public class CoffeeMachine {

    private CoffeeMaker mMaker;

    public CoffeeMachine(Cooker cooker) {
        mMaker = new SimpleMaker(cooker);
    }

    public String makeCoffee() {
        return mMaker.makeCoffee();
    }
}
