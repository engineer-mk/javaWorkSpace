package org.example.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcContextAttachment;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.example.config.BasicConfig;
import org.example.dubboApi.ProductRemoteApi;
import org.example.entity.ProductOrder;
import org.example.order.OrderApi;
import org.example.order.param.OrderAddParam;
import org.example.order.vo.OrderVo;
import org.example.repository.ProductOrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@RequiredArgsConstructor
public class OrderApiImplV2 implements OrderApi {
    private final ProductOrderRepository productOrderRepository;
    private final ProductRemoteApi productRemoteApi;

    private static final List<OrderVo> ORDER_LIST = new ArrayList<>();

    @Override
    @GlobalTransactional
    public String createOrder(OrderAddParam param) {
        final OrderVo orderVo = doCreateOrder(param);
        productRemoteApi.reduceCount(param.getProductId(), param.getCount());
        if (true) {
            throw new RuntimeException("test");
        }
        return orderVo.getOrderNo();
    }

    @Override
    public Collection<OrderVo> orderList() {
        throw new UnsupportedOperationException("not support");
    }


    @Override
    public CompletableFuture<String> createOrderAsync(OrderAddParam param) {
        return CompletableFuture.supplyAsync(() -> {
            final OrderVo orderVo = doCreateOrder(param);
            productRemoteApi.reduceCount(param.getProductId(), param.getCount());
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

    private OrderVo doCreateOrder(OrderAddParam param) {
        final ProductOrder order = new ProductOrder();
        order.setUserId(param.getUserId());
        order.setProductId(param.getProductId());
        order.setCount(param.getCount());
        order.setOrderNo(createOrderNo());
        productOrderRepository.save(order);
        final OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);
        return orderVo;
    }

    private String createOrderNo() {
        return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + new Random().nextInt(1000);
    }
}
