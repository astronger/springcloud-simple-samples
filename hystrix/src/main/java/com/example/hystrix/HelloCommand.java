package com.example.hystrix;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;

public class HelloCommand extends HystrixCommand<String> {

    RestTemplate restTemplate;

    public HelloCommand(Setter setter,RestTemplate restTemplate) {
        super(setter);
        this.restTemplate = restTemplate;
    }

    /**
     * String类型为上面定义的泛型
     * @return
     * @throws Exception
     */
    @Override
    protected String run() throws Exception {
        int i = 1 / 0;//测试异常
        return restTemplate.getForObject("http://provider/hello",String.class);
    }

    /**
     * 请求失败的回调
     * @return
     */
    @Override
    protected String getFallback(){
        return "error-extends"+getFailedExecutionException().getMessage();
    }

}
