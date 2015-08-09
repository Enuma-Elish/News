package com.example.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.base.BaseActivity;
import com.example.news.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SettingsActivity extends BaseActivity {

	@ViewInject(R.id.ib_left)
	private ImageButton ib_left;
	@ViewInject(R.id.ib_middle)
	private ImageButton ib_middle;
	@ViewInject(R.id.ib_right)
	private ImageButton ib_right;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_settings);
		initView();
	}

	private void initView() {
		ViewUtils.inject(this);
		tv_title.setText(R.string.action_settings);
		ib_left.setImageResource(R.drawable.back);
		ib_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

}
