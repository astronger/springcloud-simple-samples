package com.example.openfeign;

import com.example.api.UserService;
import com.example.commons.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "provider",fallbackFactory = HelloServiceFallbackFactory.class)
public interface HelloService extends UserService {

//    @GetMapping("/hello")
//    String hello();//这里的方法名随意取 无需遵循java规则
//
//    @GetMapping("/hello2")
//    String hello2(@RequestParam("name") String name);// k/v形式要标记参数名称
//
//    @PostMapping("/user2")
//    User addUser(@RequestBody User user);
//
//    @DeleteMapping("/user14/{id}")
//    void deleteUserById(@PathVariable("id") Integer id);
//
//    @GetMapping("/user3")
//    void getUserByName(@RequestHeader("name") String name);//放在header之后的参数，一定要转码传递
}
