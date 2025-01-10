package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * @author makui
 * @created 2022/4/2
 **/
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long productId;

    private Long count;

    private String orderNo;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    public void setCreateTime() {
        this.createTime = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdateTime() {
        this.updateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        final ProductOrder productOrder = (ProductOrder) o;
        return id != null && Objects.equals(id, productOrder.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
