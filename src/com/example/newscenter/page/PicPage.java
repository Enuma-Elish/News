package com.example.newscenter.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.activity.NewsDetailsActivity;
import com.example.base.BasePage;
import com.example.home.NewsCenterPage;
import com.example.model.BaseModel.DataModel;
import com.example.model.Photo;
import com.example.model.Photo.News;
import com.example.news.R;
import com.example.util.GsonUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

public class PicPage extends BasePage {

	@ViewInject(R.id.lv_pic)
	private ListView lv_pic;
	@ViewInject(R.id.gv_pic)
	private GridView gv_pic;
	private PhotoAdapter adapter;
	private List<News> mPhotoList = new ArrayList<Photo.News>();
	private DataModel dataModel;

	public PicPage(Context context, DataModel dataModel) {
		super(context);
		this.dataModel = dataModel;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.layout_news_pic, null);
		ViewUtils.inject(this, view);
		imgbtn_right = (ImageButton) NewsCenterPage.view.findViewById(R.id.imgbtn_right);
		
		imgbtn_right.setImageResource(R.drawable.icon_pic_list_type);
		imgbtn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeDisplay();
			}
		});
		return view;
	}

	@Override
	public void initData() {

		getData(HttpMethod.GET, dataModel.url, null, callBack);
		adapter = new PhotoAdapter();
		lv_pic.setAdapter(adapter);
		gv_pic.setAdapter(adapter);
	}

	private void processData(String json) {
		mPhotoList.clear();
		mPhotoList
				.addAll(GsonUtils.changeGsonToBean(json, Photo.class).data.news);
		adapter.notifyDataSetChanged();
	}

	RequestCallBack<String> callBack = new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			processData(responseInfo.result);
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			// TODO Auto-generated method stub

		}

	};

	@OnItemClick(R.id.lv_pic)
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

	private boolean isListDisplay = true;// 是否是列表展示
	private ImageButton imgbtn_right;

	/**
	 * 切换展现方式
	 */
	private void changeDisplay() {
		if (isListDisplay) {
			isListDisplay = false;
			lv_pic.setVisibility(View.GONE);
			gv_pic.setVisibility(View.VISIBLE);
			imgbtn_right.setImageResource(R.drawable.icon_pic_list_type);

		} else {
			isListDisplay = true;
			lv_pic.setVisibility(View.VISIBLE);
			gv_pic.setVisibility(View.GONE);

			imgbtn_right.setImageResource(R.drawable.icon_pic_grid_type);
		}
	}

}
