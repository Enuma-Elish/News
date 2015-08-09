package com.example.model;

import java.io.Serializable;
import java.util.List;

public class Photo {

	public Data data;
	public int retcode;

	public static class Data {
		public String countcommenturl;
		public String more;
		public List<News> news;
		public List<String> topic;

	}

	public static class News implements Serializable{
		public boolean comment;
		public String commentlist;
		public String commenturl;
		public int id;
		public String largeimage;
		public String listimage;
		public String pubdate;
		public String smallimage;
		public String title;
		public String type;
		public String url;
	}

}
