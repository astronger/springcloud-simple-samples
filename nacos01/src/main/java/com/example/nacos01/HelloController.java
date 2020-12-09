package com.example.nacos01;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${server.port}")
    Integer port;//添加端口号，测试集群

    @GetMapping("/hello")
    public String hello(){
        return "hello:"+port;
    }
}
