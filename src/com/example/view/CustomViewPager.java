package com.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends LazyViewPager {

	private Context context;
	private boolean touchModel;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomViewPager(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (touchModel)
			return super.onInterceptTouchEvent(ev);
		else
			return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (touchModel)
			return super.onTouchEvent(ev);
		else
			return false;
	}

}
