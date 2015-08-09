package com.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.news.R;

public class AboutItemView extends LinearLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
	public TextView tvTitle;
	public TextView tvDesc;
	private String mTitle;
	private String mDesc;

	public AboutItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public AboutItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTitle = attrs.getAttributeValue(NAMESPACE, "title");// 根据属性名称,获取属性的值
		mDesc = attrs.getAttributeValue(NAMESPACE, "desc");
		initView();
	}

	public AboutItemView(Context context) {
		super(context);
		initView();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		// 将自定义好的布局文件设置给当前的SettingItemView
		View.inflate(getContext(), R.layout.list_about_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		setTitle(mTitle);// 设置标题
		setDesc(mDesc);
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}

}
