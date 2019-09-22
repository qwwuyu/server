package com.qwwuyu.server.configs;

import com.qwwuyu.server.utils.SystemUtils;

import java.io.File;

public class SecretConfig {
    public static String privateKey = "input";
    public static String publicKey = "input";

    private static final String p = File.separator;
    private static String baseDir = (SystemUtils.IS_OS_WINDOWS ? "D:" : "") + p + "qwwuyu" + p + "develop" + p + "files" + p;
    public static File fileDir = new File(baseDir + p + "file" + p);
    public static File javaDir = new File(baseDir + p + "java" + p);

    public static final int coolqPort = 0;

    static {
        if (!javaDir.exists()) javaDir.mkdirs();
        if (!fileDir.exists()) fileDir.mkdirs();
    }
}
