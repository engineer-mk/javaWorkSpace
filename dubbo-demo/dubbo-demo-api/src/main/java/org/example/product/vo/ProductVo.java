package org.example.product.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author makui
 * @created 2023/3/15
 **/
@Data
@NoArgsConstructor
public class ProductVo implements Serializable {

    public static final Set<ProductVo> products = new HashSet<>();
    @Serial
    private static final long serialVersionUID = 2349070332799289653L;

    static {
        products.add(new ProductVo(1L, "mac book pro 1"));
        products.add(new ProductVo(2L, "mac book pro 2"));
        products.add(new ProductVo(3L, "mac book pro 3"));
        products.add(new ProductVo(4L, "mac book pro 4"));
        products.add(new ProductVo(5L, "mac book pro 5"));
    }

    private Long productId;

    private String productName;

    public ProductVo(Long productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }
}
