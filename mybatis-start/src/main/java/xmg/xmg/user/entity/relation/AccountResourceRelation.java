package xmg.xmg.user.entity.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author makui
 * @created 2022/10/30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResourceRelation {
    private Integer accountId;
    private Integer resourceId;
}
