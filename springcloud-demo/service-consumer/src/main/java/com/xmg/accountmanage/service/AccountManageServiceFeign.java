package com.xmg.accountmanage.service;

import com.xmg.accountmanage.model.AccountModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//调用的远程服务名称
//@FeignClient(value = "service-provider",fallback = AccountManageServiceHystrix.class)
@FeignClient(value = "service-provider")
public interface AccountManageServiceFeign {

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    List<AccountModel> getAccountList();

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    AccountModel getById(@PathVariable Integer id);

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    List<AccountModel> getByIds(@RequestParam List<Integer> ids);

    @RequestMapping(value = "/uid", method = RequestMethod.GET)
    public String getUid();
}
