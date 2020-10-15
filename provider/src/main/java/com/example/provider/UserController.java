package com.example.provider;

import com.example.commons.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @GetMapping("/user/{ids}")//假设 consumer 传过来的多个id格式是 1,2,3,4....
    public List<User> getUserByIds(@PathVariable String ids){
        String[] split = ids.split(",");
        List<User> user = new ArrayList<>();
        for(String s : split){
            User user1 = new User();
            user1.setId(Integer.parseInt(s));
            user.add(user1);
        }
        return user;
    }

}
