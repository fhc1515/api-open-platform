package com.api;

import com.api.rpc.RpcDemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@EnableDubbo
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@Service
public class ApiGatewayApplication {

    @DubboReference
    private RpcDemoService rpcDemoService;

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(ApiGatewayApplication.class, args);
        ApiGatewayApplication application = context.getBean(ApiGatewayApplication.class);
        String result = application.doSayHello("world");
        System.out.println("result: " + result);
    }

    public String doSayHello(String name) {
        return rpcDemoService.sayHello(name);
    }



//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                 //路由                         //断言
//                .route("to_baidu", r -> r.path("/baidu")
//                        .uri("http://www.baidu.com/"))
//                .route("toyupiicu",r -> r.path("/yupiicu")
//                        .uri("http://yupi.icu"))
//                .build();
//    }
}
