package org.example.service;

import org.apache.dubbo.rpc.RpcContext;
import org.example.order.OrderApi;
import org.example.order.param.OrderAddParam;
import org.example.order.vo.OrderVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author makui
 * @created 2023/3/14
 **/
@Service
public class OrderApiImpl implements OrderApi {

    @Override
    public String createOrder(OrderAddParam param) {
        //q: 为什么这里可以获取到远程调用的地址？
        //a: 因为在dubbo的filter中，会将远程调用的地址放到RpcContext中，这里就可以获取到了
        final String localAddressString = RpcContext.getServerContext().getLocalAddressString();
        return param.getUserId() + "-" + param.getProductId() + "-" + localAddressString + "-" + System.currentTimeMillis();
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
