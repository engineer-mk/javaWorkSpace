package xmg.xmg.accountmanage.service.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import xmg.xmg.accountmanage.model.AccountModel;
import xmg.xmg.accountmanage.service.AccountManageServiceFeign;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局服务降级示例
 */
@DefaultProperties(
        defaultFallback = "defaultFallback",
        commandProperties = {
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
        }
)
@Service
public class HystrixExamples05 {
    @Resource
    AccountManageServiceFeign accountManageServiceFeign;

    //    无指定fallbackMethod则使用默认的defaultFallback
    //    @HystrixCommand(fallbackMethod = "defaultFallback2")
    @HystrixCommand
    public List<AccountModel> getAccountList() {
        System.out.println(Thread.currentThread().getName());
        final long start = System.currentTimeMillis();
        final List<AccountModel> accountList = accountManageServiceFeign.getAccountList();
        final long end = System.currentTimeMillis();
        System.out.println("调用花费时间:" + (end - start));
        return accountList;
    }

    public List<AccountModel> defaultFallback() {
        final ArrayList<AccountModel> list = new ArrayList<>();
        list.add(new AccountModel(100, "缺省返回", "缺省返回"));
        return list;
    }

    public List<AccountModel> defaultFallback2() {
        final ArrayList<AccountModel> list = new ArrayList<>();
        list.add(new AccountModel(200, "缺省返回", "缺省返回"));
        return list;
    }
}
