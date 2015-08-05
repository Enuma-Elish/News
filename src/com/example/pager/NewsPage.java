package com.example.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.activity.MainActivity;
import com.example.base.BasePage;
import com.example.base.BaseViewPagerAdapter;
import com.example.model.BaseModel.DataModel;
import com.example.news.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qianlong.android.view.pagerindicator.TabPageIndicator;

public class NewsPage extends BasePage implements OnPageChangeListener {

	@ViewInject(R.id.pager)
	private ViewPager pager;
	@ViewInject(R.id.indicator)
	private TabPageIndicator indicator;
	@ViewInject(R.id.iv_edit_cate)
	private ImageView iv_edit_cate;
	
	private NewsPageAdapter adapter;
	private DataModel dataModel;
	private List<ItemNewsPage> pages = new ArrayList<ItemNewsPage>();
	private ItemNewsPage itemNewsPage;

	public NewsPage(Context context, DataModel dataModel) {
		super(context);
		this.dataModel = dataModel;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frag_news, null);
		ViewUtils.inject(this, view);
		super.initTitleBar(view);
		iv_edit_cate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(pager.getCurrentItem() == pages.size() -1){
					Toast.makeText(context, R.string.no_more, Toast.LENGTH_SHORT).show();
				}else{
					pager.setCurrentItem(pager.getCurrentItem() + 1, true);
				}
			}
		});
		return view;
	}
	
	@Override
	public void initData() {
		pages.clear();
		for (int i = 0; i < dataModel.children.size(); i++) {
			pages.add(new ItemNewsPage(context, dataModel.children.get(i)));
		}
		adapter = new NewsPageAdapter(context, pages);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
		indicator.setOnPageChangeListener(this);
		pages.get(0).initData();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		if (arg0 == 0) {
			((MainActivity) context).getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			((MainActivity) context).getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_NONE);
		}
		itemNewsPage = pages.get(arg0);
		itemNewsPage.initData();
	}

	class NewsPageAdapter extends BaseViewPagerAdapter<ItemNewsPage> {

		public NewsPageAdapter(Context context, List<ItemNewsPage> pages) {
			super(context, pages);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			int size = dataModel.children.size();
			return dataModel.children.get(position % size).title;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(views.get(position).getRootView());
			return views.get(position).getRootView();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(super.views.get(position)
					.getRootView());
		}

	}

}
