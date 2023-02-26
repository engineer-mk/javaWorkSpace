package com.xmg.io;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@DisplayName("字符输入输出流")
public class ReaderAndWriterTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User {
        private Integer userId;
        private String username;
        private String phone;

    }

    /**
     * 文件不存在则创建
     * 存在则覆盖
     * 指明append=true则追加
     */
    @DisplayName("字符输出流->FileWriter")
    @Test
    public void test1() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new User(i, "username" + i, "phone" + i));
        }
        File file = new File("test1.txt");
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            for (User user : list) {
                fileWriter.write(JSON.toJSONString(user));
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("字符输入流->FileReader")
    @Test
    public void test2() {
        File file = new File("test2.txt");
        try (FileReader fileWriter = new FileReader(file)) {
            char[] ch = new char[5];
            int len;
            while ((len = fileWriter.read(ch)) != -1) {
                //注意此处的len,最后一个数组末尾包含上次读取的值
                String str = new String(ch, 0, len);
                System.out.print(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("字符输入包装处理流->BufferedReader")
    @Test
    public void test3() {
        List<User> list = new ArrayList<>();
        File file = new File("test1.txt");
        try (FileReader fileWriter = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileWriter)
        ) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                list.add(JSON.parseObject(s, User.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(list.size());
    }

    @DisplayName("转换处理流->InputStreamReader(把字节输入流转换为字符输入流)")
    @Test
    public void test4() {
        File file = new File("test1.txt");
        try (InputStream inputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        ) {
            char[] ch = new char[10];
            int len;
            while ((len = inputStreamReader.read(ch)) != -1) {
                final String s = new String(ch, 0, len);
                System.out.print(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @DisplayName("转换处理流->OutputStreamWriter(字符输出流转换为字节输出流)")
    @Test
    public void test5() {
        try (OutputStream out = new FileOutputStream("test2.txt",true);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out,"gbk");
        ) {
            outputStreamWriter.write("helloWord");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
