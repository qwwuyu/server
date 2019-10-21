package com.qwwuyu.server.configs;

import com.qwwuyu.server.utils.SystemUtils;

import java.io.File;

public class SecretConfig {
    public static String privateKey = "input-input";
    public static String publicKey = "input-input";

    private static final String p = File.separator;
    public static File baseDir = new File(SystemUtils.IS_OS_WINDOWS ? "input-input" : "input-input");
    public static File javaDir = new File(baseDir, "input-input");

    public static final int coolqPort = 0;

    static {
        if (!baseDir.exists()) baseDir.mkdirs();
        if (!javaDir.exists()) javaDir.mkdirs();
    }
}
