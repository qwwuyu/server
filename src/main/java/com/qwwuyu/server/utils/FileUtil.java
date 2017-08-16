package com.qwwuyu.server.utils;

import java.io.File;
import java.io.IOException;

import com.qwwuyu.server.configs.SecretConfig;

public class FileUtil {
	private static long id = 0;

	private static synchronized String nextID() {
		return Long.toString(id++);
	}

	public static File nextFile() throws IOException {
		File file = new File(SecretConfig.javaDir, "Code" + nextID() + ".java");
		if (!file.exists())
			file.createNewFile();
		return file;
	}
}
