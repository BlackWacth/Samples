package com.hua.slidingviewpager.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 单向滑动 
 * @author hzw
 */
public class SlidingViewPager extends ViewPager{

	private boolean isCanScroll = false;
	private float downX, upX, moveX;

	
	public SlidingViewPager(Context context) {
		super(context);
	}
	
	public SlidingViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean isCanScroll() {
		return isCanScroll;
	}

	public void setScrollble(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(isCanScroll) {
			return super.dispatchTouchEvent(ev);
		} else {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = ev.getRawX();
				Log.i("hzw", "downX = " + downX);
				break;

			case MotionEvent.ACTION_MOVE:
				upX = ev.getRawX();
				moveX = upX - downX;
				Log.i("hzw", "upX = " + upX);
				Log.i("hzw", "moveX = " + moveX);
				if(moveX > 0) { //禁止右划
					Log.i("hzw", "-------------------------------");
					return true;
				}
				downX = ev.getRawX();
				break;
			}

			return super.dispatchTouchEvent(ev);
		}
//		return super.dispatchTouchEvent(ev);
	}
}
