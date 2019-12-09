package com.changgou.system;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

//获取客户端的访问IP
@Component
public class IpFilter implements GlobalFilter, Ordered {

    // 具体逻辑
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取客户端的ip
        System.out.println("1获取客户端的ip");
        ServerHttpRequest request = exchange.getRequest();
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        System.out.println(remoteAddress.getHostName());
        // 放行
        return chain.filter(exchange);
    }

    //过滤器的优先级 返回值越小优先级越高
    @Override
    public int getOrder() {
        return 1;
    }
}
