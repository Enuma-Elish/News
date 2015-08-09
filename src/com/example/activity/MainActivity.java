package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
		sm.setMode(SlidingMenu.LEFT); // 设置滑动的方向
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 设置滑动模式
		MenuFragment menuFragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_content_frame, menuFragment, "Menu")
				.commit();

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

	long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.action_about:
			
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
