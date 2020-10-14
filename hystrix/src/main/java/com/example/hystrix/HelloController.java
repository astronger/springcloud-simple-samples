package com.example.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class HelloController {
    @Autowired
    HelloService helloService;
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/hello")
    public String hello(){
        return helloService.hello();
    }

    @GetMapping("/hello2")
    public void hello2(){
        HelloCommand helloCommand = new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("hellojava")), restTemplate);
        /** 两种调用方法
         *  一、直接调用【同步】
         *  【注意：HelloCommand new出来之后只能调用一次，两种方法只执行一次】
         * */
        String execute = helloCommand.execute();
        System.out.println(execute);
        HelloCommand helloCommand2 = new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("hellojava")), restTemplate);

        /** 二、先入队，后执行 【异步】*/
        String s = null;
        try {
            Future<String> queue = helloCommand2.queue();
            s = queue.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(s);

    }

    @GetMapping("/hello3")
    public void hello3(){
        Future<String> stringFuture = helloService.hello2();
        String s = null;
        try {
            s = stringFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(s);

    }
}
