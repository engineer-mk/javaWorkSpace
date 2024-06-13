package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcContextAttachment;
import org.apache.dubbo.rpc.RpcServiceContext;
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
        //在 Client 端和 Server 端使用，用于从 Server 端回传 Client 端，
        // Server 端写入到 ServerContext 的参数在调用结束后可以在 Client 端的 ServerContext 获取到
        final RpcContextAttachment serverContext = RpcContext.getServerContext();

        //在 Dubbo 内部使用，用于传递调用链路上的参数信息，如 invoker 对象等
        final RpcServiceContext serviceContext = RpcContext.getServiceContext();

        //在 Client 端使用，往 ClientAttachment 中写入的参数将被传递到 Server 端
        final RpcContextAttachment clientAttachment = RpcContext.getClientAttachment();

        //在 Server 端使用，从 ServerAttachment 中读取的参数是从 Client 中传递过来的
        final RpcContextAttachment serverAttachment = RpcContext.getServerAttachment();

        final String localAddressString = serverContext.getLocalAddressString();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return param.getUserId() + "-" + param.getProductId() + "-" + localAddressString;
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
        throw new UnsupportedOperationException("not support");
    }

    @Override
    public CompletableFuture<Collection<OrderVo>> orderListAsync() {
        throw new UnsupportedOperationException("not support");
    }
}
