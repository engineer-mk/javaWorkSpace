package org.example.order.controller;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.rpc.RpcContext;
import org.example.order.dubboApi.OrderRemoteApi;
import org.example.order.param.OrderAddParam;
import org.example.order.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * @author makui
 * @created 2023/3/15
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderRemoteApi orderRemoteApi;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createOrder(@RequestBody OrderAddParam param) {
        return orderRemoteApi.createOrder(param);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Collection<OrderVo> orderList() {
        return orderRemoteApi.orderList();
    }


    @RequestMapping(value = "/createOrderAsync", method = RequestMethod.POST)
    public String createOrderAsync(@RequestBody OrderAddParam param) {
        RpcContext.getClientAttachment().setAttachment("test", "test");
        final CompletableFuture<String> orderAsync = orderRemoteApi.createOrderAsync(param);
        return orderAsync.join();
    }

    @RequestMapping(value = "/createAndGetOrderListAsync", method = RequestMethod.POST)
    public Collection<OrderVo> createAndGetOrderListAsync(@RequestBody OrderAddParam param) {
        final long start = System.currentTimeMillis();
//        final CompletableFuture<Collection<OrderVo>> future = orderRemoteApi.createOrderAsync(param)
//                .thenCombineAsync(orderRemoteApi.orderListAsync(), (s, orderVos) -> orderVos);
        final CompletableFuture<Collection<OrderVo>> future = CompletableFuture.supplyAsync(() -> orderRemoteApi.createOrder(param))
                .thenCombineAsync(CompletableFuture.supplyAsync(orderRemoteApi::orderList), (s, orderVos) -> orderVos);
        final Collection<OrderVo> result = future.join();
        final long end = System.currentTimeMillis();
        log.info("cost:{}", end - start);
        return result;
    }
}
