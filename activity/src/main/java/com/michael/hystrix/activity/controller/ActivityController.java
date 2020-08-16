package com.michael.hystrix.activity.controller;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ActivityController {

    @PostMapping("/createActivity")
    public String createActivity(@RequestBody String userId){
        System.out.println("为用户" +userId + "创建了优惠活动");
        return "SUCCESS";
    }

    @PostMapping("/createActivityTimeout")
    public String createActivityTimeout(@RequestBody String userId){
        try {
            TimeUnit.SECONDS.sleep(RandomUtils.nextLong(0, 5) + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("为用户" +userId + "创建了优惠活动");
        return "SUCCESS";
    }

    @PostMapping("/createActivityError")
    public String createActivityError(@RequestBody String userId){
        throw new RuntimeException("活动服务不可用");
    }
}
