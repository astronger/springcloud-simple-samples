package com.example.openfeign;

import com.example.commons.User;
import com.sun.deploy.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String hello() throws Exception{
        String s = helloService.hello2("那杯热咖啡");
        System.out.println(s);
        User user = new User();
        user.setId(1);
        user.setUsername("java");
        user.setPassword("123");
//        User user1 = helloService.addUser(user);
//        System.out.println(user1);
//        helloService.deleteUserById(1);
        helloService.getUserByName(URLEncoder.encode("那杯热咖啡","UTF-8"));
        return helloService.hello();
    }
}
