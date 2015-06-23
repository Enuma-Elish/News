package com.example.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BasePage {
	public Context context;
	private View view;

	public BasePage(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = initView(inflater);
	}

	public View getRootView() {
		return view;
	}

	public abstract void initData();

	public abstract View initView(LayoutInflater inflater);

}
