package com.xmg.user.entity.relation;

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
public class AccountRoleRelation {
    private Integer accountId;
    private Integer roleId;
}
