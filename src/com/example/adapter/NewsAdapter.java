package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.base.QLBaseAdapter;
import com.example.model.NewsListBean.News;
import com.example.news.R;
import com.lidroid.xutils.BitmapUtils;

public class NewsAdapter extends QLBaseAdapter<News, ListView> {

	BitmapUtils bitmapUtil;

	public NewsAdapter(Context context, List<News> list) {
		super(context, list);
		bitmapUtil = new BitmapUtils(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View view;
		News news = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.layout_news_item, null);
			holder.iv = (ImageView) view.findViewById(R.id.iv_img);
			holder.title = (TextView) view.findViewById(R.id.tv_title);
			holder.pub_date = (TextView) view.findViewById(R.id.tv_pub_date);
			holder.comment_count = (TextView) view
					.findViewById(R.id.tv_comment_count);
			holder.iv_popicon = (ImageView) view.findViewById(R.id.iv_popicon);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.title.setText(news.title);
		holder.pub_date.setText(news.pubdate);
		if (news.comment) {
			holder.comment_count.setVisibility(View.VISIBLE);
		} else {
			holder.comment_count.setVisibility(View.INVISIBLE);
		}
		if (TextUtils.isEmpty(news.listimage)) {
			holder.iv.setVisibility(View.GONE);
		} else {
			holder.iv.setVisibility(View.VISIBLE);
			bitmapUtil.display(holder.iv, news.listimage);
		}
		return view;
	}

	class ViewHolder {
		ImageView iv;
		TextView title;
		TextView pub_date;
		TextView comment_count;
		ImageView iv_popicon;
	}

}
