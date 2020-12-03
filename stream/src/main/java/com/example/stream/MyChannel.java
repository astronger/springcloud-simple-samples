package com.example.stream;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MyChannel {
    String INPUT = "test-input";
    String OUTPUT = "test-output";

    /**
     * 这两个通道可能定义在两个不同的通道里面，这里为了方便放在同一个项目中演示
     * F版之前消息通道的名称可以相同
     * @return
     */
    // 收(订阅频道/消息消费者)
    @Input(INPUT)
    SubscribableChannel input();

    // 发（消息生产者）
    @Output(OUTPUT)
    MessageChannel output();
}

