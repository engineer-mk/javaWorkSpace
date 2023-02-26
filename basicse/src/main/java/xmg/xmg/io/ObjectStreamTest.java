package xmg.xmg.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * transient 和static 修饰的成员变量不会被序列化
 */
public class ObjectStreamTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class User implements Serializable {
        public static final long serialVersionUID = 2123151523L;
        private Integer userId;
        private String username;
        private  String phone;

    }

    @DisplayName("ObjectOutputStream测试")
    @Test
    public void test1(){
        File file=new File("user.dat");
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new User(i, "username" + i, "phone" + i));
        }
        try (OutputStream outputStream=new FileOutputStream(file);
             ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
        ){
            objectOutputStream.writeObject(list);
            objectOutputStream.writeObject(new User(666, "777", "888"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DisplayName("ObjectInputStream测试")
    @Test
    public void test2(){
        File file=new File("user.dat");
        try (InputStream inputStream=new FileInputStream(file);
        ObjectInputStream objectInputStream=new ObjectInputStream(inputStream)){
            final Object o = objectInputStream.readObject();
            List<User> list = (List<User>) o;
            list.forEach(it-> System.out.println(it.getUsername()));
            final User user =(User) objectInputStream.readObject();
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
