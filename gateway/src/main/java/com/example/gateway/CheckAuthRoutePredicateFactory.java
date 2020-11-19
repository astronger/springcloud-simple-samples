package com.example.gateway;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Predicate;

// 自定义路由断言工厂
// 命名需要以RoutePredicateFactory结尾 比aRoutePredicateFactory 那么yml在使用时a就是断言工厂的名字

/**
 * yml配置如下；
 *spring:
 *   cloud:
 *     gateway:
 *       routes:
 *         # 自定义断言工厂 -name就是之前以xxxRoutePredicateFactory的xxxx为断言工厂名
 *         - id: customer_route
 *           uri: http://httpbin.org
 *           predicates:
 *           - name: CheckAuth
 *             args:
 *               name: kitty
 *
 *
 */
@Component
public class CheckAuthRoutePredicateFactory extends AbstractRoutePredicateFactory<CheckAuthRoutePredicateFactory.User> {

    public CheckAuthRoutePredicateFactory() {
        super(User.class);
    }

    // 自定义配置类
    @Override
    public Predicate<ServerWebExchange> apply(User config) {
        return exchange -> {
            System.out.println("进入apply：" + config.getName());
            if (config.getName().equals("kitty")){
                return true;
            }
            return false;
        };
    }

    public static class User{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

