package org.example.api;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.simple.SimpleApi;

import java.util.concurrent.CompletableFuture;

/**
 * @author makui
 * @created 2023/3/16
 **/
@DubboService
public class SimpleApiImpl implements SimpleApi {

    @Override
    public String hello(String param) {
        return "hello " + param;
    }

    @Override
    public CompletableFuture<String> helloDubbo(String param) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello " + param;
        });
    }

    @Override
    public CompletableFuture<String> hiDubbo(String param) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hi" + param;
        });
    }
}
