package com.qwwuyu.server.utils;

import java.io.*;
import java.nio.file.Files;

public class CommUtil {
    public static boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    public static boolean isExist(String str) {
        return str != null && str.length() != 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static String read(String name) throws Exception {
        String path = CommUtil.class.getClassLoader().getResource(name).getPath();
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        StringBuilder builder = new StringBuilder();
        String read;
        while ((read = reader.readLine()) != null) {
            builder.append(read).append("\n");
        }
        reader.close();
        return builder.substring(0, builder.length() - 1);
    }

    public static String getStackTraceString(Throwable tr) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    public static byte[] file2bs(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    /** close */
    public static void closeStream(Object... streams) {
        for (Object stream : streams) {
            try {
                if (stream instanceof Closeable) ((Closeable) stream).close();
            } catch (IOException ignored) {
            }
        }
    }

    public static String getFileSize(long size) {
        if (size < 1024L) {
            return size + "B";
        } else if (size < 1024L * 1024) {
            return String.format("%.1fKB", size / 1024f);
        } else if (size < 1024L * 1024 * 1024) {
            return String.format("%.1fMB", size / 1024 / 1024f);
        } else {
            return String.format("%.1fGB", size / 1024 / 1024 / 1024f);
        }
    }
}
