package com.example.model;

import java.util.List;

public class TopicBean extends BaseBean {

	private static final long serialVersionUID = 8435196329173163365L;
	public Data data;

	public static class Data {
		public String countcommenturl;
		public List<Topic> topic;
	}

	public static class Topic {
		public String id;
		public List<News> news;
		public String title;
	}

	public static class News {
		public boolean comment;
		public String commentlist;
		public String commenturl;
		public String id;
		public String url;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
	}

}
