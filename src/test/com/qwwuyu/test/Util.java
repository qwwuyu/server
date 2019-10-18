package com.qwwuyu.test;

import com.qwwuyu.server.utils.CommUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class Util {
    public static void main(String[] args) throws Exception {
        System.out.println(CommUtil.class.getClassLoader().getResource("").getPath());
        // formatHtmlTemp();
        //        File file = new File(SecretConfig.fileDir, "..\\file\\1.png");
        //        System.out.println(file.toString());
        //        System.out.println(file.exists());
    }

    // 压缩html代码为一行
    @Test
    public static void formatHtmlTemp() throws Exception {
        String read = CommUtil.read("1.txt");
        System.out.println(read.replaceAll("[\t\r\n]", "").replaceAll("\\s+", " "));
    }
}
