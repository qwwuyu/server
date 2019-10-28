package com.qwwuyu.server.utils;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TLRobot {
    public static String requestTL(String key, String userId, String txt) {
        try {
            String post = JSON.toJSONString(new TLRequest(key, userId, txt));
            URL url = new URL("http://openapi.tuling123.com/openapi/api/v2");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(post.getBytes());
            outputStream.flush();
            outputStream.close();
            if (conn.getResponseCode() == 200) {
                InputStream inStream = conn.getInputStream();
                String result = new String(toByteArray(inStream), "UTF-8");
                TLResponse tlResponse = JSON.parseObject(result, TLResponse.class);
                return tlResponse.getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class TLRequest {
        private Perception perception;
        private UserInfo userInfo;

        public TLRequest() {
        }

        public TLRequest(String apiKey, String userId, String text) {
            perception = new Perception();
            perception.inputText = new InputText();
            perception.inputText.text = text;
            userInfo = new UserInfo();
            userInfo.apiKey = apiKey;
            userInfo.userId = userId;
        }

        private class Perception {
            private InputText inputText;

            public InputText getInputText() {
                return inputText;
            }
        }

        private class InputText {
            public String text;

            public String getText() {
                return text;
            }
        }

        private class UserInfo {
            public String apiKey;
            public String userId;

            public String getApiKey() {
                return apiKey;
            }

            public String getUserId() {
                return userId;
            }
        }

        public Perception getPerception() {
            return perception;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }
    }

    private static class TLResponse {
        public Results[] results;

        private static class Results {
            public Values values;

            public void setValues(Values values) {
                this.values = values;
            }
        }

        private static class Values {
            public String text;

            public void setText(String text) {
                this.text = text;
            }
        }

        public void setResults(Results[] results) {
            this.results = results;
        }

        public String getResult() {
            if (results != null && results.length > 0 && results[0].values != null) {
                return results[0].values.text;
            }
            return null;
        }
    }

    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
