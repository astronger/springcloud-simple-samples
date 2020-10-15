package com.example.hystrix;

import com.example.commons.User;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.checkerframework.checker.units.qual.A;
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

    @Autowired
    UserService userService;

    @GetMapping("/hello5")
    public void hello5() throws ExecutionException,InterruptedException{
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        UserCollapseCommand cmd1 = new UserCollapseCommand(userService, 99);
        UserCollapseCommand cmd2 = new UserCollapseCommand(userService, 98);
        UserCollapseCommand cmd3 = new UserCollapseCommand(userService, 97);

        Future<User> q1 = cmd1.queue();
        Future<User> q2 = cmd2.queue();
        Future<User> q3 = cmd3.queue();

        User u1 = q1.get();
        User u2 = q2.get();
        User u3 = q3.get();

        System.out.println(u1);
        System.out.println(u2);
        System.out.println(u3);
        Thread.sleep(2000);//睡2秒就变成两次请求，
        UserCollapseCommand cmd4 = new UserCollapseCommand(userService, 96);
        Future<User> q4 = cmd4.queue();
        User u4 = q4.get();
        System.out.println(u4);
        ctx.close();

    }

    /**
     * 注解方式实现请求合并 测试方法
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/hello6")
    public void hello6() throws ExecutionException, InterruptedException {
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        Future<User> q1 = userService.getUserById(99);
        Future<User> q2 = userService.getUserById(98);
        Future<User> q3 = userService.getUserById(97);
        User u1 = q1.get();
        User u2 = q2.get();
        User u3 = q3.get();
        System.out.println(u1);
        System.out.println(u2);
        System.out.println(u3);
        Thread.sleep(2000);
        Future<User> q4 = userService.getUserById(96);
        User u4 = q4.get();
        System.out.println(u4);
        ctx.close();
    }
}
