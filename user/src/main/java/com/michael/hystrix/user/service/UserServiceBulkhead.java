package com.michael.hystrix.user.service;

import com.michael.hystrix.user.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceBulkhead {

    @Autowired
    RestTemplate restTemplate;

    public String createUser(User user){
        System.out.println("begin to create user for " + user);
        return restTemplate.postForObject("http://localhost:8082/createActivity", user.getUserId(), String.class);
    }

    /**
     * 定义超时时间，让用户服务超时了则不再等待，释放线程
     * 线程隔离的配置参数可在HystrixThreadPoolProperties里找到
     * @param user
     * @return
     */
    @HystrixCommand(
            threadPoolKey = "createUserTimeout",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "2"),
                    @HystrixProperty(name = "maxQueueSize", value = "20")
            },
            commandProperties = {
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
            }
    )
    public String createUserTimeout(User user) {
        System.out.println("begin to create user for " + user);
        return restTemplate.postForObject("http://localhost:8082/createActivityTimeout", user.getUserId(), String.class);
    }

    /**
     * 当活动服务不可用时，使用备用方案
     * @param user
     * @return
     */
    @HystrixCommand(
            threadPoolKey = "createUserFallback",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "1"),
                    @HystrixProperty(name = "maxQueueSize", value = "20")
            },
            fallbackMethod = "createUserFallback0"
    )
    public String createUserFallback(User user) {
        System.out.println("begin to create user for " + user);
        return restTemplate.postForObject("http://localhost:8082/createActivityError", user.getUserId(), String.class);
    }

    /**
     * 在调用错误时开启一个3秒的时间窗，在此时间内若调用次数达到了2次，并且失败率大于50%，则跳闸；
     * 而跳闸后开启一个5秒的活动窗口，5秒后Hystrix会让请求通过，若请求成功，则再次关闭断路器
     * @param user
     * @return
     */
    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "3000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            }
    )
    public String createUserCircuitOpen(User user) {
        System.out.println("begin to create user for " + user);
        return restTemplate.postForObject("http://localhost:8082/createActivityError", user.getUserId(), String.class);
    }

    public String createUserFallback0(User user) {
        return "备用方案";
    }
}
