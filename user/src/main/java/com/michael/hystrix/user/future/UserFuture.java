package com.michael.hystrix.user.future;

import com.michael.hystrix.user.entity.User;
import com.michael.hystrix.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

//@Service
public class UserFuture {

    @Autowired
    private UserService userService;

    @Async
    public CompletableFuture<String> createUserTimeout(User user){
        String result = userService.createUserTimeout(user);
        return CompletableFuture.completedFuture(result);
    }
}
