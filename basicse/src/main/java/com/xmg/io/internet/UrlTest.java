package com.xmg.io.internet;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlTest {

    @Test
    public void test1() {
        File file = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://192.168.0.10:8080/resources/ganyu.jpeg");
            file = new File(url.getFile().substring(1));
            if (!file.exists()) {
                final File path = file.getParentFile();
                if (!path.exists()) {
                    final boolean mkdirs = path.mkdirs();
                    if (!mkdirs) {
                        throw new RuntimeException("文件路径创建失败");
                    }
                }
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert urlConnection != null;
        try (final InputStream inputStream = urlConnection.getInputStream();
             final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
             final OutputStream outputStream = new FileOutputStream(file);
             final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
