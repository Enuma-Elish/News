package com.example.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.base.BaseActivity;
import com.example.constant.AppConstant;
import com.example.news.R;
import com.example.util.SharePrefUtil;
import com.example.view.SettingsItemView;
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
	@ViewInject(R.id.siv_font_size)
	private SettingsItemView siv_font_size;
	@ViewInject(R.id.siv_network)
	private SettingsItemView siv_network;
	@ViewInject(R.id.siv_clear_cache)
	private SettingsItemView siv_clear_cache;
	
	private static final String[] FONT_SIZE = new String[] { "小号字体", "中号字体",
		"大号字体", "特大号字体" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
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
		int flag = SharePrefUtil.getInt(SettingsActivity.this,
				AppConstant.SP_FONT_SIZE_NAME, 1);
		siv_font_size.tvDesc.setText(FONT_SIZE[flag]);
		siv_network.tvDesc.setText("较省流量");
		siv_clear_cache.tvDesc.setText("当前缓存：12.4M");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

}
