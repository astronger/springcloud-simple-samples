package com.example.openfeign;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients  //开启feign支持
public class OpenfeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenfeignApplication.class, args);
    }

    @Bean
    Logger.Level loggerLevel(){
        return Logger.Level.FULL;
    }

}
