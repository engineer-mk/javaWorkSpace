package xmg.xmg.model.order.repository;

import xmg.xmg.model.order.entity.RobotOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<RobotOrder, Long>, JpaSpecificationExecutor<RobotOrder> {
}
