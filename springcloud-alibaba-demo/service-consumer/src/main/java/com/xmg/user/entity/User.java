package com.xmg.user.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键自增
    private Integer id;

    private String username;

    private String phone;

    private Integer orderCount;

    public User(String username, String phone) {
        this.username = username;
        this.phone = phone;
        this.orderCount = 0;
    }
}
