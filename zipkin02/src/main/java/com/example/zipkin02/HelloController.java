package com.example.zipkin02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/hello")
    public void hello(String name){
        String s = restTemplate.getForObject("http://127.0.0.1:8080/hello?name={1}", String.class, "javaboy");
        logger.info(s);
    }
}
