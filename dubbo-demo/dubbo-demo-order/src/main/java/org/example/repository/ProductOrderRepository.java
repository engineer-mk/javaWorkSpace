package org.example.repository;

import org.example.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author makui
 * @created 2022/4/2
 **/
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
}
