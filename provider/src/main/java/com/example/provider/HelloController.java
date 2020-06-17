package com.example.provider;

import com.example.commons.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @Value("${server.port}")
    Integer port;
    @GetMapping("/hello")
    public String hello(){
        return "hello 你好"+port;
    }

    @GetMapping("/hello2")
    public String hello2(String name){
        return "hello "+name;
    }

    // key/value形式传递
    @PostMapping("/user1")
    public User addUser1(User user){
        return user;
    }

    //json传递
    @PostMapping("/user2")
    public User addUser2(@RequestBody User user){
        return user;
    }

}
