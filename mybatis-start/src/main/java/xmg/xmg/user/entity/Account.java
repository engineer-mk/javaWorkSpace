package xmg.xmg.user.entity;

import lombok.Data;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * @author makui
 * @created on  2022/9/17
 **/
@Data
public class Account {
    private Long id;

    private Long departmentId;
    private String username;

    private String phone;

    private Date createTime;

    private Date updateTime;


    private Department department;

    Set<Role> roles;

    Set<Resource> resources;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
