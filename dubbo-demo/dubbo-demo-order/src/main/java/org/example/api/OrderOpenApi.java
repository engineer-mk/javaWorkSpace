package org.example.api;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.order.OrderApi;

/**
 * @author makui
 * @created 2023/3/23
 **/
@DubboService
@RequiredArgsConstructor
public class OrderOpenApi implements OrderApi {
    @Delegate
    private final OrderApi orderApiImpl;
}
