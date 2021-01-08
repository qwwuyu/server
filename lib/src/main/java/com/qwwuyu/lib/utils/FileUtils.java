package com.qwwuyu.lib.utils;

import java.io.File;

public class FileUtils {
    /* ======================== other ======================== */
    public static File webInfFile(String path, String name) {
        String classesPath = FileUtils.class.getClassLoader().getResource("/").getPath();
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

    public static boolean isFile(File file) {
        return exists(file) && file.isFile();
    }

    public static boolean isDirectory(File file) {
        return exists(file) && file.isDirectory();
    }

    public static boolean exists(File file) {
        return file != null && file.exists();
    }

    public static String getFileSize(long size) {
        if (size < 1024L) {
            return size + "B";
        } else if (size < 1024L * 1024) {
            return String.format("%.2fKB", size / 1024f);
        } else if (size < 1024L * 1024 * 1024) {
            return String.format("%.2fMB", size / 1024 / 1024f);
        } else {
            return String.format("%.2fGB", size / 1024 / 1024 / 1024f);
        }
    }
}
