package com.example.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.model.BaseBean;

import android.text.TextUtils;

public class QLParser {
	public static <T extends BaseBean> T parse(String jsonString, Class<T> cls) {
		if (jsonString != null) {
			String json = beforeParse(jsonString);
			if (!TextUtils.isEmpty(json))
				return GsonUtils.changeGsonToBean(json, cls);
		}
		return null;

	}

	private static String beforeParse(String jsonString) {
		int code;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			code = jsonObject.getInt("retcode");
			if (code == 200) {
				System.out.println(jsonString);
				return jsonString;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
