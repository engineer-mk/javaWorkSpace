package org.example.order;

import org.example.order.param.OrderAddParam;
import org.example.order.vo.OrderVo;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * @author makui
 * @created 2023/3/14
 **/
public interface OrderApi {

    String createOrder(OrderAddParam orderAddParam);

    Collection<OrderVo> orderList();

    CompletableFuture<String> createOrderAsync(OrderAddParam orderAddParam);

    CompletableFuture<Collection<OrderVo>> orderListAsync();
}
