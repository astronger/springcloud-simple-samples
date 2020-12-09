package com.example.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope //动态刷新
public class HelloController {

    @Value("${name}")
    String name;

    @GetMapping("/hello")
    public String hello(){
        return name;
    }
}
