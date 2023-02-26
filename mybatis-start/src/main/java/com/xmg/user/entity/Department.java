package com.xmg.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author makui
 * @created on  2022/11/17
 **/
@Getter
@Setter
@TableName("department")
public class Department {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Department department = (Department) o;
        return id != null && Objects.equals(id, department.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
