package org.example.simple;

import java.util.concurrent.CompletableFuture;

/**
 * @author makui
 * @created 2023/3/16
 **/
public interface SimpleApi {

    String hello(String param);

    CompletableFuture<String> helloDubbo(String param);

    CompletableFuture<String> hiDubbo(String param);
}
