package org.example.service.v2;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcContextAttachment;
import org.example.config.BasicConfig;
import org.example.order.OrderApi;
import org.example.order.param.OrderAddParam;
import org.example.order.vo.OrderVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author makui
 * @created 2023/3/14
 * CompletableFuture 异步
 **/
@Service
@Slf4j
public class OrderApiImplV2 implements OrderApi {
    private static final List<OrderVo> ORDER_LIST = new ArrayList<>();

    @Override
    public String createOrder(OrderAddParam param) {
        throw new UnsupportedOperationException("not support");
    }

    @Override
    public Collection<OrderVo> orderList() {
        throw new UnsupportedOperationException("not support");
    }


    @Override
    public CompletableFuture<String> createOrderAsync(OrderAddParam param) {
        final RpcContextAttachment serverContext = RpcContext.getServerContext();
        log.info("client attachment test:{}", RpcContext.getServerAttachment().getAttachment("test"));
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            final OrderVo orderVo = createOrderVo(param);
            ORDER_LIST.add(orderVo);
            final Thread currentThread = Thread.currentThread();
            log.info("{}", currentThread.getName());
            return orderVo.getOrderNo();
        }, BasicConfig.executor);
    }

    @Override
    public CompletableFuture<Collection<OrderVo>> orderListAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("{}", Thread.currentThread().getName());
            return ORDER_LIST;
        }, BasicConfig.executor);
    }

    private OrderVo createOrderVo(OrderAddParam param) {
        final OrderVo orderVo = new OrderVo();
        orderVo.setId(new Random().nextLong());
        orderVo.setUserId(param.getUserId());
        orderVo.setProductId(param.getProductId());
        orderVo.setOrderNo(param.getUserId() + "-" + param.getProductId() + "-" + RpcContext.getServerContext().getLocalAddressString());
        orderVo.setCreateTime(LocalDateTime.now());
        return orderVo;
    }
}
