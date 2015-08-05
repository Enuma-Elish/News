package com.example.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.fragment.HomeFragment;
import com.example.fragment.MenuFragment;
import com.example.news.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

	private SlidingMenu sm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.menu_content);
		setContentView(R.layout.content);

		sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT); //设置滑动的方向
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); //设置滑动模式
		MenuFragment menuFragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_content_frame, menuFragment, "Menu").commit();

		HomeFragment homeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, homeFragment).commit();
	}

	public MenuFragment getMenuFragment() {
		return (MenuFragment) getSupportFragmentManager().findFragmentByTag(
				"Menu");
	}

	public void switchFragment(Fragment f) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, f).commit();
		sm.toggle();
	}
}
