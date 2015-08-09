package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.activity.MainActivity;
import com.example.base.BaseFragment;
import com.example.base.BasePage;
import com.example.base.MenuSwitchListener;
import com.example.constant.AppConstant;
import com.example.model.BaseModel;
import com.example.model.BaseModel.DataModel;
import com.example.news.R;
import com.example.pager.InteractPage;
import com.example.pager.NewsPage;
import com.example.pager.PicPage;
import com.example.pager.TopicPage;
import com.example.pager.VotePage;
import com.example.util.QLParser;
import com.example.util.SharePrefUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HomeFragment extends BaseFragment implements MenuSwitchListener {

	private List<String> menuNewsList = new ArrayList<String>();
	private MenuFragment menuFragment;
	private boolean firstGetData = true;
	private boolean firstLoad = true;
	private List<BasePage> pageList = new ArrayList<BasePage>();
	private BasePage basePage;
	@ViewInject(R.id.fl_news_center)
	private FrameLayout fl_news_center;

	@Override
	public void initData(Bundle savedInstanceState) {
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

	// 从服务器上获取数据(json)
	public void getData(HttpMethod method, String url, RequestParams params,
			RequestCallBack<String> callBack) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(method, url, params, callBack);
	}

	// 获取网络数据后接口回调
	RequestCallBack<String> callBack = new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			if (responseInfo.result.startsWith("{")
					&& responseInfo.result.endsWith("}")) {
				processData(responseInfo.result);
				SharePrefUtil.saveString(context,
						AppConstant.NEWS_CENTER_CACHE, responseInfo.result);
			}
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

	// slidingmenu的接口回调
	@Override
	public void menuSwitch(AdapterView<?> parent, View view, int position,
			long id) {
		basePage = pageList.get(position);
		fl_news_center.removeAllViews();
		fl_news_center.addView(basePage.getRootView());
		basePage.initData();
	}

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.news_center_frame, null);
		ViewUtils.inject(this, view);
		return view;
	}

}
