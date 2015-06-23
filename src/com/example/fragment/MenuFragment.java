package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.base.BaseFragment;
import com.example.news.MainActivity;
import com.example.news.R;

public class MenuFragment extends BaseFragment {

	private View view;
	private ListView listView;

	class ListViewOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Fragment f = null;
			switch (position) {
			case 0:
				f = new Fragment1();
				break;
			case 1:
				f = new Fragment2();
				break;
			case 2:
				f = new Fragment3();
				break;
			case 3:
				f = new Fragment4();
				break;
			case 4:
				f = new Fragment5();
				break;
			default:
				break;
			}
			switchFragment(f);
		}
	}

	private void switchFragment(Fragment f) {
		if (f != null) {
			if (getActivity() instanceof MainActivity) {
				((MainActivity) getActivity()).switchFragment(f);
			}
		}

	}

	private List initData() {
		List<String> list = new ArrayList<String>();
		list.add("text1");
		list.add("text2");
		list.add("text3");
		list.add("text4");
		list.add("text5");
		return list;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		listView = (ListView) view.findViewById(R.id.list_view);
		List initData = initData();
		listView.setAdapter(new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				initData));
		listView.setOnItemClickListener(new ListViewOnItemClickListener());
	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = LayoutInflater.from(getActivity()).inflate(R.layout.list_view,
				null);
		return view;
	}

}
