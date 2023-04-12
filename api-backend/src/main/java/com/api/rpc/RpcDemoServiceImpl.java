package com.api.rpc;

import com.api.rpc.RpcDemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @author fhc
 * @version 1.0
 * @description: TODO
 * @date 2023/3/22 21:05
 */
@DubboService
public class RpcDemoServiceImpl implements RpcDemoService {
    @Override
    public String sayHello(String name) {
        System.out.println("Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hello " + name;
    }
}
