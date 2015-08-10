package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsListBean extends BaseBean {

	private static final long serialVersionUID = -6537487173976403255L;
	public NewsList data;

	public static class NewsList {
		public String more;
		public String title;
		public ArrayList<TopNews> topnews;
		public ArrayList<News> news;
		public String countcommenturl;
	}

	public static class TopNews implements Serializable {

		private static final long serialVersionUID = 7982934160342807754L;
		public int id;
		public String title;
		public String topimage;
		public String url;
		public String pubdate;
		public boolean comment;
		public String commenturl;
		public String commentlist;
		// public int commentcount;
		public String type;
	}

	public static class News implements Serializable {

		private static final long serialVersionUID = -101034706063311346L;
		public int id;
		public String title;
		public String url;
		public String listimage;
		public String pubdate;
		public int commentcount;
		public boolean comment;
		public String commenturl;
		public String commentlist;
		public String type;
	}

	public class BaseNews implements Serializable {

		private static final long serialVersionUID = -7132125301468048755L;

	}
}
