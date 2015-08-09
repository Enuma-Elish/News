package com.example.model;

import java.util.ArrayList;



public class TopicListBean extends BaseBean {
	
	private static final long serialVersionUID = 8435196329173163365L;
	public TopicList data;
	public static class TopicList{
		public String more;
		public ArrayList<Topic> topic;
//		public String countcommenturl;
	}
	
	public static class Topic{
		public String id;
		public String title;
		public String url;
		public String listimage;
		public String description;
//		public ArrayList<TopicItemNews> news;
//		public String countcommenturl;
	}
	
	
}
