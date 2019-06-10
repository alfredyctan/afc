package org.afc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.afc.AFCException;

public class FileUtil {

	private static final String FILE_PROTOCOL = "file://";

	private static final String CLASSPATH_PROTOCOL = "classpath://";

	private static final Pattern TO_NATIVE_PATH = Pattern.compile("[\\\\|/]"); 

	public static InputStream resolveResourceAsStream(String path) {
		if (path.startsWith(CLASSPATH_PROTOCOL)) {
			return FileUtil.class.getResourceAsStream('/' + path.substring(CLASSPATH_PROTOCOL.length()));
		} else if (path.startsWith(FILE_PROTOCOL)) {
			try {
				return new FileInputStream(new File(path.substring(FILE_PROTOCOL.length())));
			} catch (FileNotFoundException e) {
				return null;
			}
		} else {
			InputStream is = FileUtil.class.getResourceAsStream('/' + path);
			try {
				return (is != null) ? is : new FileInputStream(path);
			} catch (FileNotFoundException e) {
				return null;
			}
		}
	}
	
	public static String resolveAbsolutePath(String path) {
		if (path.startsWith(CLASSPATH_PROTOCOL)) {
			return resolveClasspathProcotol(path);
		} else if (path.startsWith(FILE_PROTOCOL)) {
			return resolveFileProcotol(path);
		} else {
			return resolveFilePath(path);
		}
	}

	private static String resolveClasspathProcotol(String path) {
		try {
			URL url = FileUtil.class.getResource('/' + path.substring(CLASSPATH_PROTOCOL.length()));
			if (url == null) {
				return null;
			} else if (url.getProtocol().equals("jar")){
				return url.toURI().toString();
			} else {
				return new File(url.toURI()).getAbsolutePath();
			}
		} catch (URISyntaxException e) {
			return null;
		}
	}

	private static String resolveFileProcotol(String path) {
		return resolveFilePath(path.substring(FILE_PROTOCOL.length()));
	}

	private static String resolveFilePath(String path) {
		File file = new File(path);
		if (file == null || !file.canRead()) {
			return resolveClasspathProcotol(CLASSPATH_PROTOCOL + path);
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

	public static String toNativePath(String path) {
		return TO_NATIVE_PATH.matcher(path).replaceAll('\\' + File.separator);
	}

	public static String readFileAsString(String filename) {
		InputStream in = resolveResourceAsStream(filename);
		if (in == null) {
			return null;
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[1024];
			int length = 0;
			while ((length = reader.read(buffer)) > 0) {
				builder.append(buffer, 0, length);
			}
			return builder.toString();
		} catch (IOException e) {
			throw new AFCException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param src
	 * @param dest
	 * @param moveSrcDir - move the src directory instead of files inside
	 * @return
	 */
	public static boolean move(String src, String dest, boolean moveSrc) {
		return move(new File(src), new File(dest), moveSrc);
	}

	/**
	 * move the file/director to destination directory, not rename
	 * 
	 * @param srcFile
	 * @param destDir
	 * @param moveSrc - move the whole source if it is directory instead of files inside
	 * @return
	 */
	public static boolean move(File srcFile, File destFile, boolean moveSrc) {
		if (destFile.exists() && destFile.isFile()) {
			return false;
		}

		if (!destFile.exists() && !destFile.mkdirs()) {
			return false;
		}
		
		if (srcFile.isFile() || moveSrc) {
			srcFile.renameTo(new File(destFile.getPath() + File.separator + srcFile.getName()));
		} else {
			for (File sub : srcFile.listFiles()) {
				sub.renameTo(new File(destFile.getPath() + File.separator + sub.getName()));
			}
		}
		return true;
	}
}