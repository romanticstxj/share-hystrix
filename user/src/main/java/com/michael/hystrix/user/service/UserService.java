package com.michael.hystrix.user.service;

import com.michael.hystrix.user.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    public String createUser(User user){
        System.out.println("begin to create user for " + user);
        return restTemplate.postForObject("http://localhost:8082/createActivity", user.getUserId(), String.class);
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })
    public String createUserTimeout(User user) {
        System.out.println("begin to create user for " + user);
        return restTemplate.postForObject("http://localhost:8082/createActivityTimeout", user.getUserId(), String.class);
    }

    /**
     * 当活动服务不可用时，使用备用方案
     * @param user
     * @return
     */
    @HystrixCommand(fallbackMethod = "createUserFallback0")
    public String createUserFallback(User user) {
        System.out.println("begin to create user for " + user);
        return restTemplate.postForObject("http://localhost:8082/createActivityError", user.getUserId(), String.class);
    }

    public String createUserFallback0(User user) {
        return "备用方案";
    }
}
