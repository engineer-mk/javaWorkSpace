package xmg.xmg.demo;

import xmg.xmg.openfeign.ProviderApis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
    @Autowired
    ProviderApis providerApis;

    @RequestMapping(value = "/consumer/provider/point",method = RequestMethod.GET)
    public String point(){
        return providerApis.getPort();
    }
}
