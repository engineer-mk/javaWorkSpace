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
 * 线程池隔离示例
 */
@Service
public class HystrixExamples02 {
    @Resource
    AccountManageServiceFeign accountManageServiceFeign;


    //超时或异常或超出允许等待数量执行服务降级(有坑)
    @HystrixCommand(groupKey = "HystrixExamples02-getAccountList", //服务名称，相同名称使用同一个线程池
            commandKey = "getAccountList", //接口名称，默认为方法名
            threadPoolKey = "HystrixExamples02-getAccountList", //线程池名称，相同名称使用同一个线程池
            commandProperties = {
                    //超时时间，默认1000ms，超时执行拒绝策略
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "6"), //线程池大小
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "1"),//线程存活时间，默认1min
                    @HystrixProperty(name = "maxQueueSize", value = "5"),//队列等待阈值（最大队列长度，默认-1）
                    //#即使maxQueueSize没有达到，达到queueSizeRejectionThreshold该值后，请求也会被拒绝，默认值5
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "3")
            }, fallbackMethod = "getByIdsFallback")
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
