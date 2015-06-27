package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.adapter.MenuListAdapter;
import com.example.base.BaseFragment;
import com.example.base.MenuSwitchListener;
import com.example.news.MainActivity;
import com.example.news.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

public class MenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_menu)
	private ListView listView;
	public List<String> list = new ArrayList<String>();
	public MenuListAdapter adapter;
	public MenuSwitchListener listener;

	@OnItemClick(R.id.lv_menu)
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		adapter.setCurrPosition(position);
		listener.menuSwitch(parent,view,position,id);
		sm.toggle();
	}

	// 初始化list的数据
	public void initMenuList(List<String> menuList) {
		list.clear();
		list.addAll(menuList);
		if (adapter == null) {
			adapter = new MenuListAdapter(context, list);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void initData(Bundle savedInstanceState) {
	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.layout_left_menu, null);
		ViewUtils.inject(this, view);
		return view;
	}

}
