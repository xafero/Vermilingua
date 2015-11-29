package com.xafero.vermilingua.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public final class MyUtils {

	public static InputStream toStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> makeRelative(File root, Collection<File> files) {
		List<String> results = new ArrayList<>(files.size());
		String base = root.getAbsolutePath() + File.separatorChar;
		for (File file : files) {
			String path = file.getAbsolutePath();
			results.add(path.replace(base, ""));
		}
		return results;
	}

	public static String makeRelative(File root, File child) {
		return child.getAbsolutePath().replace(root.getAbsolutePath() + File.separatorChar, "");
	}

	public static String getExt(String name) {
		return FilenameUtils.getExtension(name).toUpperCase();
	}

	public static boolean isNameMatch(File file, String term) {
		return contains(file.getName(), term);
	}

	public static boolean contains(String first, String second) {
		if (first == null || second == null)
			return false;
		return first.toUpperCase().contains(second.toUpperCase());
	}
}