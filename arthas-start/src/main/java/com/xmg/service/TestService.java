package com.xmg.service;

import com.xmg.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author makui
 * @created 2023/4/25
 **/
@Service
public class TestService {
    private final UserService userService;

    public TestService(UserService userService) {
        this.userService = userService;
    }

    public List<User> test() {
        //do something
        return userService.getUsers();
    }
}
