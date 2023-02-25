package xmg.xmg.model.order.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Data
@Entity
public class RobotOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键自增
    private Integer id;

    private Integer userId;

    private String orderNumber;

    public RobotOrder(Integer userId, String orderNumber) {
        this.userId = userId;
        this.orderNumber = orderNumber;
    }
}
