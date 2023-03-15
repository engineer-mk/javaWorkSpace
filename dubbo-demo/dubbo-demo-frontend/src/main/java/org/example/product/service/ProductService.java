package org.example.product.service;

import lombok.experimental.Delegate;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.product.ProductApi;
import org.example.product.vo.ProductVo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * @author makui
 * @created 2023/3/15
 **/
@Service
public class ProductService implements ProductApi {
    @DubboReference
    @Delegate
    private ProductApi productApi;

}
