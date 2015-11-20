package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.base.QLBaseAdapter;
import com.example.model.NewsListBean.News;
import com.example.news.R;
import com.example.util.SharePrefUtil;
import com.lidroid.xutils.BitmapUtils;

public class NewsAdapter extends QLBaseAdapter<News, ListView> {

	private BitmapUtils bitmapUtil;
	private String title;
	
	public NewsAdapter(Context context, List<News> list,String title) {
		super(context, list);
		this.title = title;
		bitmapUtil = new BitmapUtils(context);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View view;
		News news = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.list_news_item, null);
			holder.iv = (ImageView) view.findViewById(R.id.iv_img);
			holder.title = (TextView) view.findViewById(R.id.tv_title);
			holder.pub_date = (TextView) view.findViewById(R.id.tv_pub_date);
			holder.comment_count = (TextView) view
					.findViewById(R.id.tv_comment_count);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		String read = SharePrefUtil.getString(context, title + "read", "");
		if(read.contains(String.valueOf(news.id))){
			holder.title.setTextColor(R.color.news_item_has_read_textcolor);
		}else{
			holder.title.setTextColor(Color.BLACK);
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
	}

}
