package com.example.consumer;

import com.example.commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
public class UserHelloController {


    /** 测试死的地址访问
     * */
    @GetMapping("/hello1")
    public String hello1() throws MalformedURLException {
        HttpURLConnection con = null;
        try {
            URL url = new URL("http://127.0.0.1:1113/hello");//这里测试写死provider地址，耦合度太高
            con = (HttpURLConnection) url.openConnection();
            if(con.getResponseCode() == 200){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String s = bufferedReader.readLine();
                bufferedReader.close();
                return s;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 动态配置调用地址
     * **/
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    @Qualifier("restTemplateOne")
    RestTemplate restTemplateOne;
    @GetMapping("/hello2")
    public String hello2(){
        //Euerka Client提供的 DiscoveryClient 工具，DiscoveryClient查询到的服务列表是一个集合，因为服务在部署的过程中，可能是集群部署，集合中的每一项就是一个实例
        List<ServiceInstance> list = discoveryClient.getInstances("provider");//这里的serviceId(provider)改为你要调用的服务名字
        ServiceInstance serviceInstance = list.get(0);//这里假设不是集合部署
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        StringBuffer sb = new StringBuffer();
        sb.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
        String s = restTemplateOne.getForObject(sb.toString(), String.class);
        return s;
    }

    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;
    @GetMapping("/hello3")
    public String hello3(){
        //restTemplate有负载均衡功能，直接给服务名调用就行，provider如下；否则不行
        return restTemplate.getForObject("http://provider/hello",String.class);
    }

    /** restTemplate get请求 */
    @GetMapping("/hello4")
    public void hello4(){
        String s1 = restTemplate.getForObject("http://provider/hello2?name={1}",String.class,"javaone");
        System.out.println(s1);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://provider/hello2?name={1}", String.class, "javaone");
        String body = responseEntity.getBody();
        System.out.println("body:"+body);
        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println("HttpStatus:"+statusCode);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        System.out.println("statusCodeValue:"+statusCodeValue);
        HttpHeaders headers = responseEntity.getHeaders();
        Set<String> keySet = headers.keySet();
        System.out.println("---------headers--------");
        for (String s : keySet) {
            System.out.println(s+":"+headers.get(s)+"///");
        }
    }

    /** restTemplate get请求
     *
     *getForObject()三种重载方法【三种不同传参方式】 例子
     *getForEntity()也类似
     * */
    @GetMapping("/hello5")
    public void hello5(){
        String s1 = restTemplate.getForObject("http://provider/hello2?name={1}",String.class,"javatwo");
        System.out.println("1:"+s1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","zhangsan");
        s1 = restTemplate.getForObject("http://provider/hello2?name={name}", String.class,map);
        System.out.println("2:"+s1);
        try {
            String url = "http://provider/hello2?name="+ URLEncoder.encode("李四","UTF-8");
            URI uri = URI.create(url);
            s1 = restTemplate.getForObject(uri, String.class);
            System.out.println("3:"+s1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /** restTemplate post请求
     *
     *postForObject()三种重载方法【三种不同传参方式】 例子
     *postForEntity()也类似
     * */
    @GetMapping("/hello6")
    public void hello6(){
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("username","javajava");
        map.add("password","123456");
        map.add("id",666);
        User user = restTemplate.postForObject("http://provider/user1", map, User.class);
        System.out.println("1:"+user);

        user.setId(888);
        user = restTemplate.postForObject("http://provider/user2", user, User.class);
        System.out.println("2:"+user);
    }


    /**
     * restTemplate post请求
     * postForLocation 方法
     *【当执行完一个post请求后，如果立马要进行重定向，比如注册就是一个post请求，立马要重定向到登录页面，此时就可以使用postForLocation】
     * 这里的post接口，响应一定是302，否则postForLocation无效
     * 重定向的地址，要写成绝对路径，否则调用时会出问题
     */
    @GetMapping("/hello7")
    public void hello7() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("username", "javajava");
        map.add("password", "123456");
        map.add("id", 666);
        URI uri = restTemplate.postForLocation("http://provider/register", map);
        String s = restTemplate.getForObject(uri, String.class);
        System.out.println(s);

    }

/**
 * restTemplate put请求
 * put接口传参其实和post一样，也是两种类型的参数，key/value 形式以及json形式
 */
    @GetMapping("/hello8")
    public void hello8() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("username", "javajava");
        map.add("password", "123456");
        map.add("id", 666);
        restTemplate.put("http://provider/user11",map);
        User user = new User();
        user.setId(98);
        user.setUsername("zhangsan");
        user.setPassword("123456");
        restTemplate.put("http://provider/user12",user);
    }


    /**
     * restTemplate delete请求
     * 两种类型的参数，key/value形式以及PathVariable形式(参数放在路径中)
     * delete中的参数传递，也支持map，实际上和get一样
     */
    @GetMapping("/hello9")
    public void hello9() {
        restTemplate.delete("http://provider/user13?id={1}",99);
        restTemplate.delete("http://provider/user14/{1}",99);
    }


//    @GetMapping("/hello2")
//    public String hello2()throws MalformedURLException{
//        //Euerka Client提供的 DiscoveryClient 工具，DiscoveryClient查询到的服务列表是一个集合，因为服务在部署的过程中，可能是集群部署，集合中的每一项就是一个实例
//        List<ServiceInstance> list = discoveryClient.getInstances("provider");//这里的serviceId(provider)改为你要调用的服务名字
//        ServiceInstance serviceInstance = list.get(0);//这里假设不是集合部署
//        String host = serviceInstance.getHost();
//        int port = serviceInstance.getPort();
//        StringBuffer sb = new StringBuffer();
//        sb.append("http://")
//                .append(host)
//                .append(":")
//                .append(port)
//                .append("/hello");
//        HttpURLConnection con = null;
//        try {
//            URL url = new URL(sb.toString());//这里测试写死provider地址，耦合度太高
//            con = (HttpURLConnection) url.openConnection();
//            if(con.getResponseCode() == 200){
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                String s = bufferedReader.readLine();
//                bufferedReader.close();
//                return s;
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "error";
//    }

//    int count = 0;
//    @GetMapping("/hello3")
//    public String hello3()throws MalformedURLException{
//        //Euerka Client提供的 DiscoveryClient 工具，DiscoveryClient查询到的服务列表是一个集合，因为服务在部署的过程中，可能是集群部署，集合中的每一项就是一个实例
//        List<ServiceInstance> list = discoveryClient.getInstances("provider");//这里的serviceId(provider)改为你要调用的服务名字
//        ServiceInstance serviceInstance = list.get((count++) % list.size());//集合部署
//        String host = serviceInstance.getHost();
//        int port = serviceInstance.getPort();
//        StringBuffer sb = new StringBuffer();
//        sb.append("http://")
//                .append(host)
//                .append(":")
//                .append(port)
//                .append("/hello");
//        HttpURLConnection con = null;
//        try {
//            URL url = new URL(sb.toString());//这里测试写死provider地址，耦合度太高
//            con = (HttpURLConnection) url.openConnection();
//            if(con.getResponseCode() == 200){
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                String s = bufferedReader.readLine();
//                bufferedReader.close();
//                return s;
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "error";
//    }
}
