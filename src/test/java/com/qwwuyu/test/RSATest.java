package com.qwwuyu.test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson.util.Base64;
import com.qwwuyu.server.utils.CommUtil;
import com.qwwuyu.server.utils.RSAUtil;

public class RSATest {
	public static void main(String[] args) throws Exception {
		File privateKeyFile = new File("D:\\jt\\rsa\\privateKeyFile");
		File publicKeyFile = new File("D:\\jt\\rsa\\publicKeyFile");
		RSAUtil rsaUtil = RSAUtil.loadKeyForBase64(privateKeyFile, publicKeyFile);
		byte[] bs = Base64.decodeFast(CommUtil.read("1.txt"));
		System.out.println(new String(rsaUtil.decrypt(bs), StandardCharsets.UTF_8));
	}
}
