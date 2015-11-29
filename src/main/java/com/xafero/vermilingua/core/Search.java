package com.xafero.vermilingua.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.google.gson.stream.JsonWriter;
import com.xafero.vermilingua.api.IParser;
import com.xafero.vermilingua.io.ManifestParser;
import com.xafero.vermilingua.io.PropsParser;
import com.xafero.vermilingua.io.TextParser;
import com.xafero.vermilingua.io.XmlParser;
import com.xafero.vermilingua.io.ZipParser;
import com.xafero.vermilingua.util.MyUtils;

public class Search {

	public static void findJARs(String dir, final String name, String clazz, String manifest, String text)
			throws IOException {
		// Set up parsers...
		final Map<String, IParser> parsers = new HashMap<>();
		parsers.put("JAR", new ZipParser(parsers, clazz));
		parsers.put("ZIP", new ZipParser(parsers, clazz));
		parsers.put("MF", new ManifestParser(manifest));
		parsers.put("TXT", new TextParser(text));
		parsers.put("INF", new TextParser(text));
		parsers.put("XML", new XmlParser(text));
		parsers.put("PROPERTIES", new PropsParser(text));
		// Start...
		final File root = new File(dir);
		PrintWriter out = new PrintWriter(System.out);
		final JsonWriter writer = new JsonWriter(out);
		writer.setIndent("  ");
		writer.beginObject();
		IOFileFilter filter = new IOFileFilter() {
			public boolean accept(File dir, String name) {
				throw new UnsupportedOperationException();
			}

			public boolean accept(File file) {
				boolean nameMatches = MyUtils.isNameMatch(file, name);
				IParser parser;
				String ext = MyUtils.getExt(file.getName());
				if (nameMatches && (parser = parsers.get(ext)) != null)
					try {
						String key = MyUtils.makeRelative(root, file);
						String res = parser.parse(file.getName(), MyUtils.toStream(file));
						if (res != null && !res.isEmpty()) {
							writer.name(key);
							writer.jsonValue(res);
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				return nameMatches;
			}
		};
		FileUtils.listFiles(root, filter, TrueFileFilter.INSTANCE);
		writer.endObject();
		writer.flush();
		writer.close();
	}
}