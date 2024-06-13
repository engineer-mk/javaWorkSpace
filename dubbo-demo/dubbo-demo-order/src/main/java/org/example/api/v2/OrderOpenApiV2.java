package org.example.api.v2;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.order.OrderApi;

/**
 * @author makui
 * @created 2023/3/23
 **/
@DubboService(group = "order",version = "2.0")
@RequiredArgsConstructor
public class OrderOpenApiV2 implements OrderApi {
    @Delegate
    private final OrderApi orderApiImplV2;
}
