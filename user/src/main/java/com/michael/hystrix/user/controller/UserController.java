package com.michael.hystrix.user.controller;

import com.michael.hystrix.user.entity.User;
import com.michael.hystrix.user.service.UserServiceBulkhead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    @Autowired
//    private UserService userService;
    private UserServiceBulkhead userService;

    @Autowired
//    private UserFuture userFuture;

    @PostMapping("/createUser")
    public String createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PostMapping("/createUserTimeout")
    public String createUserTimeout(@RequestBody User user){
        return userService.createUserTimeout(user);
    }

    @PostMapping("/createUserFallback")
    public String createUserFallback(@RequestBody User user){
        return userService.createUserFallback(user);
    }

    @PostMapping("/createUserCircuitOpen")
    public String createUserCircuitOpen(@RequestBody User user){
        return userService.createUserCircuitOpen(user);
    }

    @Async
    @PostMapping("/createUserFuture")
    public CompletableFuture<String> createUserFuture(@RequestBody User user){
        final User user1 = user;
        return CompletableFuture.supplyAsync(()->{
            return userService.createUserTimeout(user1);
        });
//        return userFuture.createUserTimeout(user);
    }

}
