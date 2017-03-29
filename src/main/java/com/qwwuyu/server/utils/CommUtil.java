package com.qwwuyu.server.utils;

public class CommUtil {
	public static boolean isWindows() {
		return SystemUtils.IS_OS_WINDOWS;
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
}
