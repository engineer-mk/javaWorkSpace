package com.xmg.model.order.service.impl;

import com.xmg.model.order.entity.RobotOrder;
import com.xmg.model.order.service.OrderService;
import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    EntityManager entityManager;

    @Override
    @Transactional
    public void addOrder(Integer userId, String orderNumber) {
        if (inGlobalTransaction()) {
            System.out.println("全局事务生效中");
        }
        RobotOrder order = new RobotOrder(userId, orderNumber);
        entityManager.persist(order);
        if (true) {
            throw new RuntimeException("test");
        }
    }

    public static boolean inGlobalTransaction() {
        return RootContext.getXID() != null;
    }
}
