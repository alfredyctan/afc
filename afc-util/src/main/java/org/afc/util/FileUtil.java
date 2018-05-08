package org.afc.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class FileUtil {

	public static String resolveAbsolutePath(String path) {
		if (path.startsWith("classpath://")) {
			return resolveClasspathProcotol(path);
		} else if (path.startsWith("file://")) {
			return resolveFileProcotol(path);
		} else {
			return resolveFilePath(path);
		}
	}

	private static String resolveClasspathProcotol(String path) {
		try {
			URL url = FileUtil.class.getResource('/' + path.substring("classpath://".length()));
			if (url == null) {
				return null;
			} else {
				return new File(url.toURI()).getAbsolutePath();
			}
		} catch (URISyntaxException e) {
			return null;
		}
	}

	private static String resolveFileProcotol(String path) {
		return resolveFilePath(path.substring("file://".length()));
	}

	private static String resolveFilePath(String path) {
		File file = new File(path);
		if (file == null || !file.canRead()) {
			return null;
		} else {
			return file.getAbsolutePath();
		}
	}

	public static String getRelativePath(String basePath, String filePath) {
		return getRelativePath(new File(basePath), new File(filePath));
	}

	public static String getRelativePath(File base, File file) {

		List<String> bases = new LinkedList<String>();
		bases.add(0, base.getName());
		for (File parent = base.getParentFile(); parent != null; parent = parent.getParentFile()) {
			bases.add(0, parent.getName());
		}

		List<String> files = new LinkedList<String>();
		files.add(0, file.getName());
		for (File parent = file.getParentFile(); parent != null; parent = parent.getParentFile()) {
			files.add(0, parent.getName());
		}

		int overlapIndex = 0;
		while (overlapIndex < bases.size() && overlapIndex < files.size() && bases.get(overlapIndex).equals(files.get(overlapIndex))) {
			overlapIndex++;
		}

		StringBuilder relativePath = new StringBuilder();
		for (int i = overlapIndex; i < bases.size(); i++) {
			relativePath.append("..").append(File.separatorChar);
		}

		for (int i = overlapIndex; i < files.size(); i++) {
			relativePath.append(files.get(i)).append(File.separatorChar);
		}

		relativePath.deleteCharAt(relativePath.length() - 1);
		return relativePath.toString();
	}

}