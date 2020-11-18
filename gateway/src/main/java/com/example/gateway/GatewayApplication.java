package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * 编码式请求转发，还有一种配置yml方法，配置前注销此方法
     * @param builder
     * @return
     */
//    @Bean
//    RouteLocator routeLocator(RouteLocatorBuilder builder){
//        // 可以添加多个
//        return builder.routes()
//                // 访问网址 http://httpbin.org
//                // 通过网关后访问 http://localhost:8080/get
//                .route("javaboy", r -> r.path("/get").uri("http://httpbin.org"))
//                .build();
//    }

}
