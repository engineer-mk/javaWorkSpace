package org.example.order.service;

import lombok.experimental.Delegate;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.order.OrderApi;
import org.springframework.stereotype.Service;

/**
 * @author makui
 * @created 2023/3/15
 **/
@Service
public class OrderRemoteApi implements OrderApi {
    @DubboReference
    @Delegate
    private OrderApi orderApi;

}
