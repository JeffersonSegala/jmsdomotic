package br.com.segala.domotica.utils;

import com.google.gson.Gson;

public abstract class StringUtil {

	private static final Gson gson = new Gson();
	
	public static <T> T jsonToObject(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);	
	}
	
}
