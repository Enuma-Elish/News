package com.example.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.base.BasePage;
import com.example.base.MenuSwitchListener;
import com.example.constant.AppConstant;
import com.example.fragment.MenuFragment;
import com.example.model.BaseModel;
import com.example.model.BaseModel.DataModel;
import com.example.news.MainActivity;
import com.example.news.R;
import com.example.newscenter.page.InteractPage;
import com.example.newscenter.page.NewsPage;
import com.example.newscenter.page.PicPage;
import com.example.newscenter.page.TopicPage;
import com.example.newscenter.page.VotePage;
import com.example.util.QLParser;
import com.example.util.SharePrefUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NewsCenterPage extends BasePage implements MenuSwitchListener {

	private List<String> menuNewsList = new ArrayList<String>();
	private MenuFragment menuFragment;
	private boolean firstGetData = true;
	private boolean firstLoad = true;
	private List<BasePage> pageList = new ArrayList<BasePage>();
	private BasePage basePage;
	@ViewInject(R.id.news_center_fl)
	private FrameLayout news_center_fl;

	public NewsCenterPage(Context context) {
		super(context);
	}

	// 初始化数据
	@Override
	public void initData() {
		if (menuNewsList.size() == 0) {
			String cache = SharePrefUtil.getString(context,
					AppConstant.NEWS_CENTER_CACHE, null);
			if (!TextUtils.isEmpty(cache)) {
				processData(cache);
			}
		}
		if (firstGetData) {
			getData(HttpMethod.GET, AppConstant.NEWS_CENTER_URL, null, callBack);
			firstGetData = false;
		}
	}

	// 获取网络数据后接口回调
	RequestCallBack<String> callBack = new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			processData(responseInfo.result);
			SharePrefUtil.saveString(context, AppConstant.NEWS_CENTER_CACHE,
					responseInfo.result);
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
		}

	};

	// 处理网络数据
	private void processData(String result) {
		BaseModel baseModel = QLParser.parse(result, BaseModel.class);
		if (baseModel.retcode != 200) {
			Toast.makeText(context, "服务器错误", Toast.LENGTH_SHORT).show();
			return;
		}
		if (menuNewsList.size() == 0) {
			List<DataModel> data = baseModel.data;
			for (DataModel dataModel : data) {
				menuNewsList.add(dataModel.title);
			}
		}
		changeMenuList(menuNewsList);
		pageList.clear();
		BasePage newsPage = new NewsPage(context, baseModel.data.get(0));
		BasePage topicPage = new TopicPage(context, baseModel.data.get(1));
		BasePage picPage = new PicPage(context, baseModel.data.get(2));
		BasePage interactPage = new InteractPage(context, baseModel.data.get(3));
		BasePage votePage = new VotePage(context, baseModel.data.get(4));
		pageList.add(newsPage);
		pageList.add(topicPage);
		pageList.add(picPage);
		pageList.add(interactPage);
		pageList.add(votePage);
		if (firstLoad) {
			this.menuSwitch(null, null, 0, 0l);
			firstLoad = false;
		}
		// TODO
	}

	// 把menuNewsList的数据传递给MenuFragment，并初始化MenuList的数据
	private void changeMenuList(List<String> menuNewsList) {
		menuFragment = ((MainActivity) context).getMenuFragment();
		menuFragment.initMenuList(menuNewsList);
		menuFragment.listener = this;
	}

	// 初始化view
	@Override
	public View initView(LayoutInflater inflater) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.news_center_frame, null);
		ViewUtils.inject(this, view);
		return view;
	}

	// slidingmenu的接口回调
	@Override
	public void menuSwitch(AdapterView<?> parent, View view, int position,
			long id) {
		basePage = pageList.get(position);
		news_center_fl.removeAllViews();
		news_center_fl.addView(basePage.getRootView());
		basePage.initData();
	}

}
