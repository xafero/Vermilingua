package com.xafero.vermilingua.io;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.xafero.vermilingua.api.AbstractParser;
import com.xafero.vermilingua.api.IParser;
import com.xafero.vermilingua.util.MyUtils;

public class TextParser extends AbstractParser implements IParser {

	private final String text;

	public TextParser(String text) {
		this.text = text;
	}

	@Override
	public String parseWithError(String name, InputStream input) throws Exception {
		List<String> lines = new LinkedList<>();
		for (String line : IOUtils.readLines(input, "UTF8"))
			if (MyUtils.contains(line, text))
				lines.add(line);
		return lines.isEmpty() ? null : gson.toJson(lines);
	}
}