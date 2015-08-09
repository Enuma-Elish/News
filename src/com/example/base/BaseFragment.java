package com.example.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.activity.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public abstract class BaseFragment extends Fragment {

	public View view;
	public Context context;
	public SlidingMenu sm;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData(savedInstanceState);
		super.onActivityCreated(savedInstanceState);
	}

	public abstract void initData(Bundle savedInstanceState);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = getActivity();
		if(context instanceof MainActivity){
			sm = ((MainActivity)context).getSlidingMenu();
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(inflater, container, savedInstanceState);
		return view;
	}

	public abstract View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);

}
