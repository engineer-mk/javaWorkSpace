package xmg.example.springdatastart.account.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 菜单
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu implements Serializable {

    private static final long serialVersionUID = 2805608821962740592L;
    /**
     * id主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 路由地址
     */
    @Column(nullable = false)
    private String path;

    /**
     * 路由名称
     */
    private String name;

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
    @ToString.Exclude
    private Menu pMenu;


    @PrePersist
    public void setCreateTime() {
        this.createTime = new Date();
    }

    @PreUpdate
    public void setUpdateTime() {
        this.updateTime = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        final Menu menu = (Menu) o;
        return id != null && Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
