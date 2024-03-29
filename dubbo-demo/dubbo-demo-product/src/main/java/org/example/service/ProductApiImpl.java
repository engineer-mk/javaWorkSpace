package org.example.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.product.ProductApi;
import org.example.product.vo.ProductVo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author makui
 * @created 2023/3/15
 **/
@Service
public class ProductApiImpl implements ProductApi {
    @Override
    public ProductVo getProduct(Long id) {
        return ProductVo.products
                .stream()
                .filter(it -> it.getProductId().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public Collection<ProductVo> getProductList() {
        return ProductVo.products;
    }
}
