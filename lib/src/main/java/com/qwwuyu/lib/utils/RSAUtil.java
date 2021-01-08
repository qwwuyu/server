package com.qwwuyu.lib.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

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

    /** 解密 */
    public String decrypt(String base64Data) throws Exception {
        return new String(decrypt(java.util.Base64.getDecoder().decode(base64Data)), StandardCharsets.UTF_8);
    }


    /** 生成新的密匙 */
    public static RSAUtil genRSAKeyPair2(File privateKeyFile, File publicKeyFile) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
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
    public static RSAUtil loadKeyForBase64(String privateKeyStr, String publicKeyStr) throws Exception {
        byte[] privateKey = Base64.decodeBase64(privateKeyStr);
        byte[] publicKey = Base64.decodeBase64(publicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        PKCS8EncodedKeySpec keySpec2 = new PKCS8EncodedKeySpec(privateKey);
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec2);
        return new RSAUtil(rsaPrivateKey, rsaPublicKey);
    }

    /** 读取密匙 */
    public static RSAUtil loadKeyForBase64(File privateKeyFile, File publicKeyFile) throws Exception {
        return loadKeyForBase64(loadBase64(privateKeyFile), loadBase64(publicKeyFile));
    }

    private static String loadBase64(File file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String readLine;
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
        if (prikey == null || pubkey == null) return;
        saveBase64(Base64.encodeBase64String(prikey.getEncoded()), privateKeyFile);
        saveBase64(Base64.encodeBase64String(pubkey.getEncoded()), publicKeyFile);
    }

    private static void saveBase64(String buffer, File file) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(buffer);
        }
    }
}