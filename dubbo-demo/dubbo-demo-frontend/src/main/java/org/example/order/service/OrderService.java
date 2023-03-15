package org.example.order.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.order.OrderApi;
import org.example.order.param.OrderAddParam;
import org.example.order.vo.OrderVo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author makui
 * @created 2023/3/15
 **/
@Service
public class OrderService {
    @DubboReference
    private OrderApi orderApi;

    public String createOrder(OrderAddParam param) {
        return orderApi.createOrder(param);
    }

    public Collection<OrderVo> orderList() {
        return orderApi.orderList();
    }
}
