package com.example.fragment;

import com.example.base.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment1 extends BaseFragment {

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TextView textview = new TextView(getActivity());
		textview.setText(Fragment1.class.getSimpleName());
		return textview;
	}

}
