package com.example.activity;

import java.io.Serializable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.base.ActivityCollector;
import com.example.base.BaseActivity;
import com.example.constant.AppConstant;
import com.example.model.NewsListBean.News;
import com.example.model.NewsListBean.TopNews;
import com.example.news.R;
import com.example.util.SharePrefUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

@SuppressWarnings("deprecation")
public class NewsDetailsActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.wv_news_details)
	private WebView wv_news_details;
	@ViewInject(R.id.imgbtn_left)
	private ImageButton imgbtn_left;
	@ViewInject(R.id.imgbtn_middle)
	private ImageButton imgbtn_middle;
	@ViewInject(R.id.imgbtn_right)
	private ImageButton imgbtn_right;
	private News news;
	private TopNews topNews;
	private WebSettings settings;
	private static final String[] FONT_SIZE = new String[] { "小号字体", "中号字体",
			"大号字体", "特大号字体" };
	private int flag = 1;
	private static final TextSize[] TEXT_SIZE = new TextSize[]{TextSize.SMALLER,TextSize.NORMAL,TextSize.LARGER,TextSize.LARGEST};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_news_details);
		ActivityCollector.addActivity(this);
		ViewUtils.inject(this);
		initTiltBar();
		initData();
	}

	@Override
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	//初始化titlebar
	private void initTiltBar() {
		imgbtn_left.setImageResource(R.drawable.back);
		imgbtn_left.setOnClickListener(this);
		imgbtn_middle.setImageResource(R.drawable.icon_textsize);
		imgbtn_middle.setOnClickListener(this);
		imgbtn_right.setImageResource(R.drawable.icon_share);
		imgbtn_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_left:
			finish(); // 返回
			break;
		case R.id.imgbtn_middle:
			showFontDialog();  //显示字体选择对话框
			break;
		case R.id.imgbtn_right:
			showShare();  //显示分享对话框
			break;
		default:
			break;
		}
	}

	private void showFontDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("正文字号")
				.setSingleChoiceItems(FONT_SIZE, flag,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								flag = which;
								
							}
						})
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						settings.setTextSize(TEXT_SIZE[flag]);
						SharePrefUtil.saveInt(NewsDetailsActivity.this, AppConstant.SP_FONT_SIZE_NAME, flag);
					}
				})
				.setNegativeButton("取消",null).create();
		alertDialog.show();
	}

	private void showShare() {

		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		if (news != null) {
			// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
			oks.setTitle(news.title);
			// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
			oks.setTitleUrl(news.url);
			// text是分享文本，所有平台都需要这个字段
			oks.setText(getResources().getString(R.string.share_content)
					+ news.title + "，详情见：" + news.url);
			// url仅在微信（包括好友和朋友圈）中使用
			oks.setUrl(news.url);
		} else if (topNews != null) {
			oks.setTitle(topNews.title);
			oks.setTitleUrl(topNews.url);
			oks.setText(getResources().getString(R.string.share_content)
					+ topNews.title + "，详情见：" + topNews.url);
			oks.setUrl(topNews.url);
		}

		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片

		// 启动分享GUI
		oks.show(this);
	}

	private void initData() {
		String url = null;
		news = (News) getIntent().getSerializableExtra("news");
		topNews = (TopNews) getIntent().getSerializableExtra("topNews");
		if (news != null) {
			url = news.url;
		} else if (topNews != null) {
			url = topNews.url;
		}
		settings = wv_news_details.getSettings();
		settings.setJavaScriptEnabled(true);
		flag = SharePrefUtil.getInt(NewsDetailsActivity.this, AppConstant.SP_FONT_SIZE_NAME, 1);
		settings.setTextSize(TEXT_SIZE[flag]);
		wv_news_details.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		if (URLUtil.isNetworkUrl(url)) {
			wv_news_details.loadUrl(url);
		}
	}

	public static void actionStart(Context context, String name,
			Serializable serializable) {
		Intent intent = new Intent();
		intent.setClass(context, NewsDetailsActivity.class);
		intent.putExtra(name, serializable);
		context.startActivity(intent);
	}

}
