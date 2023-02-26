package com.xmg.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author makui
 * @created 2023/2/24
 **/
@Data
public class Order {
    private Long id;
    private String orderNo;
    private LocalDateTime createTime;
}
