package com.xafero.vermilingua.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.xafero.vermilingua.api.IParser;
import com.xafero.vermilingua.io.JarParser;
import com.xafero.vermilingua.io.ManifestParser;

public class Search {

	private static Map<String, IParser> parsers;

	static {
		parsers = new HashMap<>();
		parsers.put("MF", new ManifestParser());
		parsers.put("JAR", new JarParser());
	}

	public static void findJARs(String dir, final String name, String clazz, String manifest) throws IOException {
		File root = new File(dir);
		IOFileFilter filter = new IOFileFilter() {
			public boolean accept(File dir, String name) {
				throw new UnsupportedOperationException();
			}

			public boolean accept(File file) {
				boolean nameMatches = isNameMatch(file, name);
				IParser parser;
				String ext = FilenameUtils.getExtension(name).toUpperCase();
				if (nameMatches && (parser = parsers.get(ext)) != null)
					parser.parse(file);
				return nameMatches;
			}
		};
		Collection<File> files = FileUtils.listFiles(root, filter, TrueFileFilter.INSTANCE);
		String ending = String.format("%n");
		List<String> paths = makeRelative(root, files);
		IOUtils.writeLines(paths, ending, System.out);
	}

	private static boolean isNameMatch(File file, String term) {
		String name = file.getName();
		return (term == null) ? true : name.toUpperCase().contains(term.toUpperCase());
	}

	private static List<String> makeRelative(File root, Collection<File> files) {
		List<String> results = new ArrayList<>(files.size());
		String base = root.getAbsolutePath() + File.separatorChar;
		for (File file : files) {
			String path = file.getAbsolutePath();
			results.add(path.replace(base, ""));
		}
		return results;
	}
}