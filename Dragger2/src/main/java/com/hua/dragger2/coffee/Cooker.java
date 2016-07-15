package com.hua.dragger2.coffee;

/**
 * Created by hzw on 2016/7/15.
 */
public class Cooker {

    String mName;
    String mCoffeeKind;

    public Cooker(String name, String coffeeKind) {
        mName = name;
        mCoffeeKind = coffeeKind;
    }

    public String make() {
        return mName + " make " + mCoffeeKind;
    }
}
