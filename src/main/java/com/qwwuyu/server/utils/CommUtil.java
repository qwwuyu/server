package com.qwwuyu.server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CommUtil {
	public static boolean isWindows() {
		return SystemUtils.IS_OS_WINDOWS;
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}

	public static String read(String name) throws Exception {
		String path = CommUtil.class.getClassLoader().getResource(name).getPath();
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		StringBuilder builder = new StringBuilder();
		String read = null;
		while ((read = reader.readLine()) != null) {
			builder.append(read).append("\n");
		}
		reader.close();
		return builder.substring(0, builder.length() - 1).toString();
	}
}
