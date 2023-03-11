package com.xmg.account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {
    private Integer id;
    private String username;
    private String phone;

    public Account(Integer id, String username, String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }
}
