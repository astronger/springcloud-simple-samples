package com.example.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 在这个方法中，将发起一个远程调用，去调用provider中提供的 /hello接口
     * 但是调用这个方法可能会失败，
     *
     * 所以在方法上面添加 @HystrixCommand ，配置fallbackMethod 属性，这个属性表示该方法调用失败时的临时替代方法
     * 【专业术语：服务降级】 */
    @HystrixCommand(fallbackMethod = "error")//如果希望直接抛出异常，不做服务降级 。则再加上ignoreExceptions配置(fallbackMethod = "error",ignoreExceptions = ArithmeticException.class)
    public String hello(){
        int i = 1 / 0;
        return restTemplate.getForObject("http://provider/hello",String.class);
    }

    @HystrixCommand(fallbackMethod = "error")
    public Future<String> hello2(){
        return new AsyncResult<String>(){

            @Override
            public String invoke() {
                return restTemplate.getForObject("http://provider/hello",String.class);
            }
        };
    }

    /**
     * 这个方法名要和 fallbackMethod一致
     * 方法返回值也要一致 【比如这里都是String类型，error也要一致】
     * 此处 调用 备案方案 ，比如数据库崩了，就要去查缓存
     *
     * *    **下面也可以继续写HystrixCommand调用error2 ，然后error2再去调用其它的方法，方法一样，依次往下，这就是【服务降级】
     * *    **越往下，数据的获取能力越来越容易，准确性可能降低，但不会让系统挂掉。
     * *    ** 这就是Hystrix 的作用： 1. 降级  2. 容错。  避免雪崩  。
     *
     * @return
     */
    //@HystrixCommand(fallbackMethod = "error2")
    public String error(Throwable t){
        return "error"+t.getMessage();
    }
}
