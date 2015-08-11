package com.example.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.NewsDetailsActivity;
import com.example.adapter.NewsAdapter;
import com.example.base.BasePage;
import com.example.model.BaseModel.ChildrenModel;
import com.example.model.NewsListBean;
import com.example.model.NewsListBean.News;
import com.example.model.NewsListBean.TopNews;
import com.example.news.R;
import com.example.util.CommonUtil;
import com.example.util.QLParser;
import com.example.util.SharePrefUtil;
import com.example.view.RollViewPager;
import com.example.view.RollViewPager.OnPagerClickCallback;
import com.example.view.pullrefreshview.PullToRefreshBase;
import com.example.view.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.example.view.pullrefreshview.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

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

	private boolean isRefreshing; // 是否正在更新
	private boolean isLoaded; // 是否为已加载
	private boolean isMore; // 是否更多
	private boolean isLoadFirstPage; // 默认缓存第一页

	private List<TopNews> topNewsList;
	private List<News> newsList;
	private NewsListBean newsListBean;
	private List<View> dots;
	private RollViewPager pager;
	private List<String> uriList;
	private List<String> titleList;
	private NewsAdapter adapter;
	private List<News> list = new ArrayList<News>();

	public ItemNewsPage(Context context, ChildrenModel children) {
		super(context);
		this.children = children;
	}

	@Override
	public void initData() {
		String cache = SharePrefUtil.getString(context, children.title, null);
		String updateTime = SharePrefUtil.getString(context, children.title
				+ "update_time", null);
		if (!TextUtils.isEmpty(updateTime)) {
			lv_item_news.setLastUpdatedLabel(updateTime);
		}
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}
		if (!isLoaded && CommonUtil.isNetworkAvailable(context) != 0) {
			getData(HttpMethod.GET, children.url, null, callBack);
		}
	}

	// 服务器上获取数据的接口回调
	RequestCallBack<String> callBack = new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			if (responseInfo.result.startsWith("{")
					&& responseInfo.result.endsWith("}")) {
				processData(responseInfo.result);
				if (!isMore) {
					setUpdateTime();
				}
				if (isMore) {
					isMore = false;
				}
				isLoaded = true;
				if (!isLoadFirstPage) {
					SharePrefUtil.saveString(context, children.title,
							responseInfo.result);
					isLoadFirstPage = true;
				}
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
		}
	};

	// 设置下拉的更新时间
	public void setUpdateTime() {
		String updateTime = CommonUtil.getStringDate();
		lv_item_news.setLastUpdatedLabel(updateTime);
		SharePrefUtil.saveString(context, children.title + "update_time",
				updateTime); // 缓存更新时间
	}

	// 处理获取数据
	private void processData(String result) {
		newsListBean = QLParser.parse(result, NewsListBean.class);
		if (newsListBean.retcode == 200) {
			if (TextUtils.isEmpty(newsListBean.data.more)) {
				lv_item_news.setHasMoreData(false);
			} else {
				lv_item_news.setHasMoreData(true);
			}
			if (!isMore) {
				topNewsList = newsListBean.data.topnews;
				processTopNews(topNewsList);
			}
			newsList = newsListBean.data.news;
			processNews(newsList);
		}
		if (isRefreshing) {
			// 完成刷新后，将下拉刷新栏收起
			lv_item_news.onPullDownRefreshComplete();
			isRefreshing = false;
		}
	}

	private void processNews(List<News> newsList) {
		if (!isMore) {
			list.clear();
		}
		if (newsList == null) {
			loading_view.setVisibility(View.VISIBLE);
			return;
		}
		for (News n : newsList) {
			list.add(n);
		}
		if (adapter == null) {
			adapter = new NewsAdapter(context, list, children.title);
			lv_item_news.getRefreshableView().setAdapter(adapter);
		}
		if (isMore) {
			lv_item_news.onPullUpRefreshComplete();
		}
		loading_view.setVisibility(View.GONE);
		adapter.notifyDataSetChanged();
	}

	// 初始化RollViewPager
	private void processTopNews(List<TopNews> topNewsList) {
		initDots();
		top_news_viewpager.removeAllViews();
		uriList = new ArrayList<String>();
		titleList = new ArrayList<String>();
		for (TopNews topNews : topNewsList) {
			uriList.add(topNews.topimage);
			titleList.add(topNews.title);
		}
		pager = new RollViewPager(context, dots, DOT_FOCUS_ID, DOT_NORMAL_ID,
				onPagerClickCallback);
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

	// 点击图片事件
	OnPagerClickCallback onPagerClickCallback = new OnPagerClickCallback() {

		@Override
		public void onPagerClick(int position) {
			TopNews topNews = topNewsList.get(position);
			NewsDetailsActivity.actionStart(context, topNews.url);
		}
	};

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.layout_roll_view, null);
		View newsItemList = inflater.inflate(R.layout.frag_item_news, null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, newsItemList);
		// 滚动到底自动加载可用
		lv_item_news.setScrollLoadEnabled(true);
		lv_item_news.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新栏，获取服务器最新数据
				if (CommonUtil.isNetworkAvailable(context) != 0) {
					isRefreshing = true;
					getData(HttpMethod.GET, children.url, null, callBack);
				} else {
					Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
					lv_item_news.onPullDownRefreshComplete();
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 加载更多
				isMore = true;
				getData(HttpMethod.GET, newsListBean.data.more, null, callBack);

			}

		});
		lv_item_news.getRefreshableView().setOnItemClickListener(this);
		lv_item_news.getRefreshableView().addHeaderView(view);
		return newsItemList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		News news = (News) parent.getAdapter().getItem(position);
		String read = SharePrefUtil.getString(context, children.title + "read", "");
		if (!read.contains(String.valueOf(news.id))) {
			read = read + news.id + ",";
			SharePrefUtil.saveString(context, children.title + "read",
					read);
		}
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setTextColor(R.color.news_item_has_read_textcolor);
		NewsDetailsActivity.actionStart(context, news.url);
	}
}
