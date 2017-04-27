package com.qwwuyu.test;

import com.qwwuyu.server.utils.BCrypt;

public class BCryptTest {
	public static void main(String[] args) throws Exception {
		String password = "testpassword";
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		System.out.println(BCrypt.checkpw("testpassword", hashed));
		System.out.println(BCrypt.checkpw("not_bacon", hashed));
	}
}
