package com.xmg.model.order.controller;

import com.xmg.model.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/provider/order/add",method = RequestMethod.POST)
    public void addOrder(@RequestParam Integer userid, @RequestParam String orderNumber) {
        orderService.addOrder(userid, orderNumber);
    }
}
