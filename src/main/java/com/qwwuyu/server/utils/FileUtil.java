package com.qwwuyu.server.utils;

import java.io.File;
import java.io.IOException;

public class FileUtil {
	private static String p = File.separator;
	private static File dir = new File((SystemUtils.IS_OS_WINDOWS ? "D:" : "") + p + "qwwuyu" + p + "develop" + p + "txt" + p);
	static {
		if (!dir.exists())
			dir.mkdirs();
	}
	private static long id = 0;

	private static synchronized String nextID() {
		return Long.toString(id++);
	}

	public static File nextFile() throws IOException {
		File file = new File(dir, "Code" + nextID() + ".java");
		if (!file.exists())
			file.createNewFile();
		return file;
	}
}
