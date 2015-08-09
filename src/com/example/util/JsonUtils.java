package com.example.util;

import com.google.gson.Gson;

public class JsonUtils {

	public static <T> T jsonToBean(String result, Class<T> cls) {
		Gson gson = new Gson();
		T t = gson.fromJson(result, cls);
		return t;
	}

}
