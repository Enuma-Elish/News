package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.example.news.R;
import com.example.util.SharePrefUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SplashActivity extends Activity {

	private static final int DELAY_TIME = 2000;
	private static final int GO_GUIDE = 1001;
	private static final int GO_MAIN = 1000;
	public static final String INIT = "init";

	@ViewInject(R.id.rl_main)
	private RelativeLayout rl_main;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_GUIDE:
				// goGuide();
				break;
			case GO_MAIN:
				goMain();
				break;
			default:
				// goGuide();
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_splash);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		initData();
		// 渐变的动画效果
		AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
		anim.setDuration(2000);
		rl_main.startAnimation(anim);
	}

	private void initData() {
		// TODO
		boolean init = SharePrefUtil.getBoolean(this, INIT, false);
		if (init) {
			handler.sendEmptyMessageDelayed(GO_GUIDE, DELAY_TIME);
			SharePrefUtil.saveBoolean(this, INIT, false);
		} else {
			handler.sendEmptyMessageDelayed(GO_MAIN, DELAY_TIME);
		}
	}

	private void goMain() {
		Intent i = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(i);
		finish();
	}

}
