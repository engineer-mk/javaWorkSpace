package org.example.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.order.param.OrderAddParam;
import org.example.order.service.OrderService;
import org.example.order.vo.OrderVo;
import org.example.product.service.ProductService;
import org.example.product.vo.ProductVo;
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
    private final OrderService orderService;
    private final ProductService productService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createOrder(@RequestBody OrderAddParam param) {
        final Long productId = param.getProductId();
        final ProductVo product = productService.getProduct(productId);
        if (product == null) {
            return "product not found";
        }
        return orderService.createOrder(param);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Collection<OrderVo> orderList() {
        return orderService.orderList();
    }
}
