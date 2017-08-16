package com.qwwuyu.server.configs;

import java.io.File;

import com.qwwuyu.server.utils.SystemUtils;

public class SecretConfig {
	public static String privateKey = null;
	public static String publicKey = null;

	private final static String p = File.separator;
	private static String baseDir = (SystemUtils.IS_OS_WINDOWS ? "D:" : "") + p;
	public static File fileDir = new File(baseDir + p + "file" + p);
	public static File javaDir = new File(baseDir + p + "java" + p);

	static {
		if (!javaDir.exists())
			javaDir.mkdirs();
		if (!fileDir.exists())
			fileDir.mkdirs();
	}
}
