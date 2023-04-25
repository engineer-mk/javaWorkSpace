package org.example.product.service;

import lombok.experimental.Delegate;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.product.ProductApi;
import org.springframework.stereotype.Service;

/**
 * @author makui
 * @created 2023/3/15
 **/
@Service
public class ProductRemoteApi implements ProductApi {
    @DubboReference
    @Delegate
    private ProductApi productApi;

}