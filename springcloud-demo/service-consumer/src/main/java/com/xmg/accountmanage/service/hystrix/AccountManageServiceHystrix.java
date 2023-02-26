package com.xmg.accountmanage.service.hystrix;

import com.xmg.accountmanage.model.AccountModel;
import com.xmg.accountmanage.service.AccountManageServiceFeign;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 通配服务降级（需配置feign.hystrix.enabled=true）
 */
@Component
public class AccountManageServiceHystrix  implements AccountManageServiceFeign {

    @Override
    public List<AccountModel> getAccountList() {
        final ArrayList<AccountModel> list = new ArrayList<>();
        list.add(new AccountModel(1, "缺省返回", "缺省返回"));
        return list;
    }

    @Override
    public AccountModel getById(Integer id) {
        return null;
    }

    @Override
    public List<AccountModel> getByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public String getUid() {
        return null;
    }
}
