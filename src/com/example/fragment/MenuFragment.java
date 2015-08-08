package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.base.BaseFragment;
import com.example.base.MenuSwitchListener;
import com.example.base.QLBaseAdapter;
import com.example.news.R;
import com.example.view.ToggleButton;
import com.example.view.ToggleButton.OnToggleChangeListener;
import com.example.view.ToggleButton.ToggleState;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

public class MenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_menu)
	private ListView listView;
	@ViewInject(R.id.tb_night_mode)
	private ToggleButton tb_night_mode;
	@ViewInject(R.id.tv_night_mode)
	private TextView tv_night_mode;

	public List<String> list = new ArrayList<String>();

	public MenuListAdapter adapter;
	public MenuSwitchListener listener;

	@OnItemClick(R.id.lv_menu)
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		adapter.setCurrPosition(position);
		listener.menuSwitch(parent, view, position, id);
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
		tb_night_mode.setState(ToggleState.Close);
		tv_night_mode.setText(R.string.day_mode);
		tb_night_mode.setOnToggleChangeListener(new OnToggleChangeListener() {
			@Override
			public void toggleChange(ToggleState state) {
				if (state == ToggleState.Close) {
					tv_night_mode.setText(R.string.day_mode);
				} else {
					tv_night_mode.setText(R.string.night_mode);
				}
			}
		});
		return view;
	}

	public class MenuListAdapter extends QLBaseAdapter<String, ListView> {

		public MenuListAdapter(Context context, List<String> list) {
			super(context, list);
		}

		private int currPosition = 0;

		public int getCurrPosition() {
			return currPosition;
		}

		public void setCurrPosition(int currPosition) {
			this.currPosition = currPosition;
			this.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(context).inflate(
						R.layout.layout_item_menu, null, false);
				viewHolder.iv_menu_item = (ImageView) view
						.findViewById(R.id.iv_menu_item);
				viewHolder.tv_menu_item = (TextView) view
						.findViewById(R.id.tv_menu_item);
				view.setTag(viewHolder);
			} else {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.tv_menu_item.setText(list.get(position));
			if (currPosition == position) {
				viewHolder.tv_menu_item.setTextColor(context.getResources()
						.getColor(R.color.red));
				viewHolder.iv_menu_item
						.setImageResource(R.drawable.menu_arr_select);
				view.setBackgroundResource(R.drawable.menu_item_bg_select);
			} else {
				viewHolder.tv_menu_item.setTextColor(context.getResources()
						.getColor(R.color.white));
				viewHolder.iv_menu_item
						.setImageResource(R.drawable.menu_arr_normal);
				view.setBackgroundResource(R.drawable.transparent);
			}
			return view;
		}

		public class ViewHolder {
			public ImageView iv_menu_item;
			public TextView tv_menu_item;
		}

	}

}
