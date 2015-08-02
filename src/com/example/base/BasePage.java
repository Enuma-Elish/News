package com.example.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.example.news.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public abstract class BasePage {

	
	protected ImageButton imgbtn_left;
	protected ImageButton imgbtn_middle;
	protected ImageButton imgbtn_right;
	
	public Context context;
	private View view;

	public BasePage(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = initView(inflater);
		initTitleBar(view);
	}

	// 初始化标题栏
	protected void initTitleBar(View view) {
		imgbtn_left = (ImageButton) view.findViewById(R.id.imgbtn_left);
		imgbtn_middle = (ImageButton) view.findViewById(R.id.imgbtn_middle);
		imgbtn_right = (ImageButton) view.findViewById(R.id.imgbtn_right);
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

}
