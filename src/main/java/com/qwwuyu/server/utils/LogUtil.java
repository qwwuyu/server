package com.qwwuyu.server.utils;

public class LogUtil {
    private static boolean open = true;

    public static void i(Object obj) {
        if (open && obj != null) {
            System.out.println("qw====" + obj);
        }
    }

    public static void i(String tag, Object obj) {
        if (open && obj != null) {
            System.out.println(tag + obj);
        }
    }
}
