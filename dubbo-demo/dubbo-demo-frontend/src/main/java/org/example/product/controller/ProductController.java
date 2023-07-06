package org.example.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.product.dubboApi.ProductRemoteApi;
import org.example.product.vo.ProductVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author makui
 * @created on  2023/3/15
 **/
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRemoteApi productRemoteApi;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Collection<ProductVo> productList() {
        return productRemoteApi.getProductList();
    }
}
