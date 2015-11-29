package com.xafero.vermilingua.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Search {

	public static void findJARs(String dir, String term) throws IOException {
		File root = new File(dir);
		Collection<File> files = findJARs(root, term);
		String ending = String.format("%n");
		List<String> paths = makeRelative(root, files);
		IOUtils.writeLines(paths, ending, System.out);
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

	private static Collection<File> findJARs(File dir, final String term) {
		IOFileFilter filter = new IOFileFilter() {
			public boolean accept(File dir, String name) {
				throw new UnsupportedOperationException();
			}

			public boolean accept(File file) {
				String name = file.getName();
				String ext = FilenameUtils.getExtension(name).toUpperCase();
				switch (ext) {
				case "JAR":
					if (term == null)
						return true;
					return name.toUpperCase().contains(term.toUpperCase());
				case "MF":
				default:
					return false;
				}
			}
		};
		return FileUtils.listFiles(dir, filter, TrueFileFilter.INSTANCE);
	}
}