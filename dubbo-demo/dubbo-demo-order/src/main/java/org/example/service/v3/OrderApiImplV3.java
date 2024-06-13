package org.example.service.v3;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;
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

/**
 * @author makui
 * @created 2023/3/14
 * AsyncContext 异步
 **/
@Service
@Slf4j
public class OrderApiImplV3 implements OrderApi {
    private static final List<OrderVo> ORDER_LIST = new ArrayList<>();

    @Override
    public String createOrder(OrderAddParam param) {
        final AsyncContext asyncContext = RpcContext.startAsync();
        BasicConfig.executor.execute(() -> {
            // 如果要使用上下文，则必须要放在第一句执行
            asyncContext.signalContextSwitch();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            log.info("{}", Thread.currentThread().getName());
            final OrderVo orderVo = createOrderVo(param);
            ORDER_LIST.add(orderVo);
            asyncContext.write(orderVo.getOrderNo());
        });
        return null;

    }

    @Override
    public Collection<OrderVo> orderList() {
        final AsyncContext asyncContext = RpcContext.startAsync();
        BasicConfig.executor.execute(() -> {
            asyncContext.signalContextSwitch();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            log.info("{}", Thread.currentThread().getName());
            asyncContext.write(ORDER_LIST);
        });
        return null;
    }


    @Override
    public CompletableFuture<String> createOrderAsync(OrderAddParam param) {
        throw new UnsupportedOperationException("not support");
    }

    @Override
    public CompletableFuture<Collection<OrderVo>> orderListAsync() {
        throw new UnsupportedOperationException("not support");
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
