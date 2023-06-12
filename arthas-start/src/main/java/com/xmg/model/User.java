package com.xmg.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author makui
 * @created 2023/4/25
 **/
public class User {
    /**
     * 获取users
     * ognl -x 3 '@com.xmg.model.User@users'
     */
    public static final List<User> users = new ArrayList<>();
    private String name;
    private String phone;

    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
