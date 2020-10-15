package com.example.hystrix;

import com.ctc.wstx.util.StringUtil;
import com.example.commons.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand
    public List<User> getUsersByIds(List<Integer> ids){
        User[] users = restTemplate.getForObject("http://provider/user/{1}", User[].class, StringUtils.join(ids, ","));
        return Arrays.asList(users);
    }
}
