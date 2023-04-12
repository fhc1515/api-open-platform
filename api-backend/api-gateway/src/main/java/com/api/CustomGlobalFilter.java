package com.api;

/**
 * @author fhc
 * @version 1.0
 * @description:
 * @date 2023/3/20 0:02
 */

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.api.apiclientsdk.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    //白名单
    private static final List<String> IP_WHITE_LIST= Collections.singletonList("127.0.0.1");
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        1、请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识" + request.getId());
        log.info("请求路径" + request.getPath().value());
        log.info("请求方法" + request.getMethod());
        log.info("请求参数" + request.getQueryParams());
        log.info("请求来源地址" + request.getURI());
        log.info("请求来源地址" + request.getLocalAddress().getHostString());
        String remoteAddress = request.getLocalAddress().getHostString();
//      2、添加黑白名单
        if (!IP_WHITE_LIST.contains(remoteAddress)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
//        3、用户鉴权（判断ak、sk是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        //true为null
        boolean hasBlank = StrUtil.hasBlank(accessKey, body, sign, nonce, timestamp);
        // 判断是否有空
        if (hasBlank) {
            return handleNoAuth(exchange.getResponse());
        }
        if (!accessKey.equals("fhc")) {
            return handleNoAuth(exchange.getResponse());
        }
        // TODO 使用accessKey去数据库查询secretKey
        // 假设查到的secretKey是fhc 进行加密得到sign
        String secretKey = "abcdefgh";
        String sign1 = SignUtil.getSign(body, secretKey);
        if (!StrUtil.equals(sign, sign1)) {
            return handleNoAuth(exchange.getResponse());
        }
        // 时间戳是否为数字
        if (!NumberUtil.isNumber(timestamp)) {
            return handleNoAuth(exchange.getResponse());
        }
        //五分钟内的请求有效
        Long currentTime = System.currentTimeMillis() / 1000;
        Long FIVE_MINIUTES = 60 * 5L;
        if (currentTime - Long.parseLong(timestamp) > FIVE_MINIUTES) {
            return handleNoAuth(exchange.getResponse());
        }
//        4、请求的模拟接口是否存在

//        5、请求转发，调用模拟接口
        Mono<Void> filter = chain.filter(exchange);
        log.info("响应" + exchange.getResponse().getStatusCode());
////        7、调用成功，接口调用次数+1
//        if (exchange.getResponse().getStatusCode() == HttpStatus.OK) {
//            // 7. 调用成功，接口调用次数+1
//        } else {
//            // 8. 调用失败，返回规范错误码
//            return handleInvokeError(exchange.getResponse());
//        }
////        8、调用失败，返回一个规范的错误码
//        return filter;
        //6.响应日志
        return handleResponse(exchange,chain);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response){
        //403
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response){
        //500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }


    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            // 从交换机拿到原始response
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓冲区工厂 拿到缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        // 对象是响应式的
                        if (body instanceof Flux) {
                            // 我们拿到真正的body
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里面写数据
                            // 拼接字符串
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // TODO 7. 调用成功，接口调用次数+1
                                // data从这个content中读取
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                // 释放掉内存
                                DataBufferUtils.release(dataBuffer);
                                // 6.构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //data
                                String data = new String(content, StandardCharsets.UTF_8);
                                sb2.append(data);
                                // 打印日志
                                log.info("响应结果：" + data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 8.调用失败返回错误状态码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);// 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }
}