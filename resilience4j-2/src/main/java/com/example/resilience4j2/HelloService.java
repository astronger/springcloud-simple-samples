package com.example.resilience4j2;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
//@Retry(name = "retryA")//【Retry】表示要使用的重试策略
@CircuitBreaker(name = "cba" , fallbackMethod = "error")
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    public String hello(){
        return restTemplate.getForObject("http://127.0.0.1:1113/hello", String.class);
    }

    // 限流配置
    public String hello1(){
        for (int i = 0; i < 5; i++) {
            restTemplate.getForObject("http://127.0.0.1:1113/hello", String.class);
        }
        return "success ratA";
    }

    // 服务降级方法中 不加参数Throwable 会报错提示缺少Throwable 要添加异常参数
    public String error(Throwable throwable){
        return "error";
    }
}
