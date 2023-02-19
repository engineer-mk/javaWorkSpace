package com.xmg.demo.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope //动态刷新
@Slf4j
public class ProductController {
    @Value("${server.port}")
    private String port;
    @Value("${config.info}")
    private String info;

    @RequestMapping(value = "/provider/point", method = RequestMethod.GET)
    public String getPort() {
        return this.port;
    }

    @RequestMapping(value = "/provider/config/info", method = RequestMethod.GET)
    public String getInfo() {
        return this.info;
    }

    @RequestMapping(value = "/provider/number", method = RequestMethod.GET)
    public Integer getNumber(Integer number) {
        if (number < 0) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return number;
    }

    @RequestMapping(value = "/provider/hotKey")
    @SentinelResource(value = "provider/hotKey",blockHandler = "def_hotKey")
    public String hotKey(Integer number) {
        return "number is " + number;
    }

    public String def_hotKey(Integer number, BlockException e) {
        return "访问受限";
    }
}
