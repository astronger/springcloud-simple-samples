package com.example.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("provider")
public interface HelloService {

    @GetMapping("/hello")
    String hello();//这里的方法名随意取 无需遵循java规则
}
