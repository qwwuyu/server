package com.qwwuyu.server.utils;

import com.qwwuyu.server.configs.SecretConfig;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static File getFile(String path) {
        return getFile(SecretConfig.baseDir, path);
    }

    public static File getFile(File parent, String path) {
        try {
            File file = new File(parent, path);
            boolean isChild = isChild(parent, file);
            if (isChild) {
                return file.getCanonicalFile();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /* ======================== java file ======================== */
    private static long id = 0;

    private static synchronized String nextID() {
        return Long.toString(id++);
    }

    public static File nextJavaFile() throws IOException {
        File file = new File(SecretConfig.javaDir, "Code" + nextID() + ".java");
        if (!file.exists()) file.createNewFile();
        return file;
    }

    /* ======================== other ======================== */
    public static File webInfFile(String path, String name) {
        String classesPath = FileUtil.class.getClassLoader().getResource("/").getPath();
        File file = new File(new File(new File(classesPath).getParentFile(), path), name);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    /* ======================== util ======================== */
    public static boolean isChild(File parent, File child) throws Exception {
        File parentFile = parent.getCanonicalFile();
        File childFile = child.getCanonicalFile();
        if (parentFile.getParent() == null && childFile.getParent() == null) {
            return parentFile.getPath().equals(childFile.getPath());
        } else if (parentFile.getParent() == null) {
            return childFile.getPath().startsWith(parentFile.getPath());
        } else if (parentFile.getParent().equals(childFile.getParent())) {
            return parentFile.getPath().equals(childFile.getPath());
        } else {
            return childFile.getPath().startsWith(parentFile.getPath());
        }
    }
}
