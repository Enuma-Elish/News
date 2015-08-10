package com.example.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.activity.NewsDetailsActivity;
import com.example.base.BasePage;
import com.example.base.QLBaseAdapter;
import com.example.model.BaseModel.DataModel;
import com.example.model.TopicBean;
import com.example.model.TopicBean.News;
import com.example.model.TopicBean.Topic;
import com.example.news.R;
import com.example.util.GsonUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class TopicPage extends BasePage {

	@ViewInject(R.id.lv_topic)
	private ListView lv_topic;
	private DataModel dataModel;
	private TopicAdapter adapter;
	private BitmapUtils bitmapUtils;
	private List<Topic> list = new ArrayList<>();

	public TopicPage(Context context, DataModel dataModel) {
		super(context);
		this.dataModel = dataModel;
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.layout_pager_topic, null);
		ViewUtils.inject(this, view);
		super.initTitleBar(view);
		return view;
	}

	@Override
	public void initData() {
		if (adapter == null) {
			adapter = new TopicAdapter(context, list);
			lv_topic.setAdapter(adapter);
		}
		getData(HttpMethod.GET, dataModel.url, null, callBack);
	}

	protected void processData(String result) {
		TopicBean topicBean = GsonUtils.changeGsonToBean(result,
				TopicBean.class);
		list.addAll(topicBean.data.topic);
		adapter.notifyDataSetChanged();
	}

	RequestCallBack<String> callBack = new RequestCallBack<String>() {
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			processData(responseInfo.result);
		}

		@Override
		public void onFailure(HttpException error, String msg) {

		}
	};

	class TopicAdapter extends QLBaseAdapter<Topic, ListView> implements
			OnClickListener {

		private Topic topic;

		public TopicAdapter(Context context, List<Topic> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			topic = list.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_topic_item, null);
				holder.tv_topic_title = (TextView) convertView
						.findViewById(R.id.tv_topic_title);
				holder.iv_head_topic = (ImageView) convertView
						.findViewById(R.id.iv_head_topic);
				holder.tv_head_topic_title = (TextView) convertView
						.findViewById(R.id.tv_head_topic_title);
				holder.lv_topic_detail = (ListView) convertView
						.findViewById(R.id.lv_topic_detail);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_topic_title.setText(topic.title);
			bitmapUtils.display(holder.iv_head_topic,
					topic.news.get(0).listimage);
			holder.iv_head_topic.setOnClickListener(this);
			holder.tv_head_topic_title.setText(topic.news.get(0).title);
			TopicItemAdapter adapter = new TopicItemAdapter(context,
					topic.news.subList(1, topic.news.size()));
			holder.lv_topic_detail.setAdapter(adapter);
			holder.lv_topic_detail.setOnItemClickListener(adapter);
			adapter.notifyDataSetChanged();
			setListViewHeight(holder.lv_topic_detail);
			return convertView;
		}

		public void setListViewHeight(ListView listView) {
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				return;
			}
			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		}

		class ViewHolder {
			public TextView tv_topic_title;
			public ImageView iv_head_topic;
			public TextView tv_head_topic_title;
			public ListView lv_topic_detail;
		}

		@Override
		public void onClick(View v) {
			NewsDetailsActivity.actionStart(context, topic.news.get(0).url);
		}

	}

	class TopicItemAdapter extends QLBaseAdapter<News, ListView> implements
			OnItemClickListener {

		public TopicItemAdapter(Context context, List<News> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			News news = list.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_topic_news_item, null);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.iv_img = (ImageView) convertView
						.findViewById(R.id.iv_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_title.setText(news.title);
			bitmapUtils.display(holder.iv_img, news.listimage);
			return convertView;
		}

		class ViewHolder {
			public ImageView iv_img;
			public TextView tv_title;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			News news = list.get(position);
			NewsDetailsActivity.actionStart(context, news.url);
		}

	}

}
