package com.xmg.accountmanage.service.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xmg.accountmanage.model.AccountModel;
import com.xmg.accountmanage.service.AccountManageServiceFeign;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Hystrix 请求合并示例
 */
@Service
public class HystrixExamples01 {
    @Resource
    AccountManageServiceFeign accountManageServiceFeign;

    //声明需要服务容错的方法
    @HystrixCommand
    public List<AccountModel> getByIds(List<Integer> ids) {
        final List<AccountModel> list = accountManageServiceFeign.getByIds(ids);
        list.forEach(it -> System.out.println(it.toString()));
        return list;
    }

    //合并请求
    @HystrixCollapser(batchMethod = "getByIds",
            scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL, //合并请求的方法
            collapserProperties = {
                    //间隔多久的请求会进行合并，默认10ms
                    @HystrixProperty(name = "timerDelayInMilliseconds", value = "100"),
                    //批处理之前，批处理中允许的最大请求数
                    @HystrixProperty(name = "maxRequestsInBatch", value = "200")
            }
    )
    //处理请求合并的方法一定要支持异步，返回值必须是Future<T>
    public Future<AccountModel> getByIdFuture(Integer id) {
        return null;
    }
}
