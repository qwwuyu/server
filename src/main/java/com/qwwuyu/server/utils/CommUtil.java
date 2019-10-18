package com.qwwuyu.server.utils;

import java.io.*;
import java.nio.file.Files;

public class CommUtil {
    public static boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    public static boolean isExist(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
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
}
