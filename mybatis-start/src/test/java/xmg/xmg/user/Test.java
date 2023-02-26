package xmg.xmg.user;

import xmg.xmg.user.entity.Account;
import xmg.xmg.user.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @author makui
 * @created on  2022/9/17
 **/
@SpringBootTest
public class Test {
    @Autowired
    private AccountMapper accountMapper;

    @org.junit.jupiter.api.Test
    public void test2() {
        final String username = accountMapper.findUsernameById(1L);
        System.out.println(username);
    }
    @org.junit.jupiter.api.Test
    public void test3(){
        final Account account = new Account();
        account.setUsername("laoWang");
        account.setPhone("10000");
        account.setCreateTime(new Date());
        account.setUpdateTime(new Date());
        accountMapper.insertAccount(account);
        System.out.println(account);
    }

    @org.junit.jupiter.api.Test
    public void test4(){
        final int i = accountMapper.deleteById(6L);
        System.out.println(i);
    }
}
