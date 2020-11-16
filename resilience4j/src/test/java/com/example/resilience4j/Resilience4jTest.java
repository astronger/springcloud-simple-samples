package com.example.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import org.junit.Test;

import java.time.Duration;
import java.util.Date;

public class Resilience4jTest {

    /**
     * 一个正常的例子
     */
    @Test
    public void test1() {
        //获取一个CircuitBreakerRegistry实例，可以调用ofDefaults获取一个CircuitBreakerRegistry实例，也可以自定义属性。
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                //故障率阈值百分比(百分之50)，超过这个阈值，断路器就会打开
                .failureRateThreshold(50)
                //断路器保持打开的时间，在到达设置的时间之后，断路器会进入到 half open 状态
                .waitDurationInOpenState(Duration.ofMillis(1000))
                //当断路器处于half open 状态时，环形缓冲区的大小
                .ringBufferSizeInHalfOpenState(2)
                .ringBufferSizeInClosedState(2)
                .build();
        CircuitBreakerRegistry r1 = CircuitBreakerRegistry.of(config);
        CircuitBreaker cb1 = r1.circuitBreaker("javaboy");//创建断路器
        CircuitBreaker cb2 = r1.circuitBreaker("javaboy2", config);
        CheckedFunction0<String> supplier = CircuitBreaker.decorateCheckedSupplier(cb1, () -> "hello resilience4j");
        Try<String> result = Try.of(supplier)
                .map(v -> v + " hello world");
        System.out.println(result.isSuccess());
        System.out.println(result.get());
    }

    /**
     * 一个出异常的断路器
     */
    @Test
    public void test2() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                //故障率阈值百分比，超过这个阈值，断路器就会打开
                .failureRateThreshold(50)
                //断路器保持打开的时间，在到达设置的时间之后，断路器会进入到 half open 状态
                .waitDurationInOpenState(Duration.ofMillis(1000))
                //当断路器处于half open 状态时，环形缓冲区的大小
                .ringBufferSizeInClosedState(2)
                .build();
        CircuitBreakerRegistry r1 = CircuitBreakerRegistry.of(config);
        CircuitBreaker cb1 = r1.circuitBreaker("javaboy");
        System.out.println(cb1.getState());//获取断路器的一个状态
        cb1.onError(0, new RuntimeException());
        System.out.println(cb1.getState());//获取断路器的一个状态
        cb1.onError(0, new RuntimeException());
        System.out.println(cb1.getState());//获取断路器的一个状态
        CheckedFunction0<String> supplier = CircuitBreaker.decorateCheckedSupplier(cb1, () -> "hello resilience4j");
        Try<String> result = Try.of(supplier)
                .map(v -> v + " hello world");
        System.out.println("result.isSuccess()="+result.isSuccess());
        System.out.println("result.get()="+result.get());
    }

    /**
     * 限流 和断路器类似
     */
    @Test
    public void test3(){
        RateLimiterConfig build = RateLimiterConfig.custom()
                // 阈值刷新的时间 1 秒
                .limitRefreshPeriod(Duration.ofMillis(1000))
                // 限制频次
                .limitForPeriod(2)
                // 限流之后的冷却时间 1秒
                .timeoutDuration(Duration.ofMillis(1000))
                .build();
        RateLimiter limiter = RateLimiter.of("learning", build);

        CheckedRunnable runnable = RateLimiter.decorateCheckedRunnable(limiter, () -> {
            System.out.println(new Date());
        });

        // 执行4次
        Try.run(runnable)
                .andThenTry(runnable)
                .andThenTry(runnable)
                .andThenTry(runnable)
                .onFailure(t -> System.out.println(t.getMessage()));
    }

    /**
     * 请求重试
      */
        @Test
        public void test4(){
        RetryConfig config = RetryConfig.custom()
                // 重试次数 【小于等于下面的count次数就抛出异常】
                .maxAttempts(4)
                // 重试间隔
                .waitDuration(Duration.ofMillis(500))
                // 重试异常
                .retryExceptions(RuntimeException.class)
                .build();

        Retry retry = Retry.of("leaning1", config);
        Retry.decorateRunnable(retry, new Runnable() {
            int count = 0;
            // 重试功能开启后 执行run方法 若抛出异常 会自动触发重试功能
            @Override
            public void run() {
                if (count++ < 4){
                    System.out.println(count);
                    throw new RuntimeException();
                }
            }
        }).run();
    }

}
