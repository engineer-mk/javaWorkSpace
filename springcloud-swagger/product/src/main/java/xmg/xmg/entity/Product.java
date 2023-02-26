package xmg.xmg.entity;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author makui
 * @created 2023/2/24
 **/
@Data
public class Product {

    private Integer id;
    private String name;
    private BigDecimal price;
}
