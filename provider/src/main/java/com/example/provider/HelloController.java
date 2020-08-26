package com.example.provider;

import com.example.commons.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


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
    public void deleteUser2(@PathVariable Integer id){
        System.out.println(id);
    }
}
