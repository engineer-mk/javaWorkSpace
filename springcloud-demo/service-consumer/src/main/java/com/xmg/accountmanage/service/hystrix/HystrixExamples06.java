package com.xmg.accountmanage.service.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xmg.accountmanage.model.AccountModel;
import com.xmg.accountmanage.service.AccountManageServiceFeign;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务熔断示例
 */
@DefaultProperties(
        defaultFallback = "defaultFallback",
        commandProperties = {
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
        }
)
@Service
public class HystrixExamples06 {
    @Resource
    AccountManageServiceFeign accountManageServiceFeign;

    //1。当满足一定的阀值的时候（默认10秒内超过20个请求次数）
    //2。当失败率达到一定的时候（默认10秒内超过50%的请求失败）
    //3。到达以上阀值，断路器将会开启
    //4。当开启的时候，所有请求都不会进行转发
    //5。一段时间之后（默认是5秒），这个时候断路器是半开状态，会让其中一个请求进行转发。如果成功，断路器会关闭，若失败，继续开启。重复4和5
    @HystrixCommand(fallbackMethod = "defaultFallback",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled",value = "true"), //开启熔断
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "20"), //请求次数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),//时间窗口期
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "50") //异常百分比
            })
    public String getOneString(Integer i) {
        if (i < 0) {
            throw new RuntimeException();
        }
        System.out.println("恢复:"+System.currentTimeMillis() / 1000);
        return accountManageServiceFeign.getUid();
    }

    public String defaultFallback(Integer i) {
        if (i >= 0) {
            System.out.println("熔断:"+System.currentTimeMillis() / 1000);
        }
        return "this is defaultFallback";
    }


}
