package com.xmg.accountmanage.controller;

import com.xmg.accountmanage.model.AccountModel;
import com.xmg.accountmanage.service.AccountManageService;
import com.xmg.accountmanage.service.AccountManageServiceFeign;
import com.xmg.accountmanage.service.hystrix.*;
import com.xmg.accountmanage.service.impl.AccountMangeServiceLoadBalancerAnnotationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class AccountManageController {
    @Qualifier(value = "accountManageServiceLoadBalancerImpl")
    @Autowired
    AccountManageService accountManageService;
    @Resource
    AccountManageServiceFeign accountManageServiceFeign;
    @Autowired
    HystrixExamples01 hystrixExamples01;
    @Autowired
    HystrixExamples02 hystrixExamples02;
    @Autowired
    HystrixExamples03 hystrixExamples03;
    @Autowired
    HystrixExamples04 hystrixExamples04;
    @Autowired
    HystrixExamples05 hystrixExamples05;
    @Autowired
    HystrixExamples06 hystrixExamples06;

    @RequestMapping(value = "/account/list", method = RequestMethod.GET)
    public List<AccountModel> accountList() {
        return hystrixExamples05.getAccountList();
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public List<AccountModel> accounts() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            ids.add(i);
        }
        return hystrixExamples01.getByIds(ids);
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    public AccountModel accountModel(@PathVariable Integer id) throws ExecutionException, InterruptedException {
        return hystrixExamples01.getByIdFuture(id).get();
    }

    @RequestMapping(value = "/account/uid/{id}",method = RequestMethod.GET)
    public String getUid(@PathVariable Integer id){
        return hystrixExamples06.getOneString(id);
    }

}
