package com.example.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.activity.MainActivity;
import com.example.news.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public abstract class BasePage implements OnClickListener {

	protected ImageButton ib_left;
	protected ImageButton ib_middle;
	protected ImageButton ib_right;
	protected TextView tv_title;

	public Context context;
	private View view;

	public BasePage(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = initView(inflater);
	}

	// 从服务器上获取数据(json)
	public void getData(HttpMethod method, String url, RequestParams params,
			RequestCallBack<String> callBack) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(method, url, params, callBack);
	}

	public View getRootView() {
		return view;
	}

	public abstract void initData();

	public abstract View initView(LayoutInflater inflater);

	public void initTitleBar(View view) {
		ib_left = (ImageButton) view.findViewById(R.id.ib_left);
		ib_middle = (ImageButton) view.findViewById(R.id.ib_middle);
		ib_right = (ImageButton) view.findViewById(R.id.ib_right);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(R.string.app_name);
		ib_left.setImageResource(R.drawable.img_menu);
		ib_left.setOnClickListener(this);
		ib_middle.setImageResource(R.drawable.ic_notification_disabled);
		ib_right.setImageResource(R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha);
		ib_right.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_left:
			((MainActivity) context).getSlidingMenu().showMenu();
			break;
		case R.id.ib_right:
			((MainActivity) context).openOptionsMenu();
			break;
		default:
			break;
		}
	}

}
