package org.example.order.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author makui
 * @created 2023/3/14
 **/
@Data

public class OrderVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -7634929614019122276L;

    private Long id;

    private Long userId;

    private Long productId;

    private String orderNo;

    private LocalDateTime createTime;
}
