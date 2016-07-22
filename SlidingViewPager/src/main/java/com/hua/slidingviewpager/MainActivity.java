package com.hua.slidingviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hua.slidingviewpager.fragment.FirstFragment;
import com.hua.slidingviewpager.fragment.SecondFragment;
import com.hua.slidingviewpager.fragment.ThirdFragment;
import com.hua.slidingviewpager.widgets.SlidingViewPager;

public class MainActivity extends AppCompatActivity {

    private SlidingViewPager mViewPager;
    private Fragment[] mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (SlidingViewPager) findViewById(R.id.sliding_view_pager);
        mFragments = new Fragment[] {FirstFragment.newInstance(), SecondFragment.newInstance(), ThirdFragment.newInstance()};

        MViewPagerAdapter adapter = new MViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }

    class MViewPagerAdapter extends FragmentPagerAdapter {

        public MViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }
}
