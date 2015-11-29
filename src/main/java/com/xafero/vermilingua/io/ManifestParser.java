package com.xafero.vermilingua.io;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import com.google.gson.JsonObject;
import com.xafero.vermilingua.api.AbstractParser;
import com.xafero.vermilingua.api.IParser;
import com.xafero.vermilingua.util.MyUtils;

public class ManifestParser extends AbstractParser implements IParser {

	private final String text;

	public ManifestParser(String text) {
		this.text = text;
	}

	@Override
	public String parseWithError(String name, InputStream input) throws Exception {
		JsonObject obj = new JsonObject();
		// Parse manifest...
		Manifest manifest = new Manifest(input);
		Map<String, Attributes> attrs = new LinkedHashMap<String, Attributes>(manifest.getEntries());
		attrs.put("main", manifest.getMainAttributes());
		for (Entry<String, Attributes> e : attrs.entrySet())
			for (Entry<Object, Object> se : e.getValue().entrySet())
				if (MyUtils.contains(se.getKey() + "", text) || MyUtils.contains(se.getValue() + "", text))
					obj.addProperty(se.getKey() + "", se.getValue() + "");
		// Ready!
		return obj.entrySet().isEmpty() ? null : obj.toString();
	}
}