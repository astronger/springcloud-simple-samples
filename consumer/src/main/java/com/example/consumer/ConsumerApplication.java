package com.example.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    RestTemplate restTemplateOne(){
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced //使用 Ribbon 来快速实现负载均衡，添加此注解开启负载均衡【客户端负载均衡】
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
