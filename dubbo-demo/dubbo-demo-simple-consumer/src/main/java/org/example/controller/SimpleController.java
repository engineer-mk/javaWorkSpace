package org.example.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.simple.SimpleApi;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

/**
 * @author makui
 * @created 2023/3/16
 **/
@RestController
public class SimpleController {
    @DubboReference(url = "localhost:10088")
    private SimpleApi simpleApi;

    @RequestMapping(value = "/hello/{str}", method = RequestMethod.GET)
    public String hello(@PathVariable String str) {
        return simpleApi.hello(str);
    }

    @RequestMapping(value = "/helloDubbo/{str}", method = RequestMethod.GET)
    public String helloDubbo(@PathVariable String str) {
        final CompletableFuture<String> helloFuture = simpleApi.helloDubbo(str);
        final CompletableFuture<String> hiFuture = simpleApi.hiDubbo(str);
        CompletableFuture.allOf(helloFuture, hiFuture).join();
        try {
            return helloFuture.get() + hiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
