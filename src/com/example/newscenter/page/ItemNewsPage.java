package com.example.newscenter.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.base.BasePage;
import com.example.model.BaseModel.ChildrenModel;
import com.example.model.NewsListBean.TopNews;
import com.example.model.NewsListBean;
import com.example.news.R;
import com.example.util.CommonUtil;
import com.example.util.GsonUtils;
import com.example.util.QLParser;
import com.example.view.RollViewPager;
import com.example.view.RollViewPager.OnPagerClickCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ItemNewsPage extends BasePage {

	private ChildrenModel children;
	private View view;
	
	@ViewInject(R.id.top_news_viewpager)
	private LinearLayout top_news_viewpager;
	@ViewInject(R.id.top_news_title)
	private TextView top_news_title;
	@ViewInject(R.id.dots_ll)
	private LinearLayout dots_ll;
	
	private static final int DOT_FOCUS_ID =  R.drawable.dot_focus;
	private static final int DOT_NORMAL_ID =  R.drawable.dot_normal;
	
	public ItemNewsPage(Context context, ChildrenModel children) {
		super(context);
		this.children = children;
	}

	@Override
	public void initData() {
		getData(HttpMethod.GET, children.url, callback);

	}

	// 服务器上获取数据的接口回调
	RequestCallBack<String> callback = new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			processData(responseInfo.result);
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
		}
	};
	
	private List<TopNews> topNewsList;
	private NewsListBean newsListBean;
	private List<View> dots;
	private RollViewPager pager;
	private List<String> uriList;
	private List<String> titleList;

	// 处理获取数据
	private void processData(String result) {
		newsListBean = QLParser.parse(result, NewsListBean.class);
		if (newsListBean.retcode == 200) {
			topNewsList = newsListBean.data.topnews;
			processTopNews(topNewsList);
		}
	}

	private void processTopNews(List<TopNews> topNewsList) {
		initDots();
		pager = new RollViewPager(context, dots, DOT_FOCUS_ID, DOT_NORMAL_ID, onPagerClickCallback);
		uriList = new ArrayList<String>();
		titleList = new ArrayList<String>();
		for (TopNews topNews : topNewsList) {
			uriList.add(topNews.topimage);
			titleList.add(topNews.title);
		}
		pager.setUriList(uriList);
		pager.setTitle(top_news_title, titleList);
		pager.startRoll();
		top_news_viewpager.addView(pager);
	}
	
	//初始化Dots
	private void initDots() {
		View view;
		dots = new ArrayList<View>();
		dots_ll.removeAllViews();
		LinearLayout.LayoutParams layoutParams = new LayoutParams(CommonUtil.dip2px(context, 6), CommonUtil.dip2px(context, 6));
		layoutParams.setMargins(5, 0, 5, 0);
		for (int i = 0; i < topNewsList.size(); i++) {
			view = new View(context);
			view.setLayoutParams(layoutParams);
			if(i == 0){
				view.setBackgroundResource(DOT_FOCUS_ID);
			}else{
				view.setBackgroundResource(DOT_NORMAL_ID);
			}
			dots_ll.addView(view);
			dots.add(view);
		}
	}

	OnPagerClickCallback onPagerClickCallback = new OnPagerClickCallback() {
		
		@Override
		public void onPagerClick(int position) {
			
		}
	};

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.layout_roll_view, null);
		ViewUtils.inject(this, view);
		return view;
	}

}
