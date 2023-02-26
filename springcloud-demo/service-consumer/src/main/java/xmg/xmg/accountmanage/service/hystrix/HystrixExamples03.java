package xmg.xmg.accountmanage.service.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import xmg.xmg.accountmanage.model.AccountModel;
import xmg.xmg.accountmanage.service.AccountManageServiceFeign;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 信号量隔离示例
 */
@Service
public class HystrixExamples03 {
    @Resource
    AccountManageServiceFeign accountManageServiceFeign;


    //超时或异常或超出允许等待数量执行服务降级
    @HystrixCommand(commandProperties = {
            //超时时间，默认1000ms，超时执行拒绝策略
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
            //信号量隔离
            @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,
                    value = "SEMAPHORE"),
            //信号量最大并发
            @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS,
                    value = "6")
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
