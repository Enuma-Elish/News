package com.example.newscenter.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.base.BasePage;

public class TopicPage extends BasePage {

	public TopicPage(Context context) {
		super(context);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		TextView view = new TextView(context);
		view.setText("zhuanti");
		return view;
	}

	@Override
	public void initData() {
		System.out.println("zhuanti init");
	}

}
