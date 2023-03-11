package com.xmg.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author makui
 * @created 2023/3/9
 **/
@RestController
public class TestController {


    public static void main(String[] args) {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 5; i++) {
            final String encode = encoder.encode("123123");
            final boolean matches = encoder.matches("123123", encode);
            System.out.println(matches);
            System.out.println(encode);
        }
    }
}
