package com.xafero.vermilingua.io;

import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import com.google.gson.JsonObject;
import com.xafero.vermilingua.api.AbstractParser;
import com.xafero.vermilingua.api.IParser;
import com.xafero.vermilingua.util.MyUtils;

public class PropsParser extends AbstractParser implements IParser {

	private final String text;

	public PropsParser(String text) {
		this.text = text;
	}

	@Override
	public String parseWithError(String name, InputStream input) throws Exception {
		JsonObject results = new JsonObject();
		Properties props = new Properties();
		props.load(input);
		for (Entry<Object, Object> e : props.entrySet())
			if (MyUtils.contains(e.getKey() + "", text) || MyUtils.contains(e.getValue() + "", text))
				results.addProperty(e.getKey() + "", e.getValue() + "");
		return results.entrySet().isEmpty() ? null : results.toString();
	}
}