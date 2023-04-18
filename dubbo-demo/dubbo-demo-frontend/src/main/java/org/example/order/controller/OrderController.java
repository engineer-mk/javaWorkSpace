package org.example.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.order.param.OrderAddParam;
import org.example.order.service.OrderRemoteApi;
import org.example.order.vo.OrderVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author makui
 * @created 2023/3/15
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderRemoteApi orderRemoteApi;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createOrder(@RequestBody OrderAddParam param) {
        return orderRemoteApi.createOrder(param);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Collection<OrderVo> orderList() {
        return orderRemoteApi.orderList();
    }
}
