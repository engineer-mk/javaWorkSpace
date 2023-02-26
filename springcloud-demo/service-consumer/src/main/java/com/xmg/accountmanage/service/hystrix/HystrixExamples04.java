package com.xmg.accountmanage.service.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xmg.accountmanage.model.AccountModel;
import com.xmg.accountmanage.service.AccountManageServiceFeign;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务降级示例
 */
@Service
public class HystrixExamples04 {
    @Resource
    AccountManageServiceFeign accountManageServiceFeign;


    //超时或异常执行服务降级
    @HystrixCommand(fallbackMethod = "getByIdsFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
            })
    public List<AccountModel> getAccountList() {
        System.out.println(Thread.currentThread().getName());
        return accountManageServiceFeign.getAccountList();
    }

    public List<AccountModel> getByIdsFallback() {
        final ArrayList<AccountModel> list = new ArrayList<>();
        list.add(new AccountModel(100, "缺省返回", "缺省返回"));
        return list;
    }

}
