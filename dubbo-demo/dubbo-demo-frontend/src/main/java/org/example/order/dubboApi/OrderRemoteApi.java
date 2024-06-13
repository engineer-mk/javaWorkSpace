package org.example.order.dubboApi;

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
    @DubboReference(check = false,group = "order",version = "2.0")
    @Delegate
    private OrderApi orderApi;

}
