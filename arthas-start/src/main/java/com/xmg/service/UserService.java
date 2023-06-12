package com.xmg.service;

import com.xmg.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author makui
 * @created 2023/4/25
 **/
@Service
public class UserService {

    public List<User> getUsers() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return User.users;
    }
}
