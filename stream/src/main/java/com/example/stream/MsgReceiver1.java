package com.example.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.util.Date;

// 绑定自定义消息通道【 MyChannel 为刚才自己建立的通道】
@EnableBinding(MyChannel.class)
public class MsgReceiver1 {

    private static final Logger logger = LoggerFactory.getLogger(MsgReceiver1.class);

    // 接收器
    @StreamListener(MyChannel.INPUT)
    public void receive(Object payload){
        logger.info("received1: " + payload + ":" + new Date());
    }

}

