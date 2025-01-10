package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Product;
import org.example.product.ProductApi;
import org.example.product.vo.ProductVo;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Optional;

/**
 * @author makui
 * @created 2023/3/15
 **/
@Service
@RequiredArgsConstructor
public class ProductApiImpl implements ProductApi {
    private final ProductRepository productRepository;

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

    @Override
    public void reduceCount(Long id, Long count) {
        final Optional<Product> optional = productRepository.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("product not found");
        }
        optional.ifPresent(product -> {
            Assert.state(product.getCount() >= count, "product count not enough");
            product.setCount(product.getCount() - count);
            productRepository.save(product);
        });
    }
}
