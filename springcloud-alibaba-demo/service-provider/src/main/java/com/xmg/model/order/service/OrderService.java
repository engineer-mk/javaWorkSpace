package com.xmg.model.order.service;

public interface OrderService {

    /**
     * 下单
     * @param userId
     * @param orderNumber
     */
    void addOrder(Integer userId, String orderNumber);

}
