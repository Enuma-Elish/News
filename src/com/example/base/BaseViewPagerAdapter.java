package com.example.base;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseViewPagerAdapter<T> extends PagerAdapter {

	protected Context context;
	protected List<T> views;

	public BaseViewPagerAdapter(Context context, List<T> views) {
		this.context = context;
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	
	
	@Override
	public abstract void destroyItem(ViewGroup container, int position, Object object);

	@Override
	public abstract Object instantiateItem(ViewGroup container, int position);

}
