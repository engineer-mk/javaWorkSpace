package org.example.product;

import org.example.product.vo.ProductVo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author makui
 * @created 2023/3/15
 **/
public interface ProductApi {

    ProductVo getProduct(Long id);

    Collection<ProductVo> getProductList();

    void reduceCount(Long id, Long count);
}
