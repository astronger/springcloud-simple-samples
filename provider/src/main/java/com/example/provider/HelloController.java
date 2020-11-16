package com.example.provider;

import com.example.api.UserService;
import com.example.commons.User;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;


@RestController
public class HelloController implements UserService {
    @Value("${server.port}")
    Integer port;
//    @GetMapping("/hello")
    @Override
    @RateLimiter(name="rlA")
    public String hello(){
        String s = "hello 你好"+port;
//        System.out.println(s);
      //  int i = 1 / 0;//此处测试resilience4j-2 【Retry、CircuitBreaker、RateLimiter】模块抛出异常

        System.out.println(new Date()); //测试RateLimiter
        return s;
    }

    @GetMapping("/hello2")
    @Override
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
    @Override
    public User addUser2(@RequestBody User user){
        return user;
    }

    @PutMapping("/user11")
    public void updateUser1(User user1){
        System.out.println(user1);
    }

    @PutMapping("/user12")
    public void updateUser2(@RequestBody User user2){
        System.out.println(user2);
    }

    @DeleteMapping("/user13")
    public void deleteUser1(Integer id){
        System.out.println(id);
    }

    @DeleteMapping("/user14/{id}")
    @Override
    public void deleteUser2(@PathVariable Integer id){
        System.out.println(id);
    }

    @GetMapping("/user3")
    @Override
    public void getUserByName(@RequestHeader String name) throws UnsupportedEncodingException {
        System.out.println(URLDecoder.decode(name,"UTF-8"));
    }
}
