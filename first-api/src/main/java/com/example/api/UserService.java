package com.example.api;

import com.example.commons.User;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

public interface UserService {

    @GetMapping("/hello")
    String hello();//这里的方法名随意取 无需遵循java规则

    @GetMapping("/hello2")
    String hello2(@RequestParam("name") String name);// k/v形式要标记参数名称

    @PostMapping("/user2")
    User addUser2(@RequestBody User user);

    @DeleteMapping("/user14/{id}")
    void deleteUser2(@PathVariable("id") Integer id);

    @GetMapping("/user3")
    void getUserByName(@RequestHeader("name") String name) throws UnsupportedEncodingException;//放在header之后的参数，一定要转码传递
}
