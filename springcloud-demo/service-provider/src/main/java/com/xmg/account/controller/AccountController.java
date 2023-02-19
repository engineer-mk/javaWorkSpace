package com.xmg.account.controller;

import com.xmg.account.entity.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class AccountController {
    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public List<Account> accountList() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account(1, "abc", "133"));
        accounts.add(new Account(2, "qwe", "134"));
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return accounts;
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Account account(@PathVariable Integer id) {
        return new Account(id, "abc", "133");
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<Account> accounts(@RequestParam List<Integer> ids) {
        List<Account> list = new ArrayList<>();
        ids.forEach(it -> list.add(new Account(it, "aaa", "bbb")));
        return list;
    }

    @RequestMapping(value = "/uid", method = RequestMethod.GET)
    public String getUid(){
        return UUID.randomUUID().toString();
    }
    @RequestMapping(value = "/point",method = RequestMethod.GET)
    public String getPoint(){
        return port;
    }
}
