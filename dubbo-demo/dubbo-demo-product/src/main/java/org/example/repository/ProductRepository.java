package org.example.repository;

import org.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author makui
 * @created 2022/4/2
 **/
public interface ProductRepository extends JpaRepository<Product,Long> {
}
