package com.example.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.base.BasePage;

public class FunctionPage extends BasePage{

	public FunctionPage(Context context) {
		super(context);
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View initView(LayoutInflater inflater) {
		TextView textView = new TextView(context);
		textView.setText("bbb");
		return textView;
	}

}
