package com.example.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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

    @GetMapping("/hello2")
    public String hello2()throws MalformedURLException{
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
        HttpURLConnection con = null;
        try {
            URL url = new URL(sb.toString());//这里测试写死provider地址，耦合度太高
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

    int count = 0;
    @GetMapping("/hello3")
    public String hello3()throws MalformedURLException{
        //Euerka Client提供的 DiscoveryClient 工具，DiscoveryClient查询到的服务列表是一个集合，因为服务在部署的过程中，可能是集群部署，集合中的每一项就是一个实例
        List<ServiceInstance> list = discoveryClient.getInstances("provider");//这里的serviceId(provider)改为你要调用的服务名字
        ServiceInstance serviceInstance = list.get((count++) % list.size());//集合部署
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        StringBuffer sb = new StringBuffer();
        sb.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
        HttpURLConnection con = null;
        try {
            URL url = new URL(sb.toString());//这里测试写死provider地址，耦合度太高
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
}
