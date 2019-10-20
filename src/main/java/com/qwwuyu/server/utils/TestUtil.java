package com.qwwuyu.server.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

public class TestUtil {
    public static void main(String[] args) throws Exception {
        formatHtmlTemp();
    }

    public static void formatHtmlTemp() throws Exception {
        String read = CommUtil.read("1.txt");
        System.out.println(read.replaceAll("[\t\r\n]", "").replaceAll("\\s+", " "));
    }

    public static void sys(HttpServletRequest request) {
        Iterator<Entry<String, String[]>> it = request.getParameterMap().entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String[]> next = it.next();
            System.out.println(next.getKey() + ":" + Arrays.toString(next.getValue()));
        }
    }
}
