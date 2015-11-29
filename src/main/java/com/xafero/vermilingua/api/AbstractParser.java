package com.xafero.vermilingua.api;

import java.io.InputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public abstract class AbstractParser implements IParser {

	protected final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
	protected final JsonParser json = new JsonParser();

	@Override
	public String parse(String name, InputStream input) {
		try {
			return parseWithError(name, input);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public abstract String parseWithError(String name, InputStream input) throws Exception;
}