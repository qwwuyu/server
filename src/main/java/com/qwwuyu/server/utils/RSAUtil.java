package com.qwwuyu.server.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.tools.keytool.CertAndKeyGen;

/**
 * RSA工具
 */
public class RSAUtil {
	public RSAPublicKey pubkey;
	public RSAPrivateKey prikey;

	private RSAUtil(RSAPrivateKey prikey, RSAPublicKey pubkey) {
		this.prikey = prikey;
		this.pubkey = pubkey;
	}

	/** 加密 */
	public byte[] encrypt(byte[] bytes) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);
		return cipher.doFinal(bytes);
	}

	/** 解密 */
	public byte[] decrypt(byte[] bytes) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, prikey);
		return cipher.doFinal(bytes);
	}

	/** 生成新的密匙 */
	public static RSAUtil genRSAKeyPair2(File privateKeyFile, File publicKeyFile) throws Exception {
		CertAndKeyGen keyPair = new CertAndKeyGen("RSA", "SHA1WithRSA", null);
		keyPair.generate(1024);
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivateKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublicKey();
		RSAUtil rsaUtil = new RSAUtil(privateKey, publicKey);
		rsaUtil.saveKeyForBase64(privateKeyFile, publicKeyFile);
		return rsaUtil;
	}

	/** 生成新的密匙 */
	public static RSAUtil genRSAKeyPair(File privateKeyFile, File publicKeyFile) throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAUtil rsaUtil = new RSAUtil(privateKey, publicKey);
		rsaUtil.saveKeyForBase64(privateKeyFile, publicKeyFile);
		return rsaUtil;
	}

	/** 读取密匙 */
	public static RSAUtil loadKeyForBase64(File privateKeyFile, File publicKeyFile) throws Exception {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] privateKey = decoder.decodeBuffer(loadBase64(privateKeyFile));
		byte[] publicKey = decoder.decodeBuffer(loadBase64(publicKeyFile));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		PKCS8EncodedKeySpec keySpec2 = new PKCS8EncodedKeySpec(privateKey);
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec2);
		return new RSAUtil(rsaPrivateKey, rsaPublicKey);
	}

	private static String loadBase64(File file) throws Exception {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.length() > 0 && readLine.charAt(0) != '-') {
					sb.append(readLine);
				}
			}
			return sb.toString();
		}
	}

	/** 保存密匙 */
	public void saveKeyForBase64(File privateKeyFile, File publicKeyFile) throws Exception {
		if (prikey == null || pubkey == null)
			return;
		BASE64Encoder encoder = new BASE64Encoder();
		saveBase64(encoder.encodeBuffer(prikey.getEncoded()), privateKeyFile);
		saveBase64(encoder.encodeBuffer(pubkey.getEncoded()), publicKeyFile);
	}

	private static void saveBase64(String buffer, File file) throws Exception {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(buffer);
		}
	}

	public static void main(String[] args) throws Exception {
		File privateKeyFile = new File("D:\\jt\\rsa\\privateKeyFile");
		File publicKeyFile = new File("D:\\jt\\rsa\\publicKeyFile");
		// RSAUtil rsaUtil = RSAUtil.genRSAKeyPair(privateKeyFile, publicKeyFile);
		RSAUtil rsaUtil = RSAUtil.loadKeyForBase64(privateKeyFile, publicKeyFile);

		String clearText = "阿萨德";
		byte[] encryptedBytes = rsaUtil.encrypt(clearText.getBytes(StandardCharsets.UTF_8));
		String decryptedText = new String(rsaUtil.decrypt(encryptedBytes), StandardCharsets.UTF_8);
		System.out.println(decryptedText.equals(clearText));
	}
}