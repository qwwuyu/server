package com.qwwuyu.server.utils;

import com.qwwuyu.server.configs.SecretConfig;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    private static long id = 0;

    private static synchronized String nextID() {
        return Long.toString(id++);
    }

    public static File nextFile() throws IOException {
        File file = new File(SecretConfig.javaDir, "Code" + nextID() + ".java");
        if (!file.exists()) file.createNewFile();
        return file;
    }

    public static File webInfFile(String path, String name) {
        String classesPath = FileUtil.class.getClassLoader().getResource("/").getPath();
        File file = new File(new File(new File(classesPath).getParentFile(), path), name);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }
}
