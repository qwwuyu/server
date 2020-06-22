package com.qwwuyu.test;

import com.qwwuyu.server.utils.CommUtil;
import org.junit.Test;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class Util {
    public static void main(String[] args) throws Exception {
        formatHtmlTemp();
    }

    // 压缩html代码为一行
    @Test
    public static void formatHtmlTemp() throws Exception {
        String read = CommUtil.read("1.txt");
        final String readNoSpace = read.replaceAll("[\t\r\n]", "").replaceAll("\\s+", " ");
        System.out.println(readNoSpace);
        setClipboardString(readNoSpace);
    }

    public static void setClipboardString(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = new StringSelection(text);
        clipboard.setContents(trans, null);
    }
}
