package com.example.newscenter.page;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Comment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.NewsAdapter;
import com.example.base.BasePage;
import com.example.base.QLBaseAdapter;
import com.example.model.BaseModel.ChildrenModel;
import com.example.model.NewsListBean.News;
import com.example.model.NewsListBean.TopNews;
import com.example.model.CountBean;
import com.example.model.NewsListBean;
import com.example.news.R;
import com.example.util.CommonUtil;
import com.example.util.GsonUtils;
import com.example.util.QLParser;
import com.example.util.SharePrefUtil;
import com.example.view.RollViewPager;
import com.example.view.RollViewPager.OnPagerClickCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.qianlong.android.view.pullrefreshview.PullToRefreshListView;

public class ItemNewsPage extends BasePage implements OnItemClickListener {

	private ChildrenModel children;
	private View view;

	@ViewInject(R.id.top_news_viewpager)
	private LinearLayout top_news_viewpager;
	@ViewInject(R.id.top_news_title)
	private TextView top_news_title;
	@ViewInject(R.id.dots_ll)
	private LinearLayout dots_ll;
	@ViewInject(R.id.lv_item_news)
	private PullToRefreshListView lv_item_news;
	@ViewInject(R.id.loading_view)
	private FrameLayout loading_view;

	private static final int DOT_FOCUS_ID = R.drawable.dot_focus;
	private static final int DOT_NORMAL_ID = R.drawable.dot_normal;

	private boolean isLoadFirstPage; // 默认缓存第一页

	public ItemNewsPage(Context context, ChildrenModel children) {
		super(context);
		this.children = children;
	}

	@Override
	public void initData() {
		String cache = SharePrefUtil.getString(context, children.title, null);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}
		getData(HttpMethod.GET, children.url, callBack);
	}

	// 服务器上获取数据的接口回调
	RequestCallBack<String> callBack = new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			processData(responseInfo.result);
			if (!isLoadFirstPage) {
				SharePrefUtil.saveString(context, children.title,
						responseInfo.result);
				isLoadFirstPage = true;
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
		}
	};
	
	// 服务器上获取评论量数据的接口回调
		RequestCallBack<String> commentCallBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				processComment(responseInfo.result);
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
			}
		};

	private CountBean countBean;
	private List<TopNews> topNewsList;
	private List<News> newsList;
	private NewsListBean newsListBean;
	private List<View> dots;
	private RollViewPager pager;
	private List<String> uriList;
	private List<String> titleList;
	private NewsAdapter adapter;
	private List<News> list = new ArrayList<News>();

	// 处理获取数据
	private void processData(String result) {
		newsListBean = QLParser.parse(result, NewsListBean.class);
		if (newsListBean.retcode == 200) {
			topNewsList = newsListBean.data.topnews;
			newsList = newsListBean.data.news;
			processTopNews(topNewsList);
			processNews(newsList);
		}
	}

	private void processNews(List<News> newsList) {
		list.clear();
		if (newsList == null) {
			loading_view.setVisibility(View.VISIBLE);
			return;
		}
		for (News n : newsList) {
			getData(HttpMethod.GET, n.commenturl+n.id, commentCallBack);
			list.add(n);
		}
		if (adapter == null) {
			adapter = new NewsAdapter(context, list);
			lv_item_news.getRefreshableView().setAdapter(adapter);
		}
		loading_view.setVisibility(View.GONE);
		lv_item_news.getRefreshableView().addHeaderView(view);

		adapter.notifyDataSetChanged();
	}
	
	//处理服务器返回的评论数据
	private void processComment(String result) {
		countBean = QLParser.parse(result, CountBean.class);
		
	}

	private void processTopNews(List<TopNews> topNewsList) {
		initDots();
		pager = new RollViewPager(context, dots, DOT_FOCUS_ID, DOT_NORMAL_ID,
				onPagerClickCallback);
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

	// 初始化Dots
	private void initDots() {
		View view;
		dots = new ArrayList<View>();
		dots_ll.removeAllViews();
		LinearLayout.LayoutParams layoutParams = new LayoutParams(
				CommonUtil.dip2px(context, 6), CommonUtil.dip2px(context, 6));
		layoutParams.setMargins(5, 0, 5, 0);
		for (int i = 0; i < topNewsList.size(); i++) {
			view = new View(context);
			view.setLayoutParams(layoutParams);
			if (i == 0) {
				view.setBackgroundResource(DOT_FOCUS_ID);
			} else {
				view.setBackgroundResource(DOT_NORMAL_ID);
			}
			dots_ll.addView(view);
			dots.add(view);
		}
	}

	OnPagerClickCallback onPagerClickCallback = new OnPagerClickCallback() {

		@Override
		public void onPagerClick(int position) {
			// TODO 点击图片事件
		}
	};

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.layout_roll_view, null);
		View newsItemList = inflater.inflate(R.layout.frag_item_news, null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, newsItemList);
		// 上拉加载不可用
		lv_item_news.setPullLoadEnabled(false);
		// 滚动到底自动加载可用
		lv_item_news.setScrollLoadEnabled(true);
		lv_item_news.getRefreshableView().setOnItemClickListener(this);
		return newsItemList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 新闻条目点击事件
	}

}
