package com.api.rpc;

import java.util.concurrent.CompletableFuture;

/**
 * @author fhc
 * @version 1.0
 * @description: TODO
 * @date 2023/3/22 21:04
 */
public interface  RpcDemoService {
    String sayHello(String name);
    default CompletableFuture<String> sayHelloAsync(String name) {
        return CompletableFuture.completedFuture(sayHello(name));
    }
}
