package com.qwwuyu.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qwwuyu.server.utils.CommUtil;

@RunWith(SpringJUnit4ClassRunner.class)
public class Util {
	public static void main(String[] args) throws Exception {
		formatHtmlTemp();
	}

	// 压缩html代码为一行
	@Test
	public static void formatHtmlTemp() throws Exception {
		String read = CommUtil.read("1.txt");
		System.out.println(read.replaceAll("\t|\r|\n", "").replaceAll("\\s+", " "));
	}
}
