package com.example.springdatastart.account.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 用户
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {
        @Index(name = "uk_username_id", columnList = "username,id", unique = true)
})
@Entity
@NamedEntityGraph(name = "user.rm",
        attributeNodes = {@NamedAttributeNode("resourceList"),
                @NamedAttributeNode("menuList")}
)
public class User implements Serializable {

    private static final long serialVersionUID = 1442364884625740864L;

    /**
     * id主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 用户名
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * 手机
     */
    private String phone;


    /**
     * 创建时间
     */
    @Temporal(value = TemporalType.DATE)
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 父级
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private User pUser;



    /**
     * 角色拥有的菜单
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Set<Menu> menuList;

    /**
     * 角色的资源
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Set<Resource> resourceList;

    @PrePersist
    public void setCreateTime() {
        this.createTime = new Date();
    }

    @PreUpdate
    public void setUpdateTime() {
        this.updateTime = new Date();
    }
}
