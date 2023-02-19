package com.xmg;

import com.xmg.ribbonrule.rule.ServiceProviderIRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@EnableEurekaClient //配置了eureka注册中心默认开启可省略
@EnableFeignClients //开启feign支持
@EnableHystrix  //开启服务熔断支持
@SpringBootApplication
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.xmg.ribbon.rule.*")
})
@RibbonClients(value = {@RibbonClient(value = "service-provider", configuration = ServiceProviderIRule.class)})
public class ServiceConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceConsumerApplication.class);
    }
}
