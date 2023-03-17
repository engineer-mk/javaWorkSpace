package org.example.api;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.order.OrderApi;
import org.example.order.param.OrderAddParam;
import org.example.order.vo.OrderVo;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author makui
 * @created 2023/3/14
 **/
@DubboService
public class OrderApiImpl implements OrderApi {
    @Value("${dubbo.protocol.port}")
    private String port;

    @Override
    public String createOrder(OrderAddParam param) {
        return param.getUserId() + "-" + param.getProductId()+"-"+port;
    }

    @Override
    public Collection<OrderVo> orderList() {
        List<OrderVo> result = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            final OrderVo orderVo = new OrderVo();
            orderVo.setId(i);
            orderVo.setUserId(i);
            orderVo.setProductId(i);
            orderVo.setOrderNo(i + "-" + i);
            orderVo.setCreateTime(LocalDateTime.now());

            result.add(orderVo);
        }
        return result;
    }
}
