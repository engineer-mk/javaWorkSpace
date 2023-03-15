package org.example.order.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author makui
 * @created 2023/3/14
 **/
@Data
public class OrderVo {
    private Long id;

    private Long userId;

    private Long productId;

    private String orderNo;

    private LocalDateTime createTime;
}
