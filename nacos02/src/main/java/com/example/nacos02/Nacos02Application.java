package com.example.nacos02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Nacos02Application {

    public static void main(String[] args) {
        SpringApplication.run(Nacos02Application.class, args);
    }

    @Bean
    @LoadBalanced //开启负载均衡
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
