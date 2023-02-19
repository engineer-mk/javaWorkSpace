package org.example.beans;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author makui
 * @created on  2022/9/27
 **/
public class UserA {

    @Autowired
    private UserB userB;
    public UserA() {
        System.out.println("UserA 实例化");
    }


    public void a() {
        System.out.println("UserA:a()执行...");
    }
}
