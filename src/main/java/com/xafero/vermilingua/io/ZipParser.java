package com.xafero.vermilingua.io;

import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xafero.vermilingua.api.AbstractParser;
import com.xafero.vermilingua.api.IParser;
import com.xafero.vermilingua.util.MyUtils;

public class ZipParser extends AbstractParser implements IParser {

	private static final String CLASSES_ARRAY = "classes";

	private final Map<String, IParser> parsers;
	private final String text;

	public ZipParser(Map<String, IParser> parsers, String text) {
		this.parsers = parsers;
		this.text = text;
	}

	@Override
	public String parseWithError(String name, InputStream in) throws Exception {
		ZipInputStream jar = new ZipInputStream(in);
		JsonObject obj = new JsonObject();
		ZipEntry entry;
		// Loop for entries...
		while ((entry = jar.getNextEntry()) != null) {
			String ename = entry.getName();
			if (ename.endsWith(".class")) {
				String className = ename.replace('/', '.').replace('$', '.').replace(".class", "");
				if (!className.matches("^.+?\\.\\d$") && MyUtils.contains(className, text)) {
					JsonElement array = obj.get(CLASSES_ARRAY);
					if (array == null)
						obj.add(CLASSES_ARRAY, array = new JsonArray());
					JsonArray classes = array.getAsJsonArray();
					classes.add(className);
				}
			}
			String ext = MyUtils.getExt(ename);
			IParser parser = parsers.get(ext);
			if (parser == null)
				continue;
			String res = parser.parse(entry.getName(), jar);
			if (res != null && !res.isEmpty()) {
				JsonElement item = json.parse(res);
				obj.add(ename, item);
			}
		}
		// Ready!
		return obj.entrySet().isEmpty() ? null : gson.toJson(obj);
	}
}