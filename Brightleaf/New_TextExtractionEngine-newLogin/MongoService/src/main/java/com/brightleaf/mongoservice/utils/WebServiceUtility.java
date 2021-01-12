package com.brightleaf.mongoservice.utils;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class WebServiceUtility {

	private static Logger logger = Logger.getLogger(WebServiceUtility.class);

	private WebServiceUtility() {
		throw new IllegalStateException("Utility class");
	}

	public static <T> T convertJsonToObject(final String json, final Class<T> clazz) {
		Gson gson = new Gson();
		T object = null;
		try {
			object = gson.fromJson(json, clazz);
		} catch (NullPointerException | JsonSyntaxException e) {
			logger.error("convertJsonToObject", e);
		}

		return object;
	}
}
