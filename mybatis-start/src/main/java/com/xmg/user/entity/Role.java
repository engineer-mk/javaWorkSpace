package com.xmg.user.entity;

import lombok.Data;

import java.util.Objects;

/**
 * @author makui
 * @created 2022/10/30
 **/
@Data
public class Role {
    private Integer id;
    private String name;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Role role = (Role) o;
        return id != null && Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
