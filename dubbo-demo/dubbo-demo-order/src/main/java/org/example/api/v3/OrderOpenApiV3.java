package org.example.api.v3;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.order.OrderApi;

/**
 * @author makui
 * @created 2023/3/23
 **/
@DubboService(group = "order",version = "3.0")
@RequiredArgsConstructor
public class OrderOpenApiV3 implements OrderApi {
    @Delegate
    private final OrderApi orderApiImplV3;
}
