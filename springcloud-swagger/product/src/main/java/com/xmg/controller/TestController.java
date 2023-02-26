package com.xmg.controller;

import com.xmg.entity.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author makui
 * @created 2023/2/24
 **/
@RestController
@RequestMapping("/product")
public class TestController {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Product> products() {
        List<Product> products = new ArrayList<>();
        final Product product = new Product();
        product.setId(1);
        product.setName("mac book pro");
        product.setPrice(new BigDecimal("12999"));
        products.add(product);
        return products;
    }
}
