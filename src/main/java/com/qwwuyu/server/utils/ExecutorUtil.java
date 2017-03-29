package com.qwwuyu.server.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorUtil {
	private static ExecutorService cachedService = Executors.newCachedThreadPool();

	public static void addCachedThread(Runnable task) {
		cachedService.submit(task);
	}
}
