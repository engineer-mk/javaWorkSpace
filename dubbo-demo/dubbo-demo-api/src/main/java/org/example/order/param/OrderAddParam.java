package org.example.order.param;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author makui
 * @created 2023/3/14
 **/
@Data
public class OrderAddParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 8139948952152481150L;
    private Long userId;

    private Long productId;

    private Long count;
}
