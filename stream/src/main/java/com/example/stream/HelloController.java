package com.example.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(MsgReceiver1.class);

    @Autowired
    MyChannel myChannel;
    @GetMapping("/hello")
    public void hello(){
        logger.info("send msg:"+new Date());
        myChannel.output().send(MessageBuilder.withPayload("你好 spring cloud stream!").setHeader("x-delay",3000).build());
    }
}
