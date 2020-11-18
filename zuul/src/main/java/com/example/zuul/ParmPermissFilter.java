package com.example.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

// 简单权限过滤器(只是试验而已)
@Component
public class ParmPermissFilter extends ZuulFilter {

    // 过滤器类型 权限判断一般是pre
    @Override
    public String filterType() {
        return "pre";
    }

    // 过滤器优先级
    @Override
    public int filterOrder() {
        return 0;
    }

    // 是否过滤 修改一下(true --> 是)
    @Override
    public boolean shouldFilter() {
        return true;
    }

    // 判断逻辑
    // 在源码已经说过了 该返回值忽略它 无所谓返回了
    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String name = request.getParameter("name");
        if (!"烤肉".equals(name)) {
            // 设置响应返回
            // 设置响应头(乱码)
            context.addZuulResponseHeader("content-type","text/html;charset=utf-8");
            // 响应码
            context.setResponseStatusCode(401);
            context.setResponseBody("请求拦截");
            context.setSendZuulResponse(false);
        }
        return null;
    }
}

