package com.xmg.user.service;

import com.xmg.openfeign.ProviderApis;
import com.xmg.user.entity.User;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    EntityManager entityManager;
    @Autowired
    ProviderApis providerApis;

    @GlobalTransactional
    @Transactional
    public void useraddOrder(){
        if (inGlobalTransaction()) {
            System.out.println("全局事务生效");
        }
        User user = entityManager.find(User.class, 1);
        if (user == null) {
            TypedQuery<User> query = entityManager.createQuery("SELECT  u from User u where u.id=:userId", User.class);
            query.setParameter("userId",1);
            try {
                user = query.getSingleResult();
            } catch (Exception ignore) {

            }
        }
        if (user == null) {
            user = new User("abc01", "13333333330");
            entityManager.persist(user);
            user.setOrderCount(user.getOrderCount() + 1);
        }
        user.setOrderCount(user.getOrderCount()+1);
        providerApis.addOrder(user.getId(), UUID.randomUUID().toString());
//        if (true) {
//            throw new RuntimeException();
//        }
    }

    @Transactional
    public void orderCountAdd(User user){
        user.setOrderCount(user.getOrderCount()+1);
    }

    public static boolean inGlobalTransaction() {
        return RootContext.getXID() != null;
    }
}
