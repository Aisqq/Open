package com.me.controller;


import com.me.dao.UserDao;
import com.me.entity.User;
import com.me.service.IotDeviceServer;
import com.me.utils.Message;
import com.me.utils.Result;
import com.me.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@Transactional
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private List<IotDeviceServer> iotDeviceServers;
    private final UserDao userDao;
    private final ChatClient chatClient;

    @PostMapping("/add")
    public Result<String> addTemp(@RequestBody Map<String ,Object> map) {
        User user = userDao.findUserByUsername("user");
        UserHolder.saveUser(user);
        String deviceType = (String) map.get("type");
        for(IotDeviceServer iotDeviceServer:iotDeviceServers){
            if(iotDeviceServer.findDeviceType(deviceType)){
                iotDeviceServer.addData(map);
                break;
            }
        }
        return Result.success(Message.SUCCESS);
    }

    @GetMapping(value = "/ask",produces = "text/plain;charset=utf-8")
    public Flux<String> generateStreamChat(@RequestParam("message") String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
    @PostMapping("/hello")
    public String hello(@RequestBody String me){
        return me;
    }
}


/**
 *   @GetMapping("/t1")
 *     public String hello(){
 *         return "hello";
 *     }
 *
 *     @Role("admin")
 *     @GetMapping("/t2")
 *     public String hello2(){
 *         return "hello1";
 *     }
 */
