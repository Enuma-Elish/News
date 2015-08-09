package com.example.model;

import java.util.List;

public class BaseModel extends BaseBean{

	private static final long serialVersionUID = -3792382308817687402L;
	public List<DataModel> data;
	public List<Integer> extend;

	public static class DataModel {
		public List<ChildrenModel> children;
		public int id;
		public String title;
		public int type;
		public String url;
		public String dayurl;
		public String excurl;
		public String weekurl;
	}

	public static class ChildrenModel {
		public int id;
		public String title;
		public int type;
		public String url;
	}

}
