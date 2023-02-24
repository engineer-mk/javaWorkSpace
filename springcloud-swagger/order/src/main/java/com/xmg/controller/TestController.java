package com.xmg.controller;

import com.xmg.entity.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author makui
 * @created 2023/2/24
 **/
@RestController
@RequestMapping("/order")
public class TestController {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Order> orders() {
        List<Order> list = new ArrayList<>();
        final Order order = new Order();
        order.setId(1L);
        order.setOrderNo("310101198909013024");
        order.setCreateTime(LocalDateTime.now());
        list.add(order);
        return list;
    }
}
