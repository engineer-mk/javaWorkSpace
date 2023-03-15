package org.example.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.order.param.OrderAddParam;
import org.example.order.service.OrderService;
import org.example.order.vo.OrderVo;
import org.example.product.service.ProductService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author makui
 * @created 2023/3/15
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createOrder(@RequestBody OrderAddParam param) {
        final Long productId = param.getProductId();

        return orderService.createOrder(param);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<OrderVo> orderList() {
        return orderService.orderList();
    }
}
