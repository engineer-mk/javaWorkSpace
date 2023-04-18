package org.example.api;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.product.ProductApi;

/**
 * @author makui
 * @created 2023/4/18
 **/
@DubboService
@RequiredArgsConstructor
public class ProductOpenApi implements ProductApi {
    @Delegate
    private final ProductApi productApiImpl;
}
