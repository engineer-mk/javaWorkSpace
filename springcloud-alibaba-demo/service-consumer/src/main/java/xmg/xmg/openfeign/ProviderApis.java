package xmg.xmg.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "provider")
@Component
public interface ProviderApis {

    @RequestMapping(value = "/provider/point", method = RequestMethod.GET)
    String getPort();

    @RequestMapping(value = "/provider/order/add", method = RequestMethod.POST)
    void addOrder(@RequestParam Integer userid, @RequestParam String orderNumber);
}
