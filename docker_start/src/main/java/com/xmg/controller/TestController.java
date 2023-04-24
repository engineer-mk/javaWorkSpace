package com.xmg.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author makui
 * @created on  2023/1/12
 **/
@RestController
public class TestController {

    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    public String test(@PathVariable String name) {
        return "hello " + name + "!";
    }

}
