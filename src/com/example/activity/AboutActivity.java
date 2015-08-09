package com.example.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.base.BaseActivity;
import com.example.news.R;
import com.example.view.AboutItemView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AboutActivity extends BaseActivity {

	@ViewInject(R.id.ib_left)
	private ImageButton ib_left;
	@ViewInject(R.id.ib_middle)
	private ImageButton ib_middle;
	@ViewInject(R.id.ib_right)
	private ImageButton ib_right;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;

	@ViewInject(R.id.aiv_app_name)
	private AboutItemView aiv_app_name;
	@ViewInject(R.id.aiv_version)
	private AboutItemView aiv_version;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		try {
			initView();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initView() throws NameNotFoundException {
		ViewUtils.inject(this);
		tv_title.setText(R.string.action_about);
		ib_left.setImageResource(R.drawable.back);
		ib_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		aiv_app_name.tvDesc.setText(R.string.app_name);
		aiv_version.tvDesc.setText(getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

}
