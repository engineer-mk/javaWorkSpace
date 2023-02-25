package xmg.xmg.reflection.support;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends AbstractUser<Long> implements UserInfo<String> {
    private Long id;
    @Info("username info")
    private String username;
    private String phone;
    public Integer c;
    Integer d;

    private User(String username) {
        this.username = username;
    }

    public User(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String info() {
        return "user";
    }

    @Info("hello")
    private String hello(String a, Integer b, boolean c) throws ClassNotFoundException {
        return "hello";
    }

    private static void  ahh(){
        System.out.println("我是一个私有静态方法");
    }
}
