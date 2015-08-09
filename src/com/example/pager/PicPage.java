package com.example.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.activity.NewsDetailsActivity;
import com.example.base.BasePage;
import com.example.constant.AppConstant;
import com.example.model.BaseModel.DataModel;
import com.example.model.Photo;
import com.example.model.Photo.Data;
import com.example.model.Photo.News;
import com.example.news.R;
import com.example.util.CommonUtil;
import com.example.util.GsonUtils;
import com.example.util.SharePrefUtil;
import com.example.view.pullrefreshview.PullToRefreshBase;
import com.example.view.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.example.view.pullrefreshview.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PicPage extends BasePage implements OnItemClickListener {

	@ViewInject(R.id.lv_pic)
	private PullToRefreshListView lv_pic;
	@ViewInject(R.id.fl_loading)
	private FrameLayout fl_loading;

	private PhotoAdapter adapter;
	private List<News> mPhotoList = new ArrayList<Photo.News>();
	private DataModel dataModel;

	private boolean first = true;
	private boolean refresh = false;

	public PicPage(Context context, DataModel dataModel) {
		super(context);
		this.dataModel = dataModel;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.layout_news_pic, null);
		ViewUtils.inject(this, view);
		super.initTitleBar(view);
		lv_pic.setScrollLoadEnabled(true);
		lv_pic.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新栏，获取服务器最新数据
				refresh = true;
				getData(HttpMethod.GET, dataModel.url, null, callBack);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 加载更多
				if (!TextUtils.isEmpty(dataCache.more)) {
					getData(HttpMethod.GET, dataCache.more, null, callBack);
				}
			}

		});
		lv_pic.getRefreshableView().setOnItemClickListener(this);
		return view;
	}

	@Override
	public void initData() {
		if (adapter == null) {
			adapter = new PhotoAdapter();
			lv_pic.getRefreshableView().setAdapter(adapter);
		}
		String cache = SharePrefUtil.getString(context, AppConstant.PIC_CACHE,
				"");
		String updateTime = SharePrefUtil.getString(context,
				AppConstant.PIC_CACHE + "update_time", "");
		if (!TextUtils.isEmpty(updateTime)) {
			lv_pic.setLastUpdatedLabel(updateTime);
		}
		if (TextUtils.isEmpty(cache)) {
			getData(HttpMethod.GET, dataModel.url, null, callBack);
		} else {
			processData(cache);
			if (first) {
				getData(HttpMethod.GET, dataModel.url, null, callBack);
			}
		}
	}

	private void processData(String json) {
		dataCache = GsonUtils.changeGsonToBean(json, Photo.class).data;
		if (dataCache != null) {
			mPhotoList.addAll(dataCache.news);
		}
		if (mPhotoList.size() > 0) {
			fl_loading.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
		}
	}

	// 设置下拉的更新时间
	public void setUpdateTime() {
		String updateTime = CommonUtil.getStringDate();
		lv_pic.setLastUpdatedLabel(updateTime);
		SharePrefUtil.saveString(context,
				AppConstant.PIC_CACHE + "update_time", updateTime); // 缓存更新时间
	}

	RequestCallBack<String> callBack = new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			if (refresh || first) {
				SharePrefUtil.saveString(context, AppConstant.PIC_CACHE,
						responseInfo.result);
				mPhotoList.clear();
				setUpdateTime();
			}
			processData(responseInfo.result);
			if (refresh || first) {
				lv_pic.onPullDownRefreshComplete();
				refresh = false;
				first = false;
			}else{
				lv_pic.onPullUpRefreshComplete();
			}
			
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			// TODO Auto-generated method stub

		}

	};
	private Data dataCache;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		News news = (News) parent.getAdapter().getItem(position);
		NewsDetailsActivity.actionStart(context, "news", news);
	}

	class PhotoAdapter extends BaseAdapter {

		private BitmapUtils utils;

		public PhotoAdapter() {
			utils = new BitmapUtils(context);
			utils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return mPhotoList.size();
		}

		@Override
		public News getItem(int position) {
			return mPhotoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			News news = mPhotoList.get(position);
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.list_photo_item,
						null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvTitle.setText(news.title);
			utils.display(holder.ivPic, news.largeimage);
			return convertView;
		}

		public class ViewHolder {
			public TextView tvTitle;
			public ImageView ivPic;
		}

	}

}
