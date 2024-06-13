package org.example.service;

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
import java.util.stream.LongStream;

/**
 * @author makui
 * @created 2023/3/14
 **/
@Service
@Slf4j
public class OrderApiImpl implements OrderApi {

    @Override
    public String createOrder(OrderAddParam param) {
        log.info("param:{},time:{}", param.toString(), System.currentTimeMillis());
        final RpcContextAttachment serverContext = RpcContext.getServerContext();
        //q: 为什么这里可以获取到远程调用的地址？
        //a: 因为在dubbo的filter中，会将远程调用的地址放到RpcContext中，这里就可以获取到了
        final String localAddressString = serverContext.getLocalAddressString();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return param.getUserId() + "-" + param.getProductId() + "-" + localAddressString + "-" + System.currentTimeMillis();
    }

    @Override
    public Collection<OrderVo> orderList() {
        List<OrderVo> result = new ArrayList<>();
        LongStream.range(0, 5).forEach(i -> {
            final OrderVo orderVo = new OrderVo();
            orderVo.setId(i);
            orderVo.setUserId(i);
            orderVo.setProductId(i);
            orderVo.setOrderNo(i + "-" + i);
            orderVo.setCreateTime(LocalDateTime.now());
            result.add(orderVo);
        });
        //产生一个随机数
        final Random random = new Random();
        final int i = random.nextInt(10);
        if (i % 2 == 0) {
            throw new ArithmeticException("随机异常");
        }
        return result;
    }

    @Override
    public CompletableFuture<String> createOrderAsync(OrderAddParam orderAddParam) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Thread currentThread = Thread.currentThread();
            return currentThread.getName() + ":" + orderAddParam.getUserId() + "-" + orderAddParam.getProductId() + "-" + System.currentTimeMillis();
        }, BasicConfig.executor);
    }
}
