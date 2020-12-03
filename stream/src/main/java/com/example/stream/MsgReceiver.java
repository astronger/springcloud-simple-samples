package com.example.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

// 该注解表示绑定Sink消息通道
@EnableBinding(Sink.class)
public class MsgReceiver {

    private static final Logger logger = LoggerFactory.getLogger(MsgReceiver.class);
    // 自带 消费者
    @StreamListener(Sink.INPUT)//(sink是一个默认的消息通道)
    public void receive(Object payload){
        logger.info("received: " + payload);
    }
}
