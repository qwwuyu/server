package com.qwwuyu.lib.utils;

import java.io.*;

public class JavaRunUtil {
    private static final String baseCode
            = "public class %s {\n"
            + "public static void main(String[] args) throws Exception {\n"
            + "%s\n"
            + "}\n"
            + "}";

    public static String run(String code, File file) {
        try {
            String handCode = handCode(code);
            writeFile(handCode, file);
            String result = runFile(file);
            return "code=\n" + handCode + "\nresult=\n" + result;
        } catch (Exception e) {
            return "服务器内部错误" + e.getMessage();
        } finally {
            if (file != null) {
                delFile(file);
            }
        }
    }

    private static String handCode(String code) {
        return code;
    }

    private static void writeFile(String code, File file) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.format(baseCode, file.getName().substring(0, file.getName().lastIndexOf('.')), code));
            writer.flush();
        }
    }

    private static String runFile(File file) throws Exception {
        Object[] result = runProcess("javac " + file + " -encoding utf-8");
        if (!(boolean) result[0]) return (String) result[1];
        result = runProcess("java -cp " + file.getParent() + " " + file.getName().substring(0, file.getName().lastIndexOf('.')));
        return (String) result[1];
    }

    private static String getCharsetName() {
        return CommUtil.isWindows() ? "GBK" : "UTF-8";
    }

    private static Object[] runProcess(String command) throws Exception {
        final Process pro = Runtime.getRuntime().exec(command);
        final Boolean[] bls = new Boolean[]{true};
        ExecutorUtil.addCachedThread(() -> {
            CommUtil.sleep(3000);
            pro.destroy();
            bls[0] = false;
        });
        String input = streamToString(pro.getInputStream(), getCharsetName());
        String error = streamToString(pro.getErrorStream(), getCharsetName());
        int waitFor = pro.waitFor();
        return new Object[]{waitFor == 0, input + error + (bls[0] ? "" : SystemUtils.LINE_SEPARATOR + "运行超3s")};
    }

    private static String streamToString(InputStream ins, String charsetName) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] bs = new byte[1024];
        int read;
        while ((read = ins.read(bs)) != -1) {
            stream.write(bs, 0, read);
            if (stream.size() > 1024 * 100) {
                return new String(stream.toByteArray(), charsetName) + SystemUtils.LINE_SEPARATOR + "数据多于100kb";
            }
        }
        return new String(stream.toByteArray(), charsetName);
    }

    private static void delFile(File file) {
        File clazz = new File(file.getPath().substring(0, file.getPath().length() - 4) + "class");
        file.delete();
        clazz.delete();
    }
}
