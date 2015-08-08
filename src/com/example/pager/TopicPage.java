package com.example.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.base.BasePage;
import com.example.model.BaseModel.DataModel;
import com.example.news.R;
import com.lidroid.xutils.ViewUtils;

public class TopicPage extends BasePage {

	public TopicPage(Context context, DataModel dataModel) {
		super(context);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.layout_pager_topic, null);
		ViewUtils.inject(this, view);
		super.initTitleBar(view);
		return view;
	}

	@Override
	public void initData() {
		
	}

}
