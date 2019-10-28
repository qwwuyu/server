package com.qwwuyu.test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    public static void main(String[] args) throws Exception {
        String body = String.format("{\"type\":\"android\",\"bundle_id\":\"%s\",\"api_token\":\"%s\"}", "asd", "asd");
        post("http://api.fir.im/apps", body);

        Map<String, String> params = new HashMap<>();
        params.put("key", "d95bf455542267964ad56e5c9fadae9c1f9596ca.apk");
        params.put("x:build", "4");
        params.put("x:changelog", "啊哦额一");
        String[] names = new String[]{"file"};
        File[] files = new File[]{new File("C:\\Users\\qw\\Desktop\\123.apk")};
        upload("http://upload.qbox.me", params, names, files);
    }

    public static String post(String url, String body) throws Exception {
        URL postUrl = new URL(url);
        // 打开连接

        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.connect();

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(body.getBytes("UTF-8"));
        out.flush();
        out.close();

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);
        String result = new String(streamToBytes(connection.getInputStream()), "UTF-8");
        System.out.println(result);
        connection.disconnect();
        return result;
    }

    public static String upload(String url, Map<String, String> params, String[] names, File[] files) throws Exception {
        final String enter = "\r\n";
        final String boundary = "----WebKitFormBoundaryQJDb3vvO3mUQrAVh";

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream output = connection.getOutputStream();

        StringBuilder paramsSb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
                paramsSb.append("--").append(boundary).append(enter);
                paramsSb.append(String.format("Content-Disposition: form-data; name=\"%s\"", entry.getKey())).append(enter).append(enter);
                paramsSb.append(entry.getValue()).append(enter);
            }
        }
        output.write(paramsSb.toString().getBytes());

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            StringBuilder fileSB = new StringBuilder();
            fileSB.append("--").append(boundary).append(enter);
            fileSB.append(String.format("Content-Disposition: form-data;name=\"%s\";filename=\"%s\"", names[i], file.getName())).append(enter);
            fileSB.append("Content-Type: application/octet-stream").append(enter).append(enter);
            output.write(fileSB.toString().getBytes());
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer, 0, 1024)) != -1) {
                output.write(buffer, 0, len);
            }
            inputStream.close();
            output.write(enter.getBytes());
        }

        output.write(("--" + boundary + "--" + enter).getBytes());
        output.flush();
        output.close();

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);
        String result = new String(streamToBytes(connection.getInputStream()), "UTF-8");
        System.out.println(result);
        connection.disconnect();
        return result;
    }

    private static byte[] streamToBytes(InputStream in) throws IOException {
        byte[] buffer = new byte[5120];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int readSize;
        while ((readSize = in.read(buffer)) >= 0) out.write(buffer, 0, readSize);
        in.close();
        return out.toByteArray();
    }
}
